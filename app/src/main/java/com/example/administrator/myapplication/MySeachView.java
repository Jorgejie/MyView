package com.example.administrator.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jorgejie on 2017/8/21.
 */

public class MySeachView extends View {
    // 画笔
    private Paint mPaint;

    // View 宽高
    private int mViewWidth;
    private int mViewHeight;

    // 当前的状态(非常重要)
    private State mCurrentState = State.NONE;

    // 放大镜与外部圆环
    private Path path_srarch;
    private Path path_circle;

    // 测量Path 并截取部分的工具
    private PathMeasure mMeasure;

    // 默认的动效周期 2s
    private int defaultDuration = 2000;

    // 控制各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;

    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    // 用于控制动画状态转换
    private Handler mAnimatorHandler;

    // 判断是否已经搜索结束
    private boolean isOver = false;

    private int count = 0;
    public MySeachView(Context context) {
        super(context);
        init();
    }

    public MySeachView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySeachView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //视图拥有的状态
    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    private void init() {
        // 进入开始动画
        mCurrentState = State.STARTING;

        initPaint();

        initPath();

        initListener();

        initHandler();

        initAnimator();


//        mStartingAnimator.start();
    }

    private void initAnimator() {

    }

    private void initHandler() {

    }

    private void initListener() {

    }

    private void initPath() {
        path_srarch = new Path();
        path_circle = new Path();
        mMeasure = new PathMeasure();

        //注意不要到360度,否则内部会自动优化,测量不能取到需要的数值
        RectF oval1 = new RectF(-50, -50, 50, 50);//放大镜圆环
        path_srarch.addArc(oval1, 45, 359.9f);

        RectF oval2 = new RectF(-100, -100, 100, 100);//外部圆环
        path_circle.addArc(oval2, 45, -359.9f);

        float[] pos = new float[2];
        mMeasure.setPath(path_circle, false);
        mMeasure.getPosTan(0, pos, null);
        path_srarch.lineTo(pos[0], pos[1]);//放大镜把手

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w / 2;
        mViewHeight = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSerch(canvas);
    }

    private void drawSerch(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.translate(mViewWidth,mViewHeight);
        mMeasure.setPath(path_srarch, false);
        Path dst = new Path();
        float length = mMeasure.getLength();
        mMeasure.getSegment(mMeasure.getLength(), mMeasure.getLength(), dst, true);
        canvas.drawPath(dst, mPaint);
    }
}
