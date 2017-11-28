package com.example.administrator.myapplication.animibutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Jorgejie on 2017/11/27.
 */

public class AnimiButton extends View {

    private AnimationButtonListener mAnimationButtonListener;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mOkPaint;
    private int mWidth;
    private int mHeight;

    /**
     * 路径--用来获取对勾的路径
     */
    private Path mPath = new Path();

    /**
     * 默认两圆圆心之间的距离=需要移动的距离
     */
    private int default_two_circle_distance;
    private PathMeasure mPathMeasure;
    private ValueAnimator mAnimator_rect_to_angle;
    private int mCircleAngle;
    private ObjectAnimator mAnimator_move_to_up;
    private int mTwo_circle_distance;
    private ValueAnimator mAnimator_draw_ok;
    private ValueAnimator mAnimator_rect_to_square;


    public AnimiButton(Context context) {
        this(context, null);
    }

    public AnimiButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimiButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimationButtonListener != null) {
                    mAnimationButtonListener.onClickListener();
                }
            }
        });

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (mAnimationButtonListener != null) {
                    mAnimationButtonListener.animationFinish();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 背景颜色
     */
    private int bg_color = 0xffbc7d53;

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(bg_color);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        mOkPaint = new Paint();
        mOkPaint.setStrokeWidth(10);
        mOkPaint.setStyle(Paint.Style.STROKE);
        mOkPaint.setAntiAlias(true);
        mOkPaint.setColor(Color.WHITE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw_oval_to_circle(canvas);
        drawText(canvas);
        if (startDrawOk) {
            canvas.drawPath(mPath, mOkPaint);
        }
    }

    /**
     * 按钮文字字符串
     */
    private String buttonString = "确认完成";

    /**
     * 文字绘制所在矩形
     */
    private Rect textRect = new Rect();

    private void drawText(Canvas canvas) {
        textRect.left = 0;
        textRect.top = 0;
        textRect.right = mWidth;
        textRect.bottom = mHeight;
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //文字绘制到整个布局的中心位置
        canvas.drawText(buttonString, textRect.centerX(), baseline, mTextPaint);
    }

    /**
     * 根据view的大小设置成矩形
     */
    private RectF rectf = new RectF();

    private void draw_oval_to_circle(Canvas canvas) {
        rectf.left = mTwo_circle_distance;
        rectf.top = 0;
        rectf.right = mWidth - mTwo_circle_distance;
        rectf.bottom = mHeight;

        //画圆角矩形
        canvas.drawRoundRect(rectf, mCircleAngle, mCircleAngle, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        default_two_circle_distance = (w - h) / 2;
        initOk();
        initAnimation();
    }


    private void initOk() {
        //对勾的路径
        mPath.moveTo(default_two_circle_distance + mHeight / 8 * 3, mHeight / 2);
        mPath.lineTo(default_two_circle_distance + mHeight / 2, mHeight / 5 * 3);
        mPath.lineTo(default_two_circle_distance + mHeight / 3 * 2, mHeight / 5 * 2);

        mPathMeasure = new PathMeasure(mPath, true);
    }

    private void initAnimation() {
        set_rect_to_angle_animation();
        set_cirrect_to_circle_animation();
        set_move_to_up_animation();
        set_draw_ok_animation();

        mAnimatorSet
                .play(mAnimator_move_to_up)
                .before(mAnimator_draw_ok)//最后播放
                .after(mAnimator_rect_to_square)//在play之前播放
                .after(mAnimator_rect_to_angle);//最先播放
    }

    private int duration = 1000;

    /**
     * 设置矩形过度到圆角矩形的动画
     */
    private void set_rect_to_angle_animation() {
        mAnimator_rect_to_angle = ValueAnimator.ofInt(0, mHeight / 2);
        mAnimator_rect_to_angle.setDuration(duration);
        mAnimator_rect_to_angle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCircleAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }


    /**
     * 设置圆角矩形过度到圆的动画
     */
    private void set_cirrect_to_circle_animation() {
        mAnimator_rect_to_square = ValueAnimator.ofInt(0, default_two_circle_distance);
        mAnimator_rect_to_square.setDuration(duration);
        mAnimator_rect_to_square.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTwo_circle_distance = (int) animation.getAnimatedValue();
                int alpha = 255 - (mTwo_circle_distance * 255) / default_two_circle_distance;//逐步减小透明度
                mTextPaint.setAlpha(alpha);
                invalidate();
            }
        });
    }

    /**
     * view向上移动距离
     */
    private int move_distance = 300;

    /**
     * 设置view上移的动画
     */
    private void set_move_to_up_animation() {
        float curTranslationY = this.getTranslationY();
        mAnimator_move_to_up = ObjectAnimator.ofFloat(this, "translationY", curTranslationY, curTranslationY - move_distance);
        mAnimator_move_to_up.setDuration(duration);
        mAnimator_move_to_up.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private boolean startDrawOk;

    /**
     * 绘制对勾的动画
     */
    private void set_draw_ok_animation() {
        mAnimator_draw_ok = ValueAnimator.ofFloat(1, 0);
        mAnimator_draw_ok.setDuration(duration);
        mAnimator_draw_ok.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDrawOk = true;
                float value = (float) animation.getAnimatedValue();
                DashPathEffect effect = new DashPathEffect(new float[]{mPathMeasure.getLength(), mPathMeasure.getLength()}, value * mPathMeasure.getLength());
                mOkPaint.setPathEffect(effect);
                invalidate();
            }
        });
    }

    public void start() {
        mAnimatorSet.start();
    }


    public void setAnimationButtonListener(AnimationButtonListener listener) {
        mAnimationButtonListener = listener;
    }

    /**
     * 接口回调
     */
    public interface AnimationButtonListener {
        /**
         * 按钮点击事件
         */
        void onClickListener();

        /**
         * 动画完成回调
         */
        void animationFinish();
    }
}
