package com.example.administrator.myapplication.qqNaviView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;

import static android.R.attr.x;
import static android.R.attr.y;


/**
 * Created by Jorgejie on 2017/10/9.
 * 仿qq底部导航拖拽效果
 */

public class QQMenuNavigation extends LinearLayout {

    private Context mContext;
    private int mBigPic;//初始化大图icon
    private int mSmallPic;//初始化小图icon
    private float mIconWidth;
    private float mIconHeight;
    private float mRange;
    private View mView;
    private ImageView mBigicon;
    private ImageView mSmallicon;
    private float mSmallRadius;
    private float lastX;
    private float lastY;
    private float mMBigRadius;

    public QQMenuNavigation(Context context) {
        this(context, null);
    }

    public QQMenuNavigation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQMenuNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQNaviView, defStyleAttr, 0);
        mBigPic = typedArray.getResourceId(R.styleable.QQMenuNavigation_bigIconSrc, R.drawable.bubble_big);
        mSmallPic = typedArray.getResourceId(R.styleable.QQMenuNavigation_bigIconSrc, R.drawable.bubble_small);
        mIconWidth = typedArray.getDimension(R.styleable.QQMenuNavigation_iconWidth, dp2px(context, 60));
        mIconHeight = typedArray.getDimension(R.styleable.QQMenuNavigation_iconHeight, dp2px(context, 60));
        mRange = typedArray.getFloat(R.styleable.QQMenuNavigation_range, 1);
        typedArray.recycle();

        //默认垂直排序
        setOrientation(LinearLayout.VERTICAL);
        init(context);
    }


    private void init(Context context) {
        mView = inflate(context, R.layout.view_icon, null);
        mBigicon = (ImageView) mView.findViewById(R.id.iv_big);
        mSmallicon = (ImageView) mView.findViewById(R.id.iv_small);
        mBigicon.setImageResource(mBigPic);
        mSmallicon.setImageResource(mSmallPic);
        setWidthAndHeight(mBigicon);
        setWidthAndHeight(mSmallicon);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mView.setLayoutParams(layoutParams);
        addView(mView);
    }

    //设置icon的宽高
    private void setWidthAndHeight(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = (int) mIconWidth;
        layoutParams.height = (int) mIconHeight;
        view.setLayoutParams(layoutParams);
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setupView();
        measureDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 确定view以及拖动的相关参数
     */
    private void setupView() {
        //根据view的宽高确定可拖动的半径的大小
        mSmallRadius = 0.1f * Math.min(mView.getMeasuredWidth(), mView.getMeasuredHeight()) * mRange;
        mMBigRadius = 1.5f * mSmallRadius;
        //给图片设置padding值,否则拖动,图片边缘会消失
        int padding = (int) mMBigRadius;
        mBigicon.setPadding(padding, padding, padding, padding);
        mSmallicon.setPadding(padding, padding, padding, padding);
    }

    private void measureDimension(int widthMeasureSpec, int heightMeasureSpec) {
        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //mode的三种模式
        //unspecified : (父控件不对子空间添加任何约束)
//        public static final int UNSPECIFIED = 0 << MODE_SHIFT;
        //exactly : 父控件完全指定子View的大小
//        public static final int EXACTLY     = 1 << MODE_SHIFT;
        //at_most = 子view的大小不能超出父View指定的最大的size
//        public static final int AT_MOST     = 2 << MODE_SHIFT;
        final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                final int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                width += childWidth;
                height += childHeight;
            }
        }
        width += (getPaddingLeft() + getPaddingRight());
        height += (getPaddingTop() + getPaddingBottom());
        //如果此处已经确切指定了子View的大小就直接使用获取到的sizeWidth(sizeHeight),否则使用width(height)
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    //设置布局位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childleft;
        int childTop = 0;
        for (int i = 0; i < getChildCount(); i++) {//此处一共包含两个子view,一个是图片,另外一个就是textView
            final View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeght = child.getMeasuredHeight();
                //水平居中显示
                childleft = (getWidth() - childWidth) / 2;
                //计算当前子View的top
                childTop += lp.topMargin;
                //设置子View的位置
                child.layout(childleft, childTop, childleft + childWidth, childTop + childHeght);
                //textView的top是当前图片view的top + height + botoommargin;
                childTop += childHeght + lp.bottomMargin;
            }
        }
    }

    //手势操作
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
//            ACTION_DOWN	手指 初次接触到屏幕 时触发。
//            ACTION_MOVE	手指 在屏幕上滑动 时触发，会多次触发。
//            ACTION_UP	手指 离开屏幕 时触发。
//            ACTION_CANCEL	事件 被上层拦截 时触发。
//            ACTION_OUTSIDE	手指 不在控件区域 时触发。
            case MotionEvent.ACTION_DOWN:
                //记录落点位置
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                float deltaX = x - lastX;
                float deltaY = y - lastY;
                //前面定义的拖动参数大的拖动半径是小的拖动半径的1.5倍,这里处理小图标的时候相应的乘以1.5
                moveEvent(mBigicon, deltaX, deltaY, mSmallRadius);
                moveEvent(mSmallicon, 1.5f * deltaX, 1.5f * deltaY, mMBigRadius);

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //抬起时复位
                mBigicon.setX(0);
                mBigicon.setY(0);
                mSmallicon.setX(0);
                mSmallicon.setY(0);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void moveEvent(ImageView view, float deltaX, float deltaY, float radius) {
        //首先计算拖动距离
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        //拖动的角度
        double degree = Math.atan2(deltaY, deltaX);
        //判断拖动的最大距离不能大于临界半径,如果大于保持临界状态
        if (distance > radius) {
            view.setX(view.getLeft()+(float)(radius*Math.cos(degree)));
            view.setY(view.getTop() + (float)(radius * Math.sin(degree)));
        } else {
            view.setX(view.getLeft() + deltaX);
            view.setY(view.getTop() + deltaY);
        }

    }


    public void setBigIcon(int res){
        mBigicon.setImageResource(res);
    }

    public void setSmallIcon(int res){
        mSmallicon.setImageResource(res);
    }

    public void setIconWidthAndHeight(float width, float height){
        mIconWidth = dp2px(mContext, width);
        mIconHeight = dp2px(mContext, height);
        setWidthAndHeight(mBigicon);
        setWidthAndHeight(mSmallicon);
    }

    public void setRange(float range){
        mRange = range;
    }
}
