package com.example.administrator.myapplication.ratingstar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jorgejie on 2017/10/13.
 */

public class RatingStarView extends LinearLayout {

    private Drawable mStarOn;
    private Drawable mStarHalf;
    private Drawable mStarOff;
    private boolean mIsRatable;
    private float mPadding;
    private int mStarWidth;
    private int mHalfStarWidth;
    private int mPaddingLeft;
    private float  points[] = new float[11];    //0-10分点的x坐标，index为分数，值为此分数为坐标
    private List<ImageView> mList;
    private OnRateChangeListener mOnRateChangeListener;

    public RatingStarView(Context context) {
        this(context,null);
    }

    public RatingStarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatingStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingStarView, defStyleAttr, 0);
        mStarOn = typedArray.getDrawable(R.styleable.RatingStarView_star_on);
        mStarHalf = typedArray.getDrawable(R.styleable.RatingStarView_star_half);
        mStarOff = typedArray.getDrawable(R.styleable.RatingStarView_star_off);
        //默认可以滑动
        mIsRatable = typedArray.getBoolean(R.styleable.RatingStarView_isRatable, true);
        mStarWidth = mStarOn.getIntrinsicWidth();
        mPadding = typedArray.getDimension(R.styleable.RatingStarView_star_padding,mStarWidth / 3);

        mHalfStarWidth = mStarWidth / 2;

        mPaddingLeft = getPaddingLeft();
        mList = new ArrayList<>();
        ImageView imageView;
        //初始化五个点赞图片,并且通过points数组把分数与坐标对应起来,需要分析坐标系
        for(int i=0;i<5;i++) {
            imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                layoutParams.setMargins(0, 0, (int)mPadding / 2, 0);
                points[0] = 0;
                points[1] = mHalfStarWidth;
            }
            if (i != 0 && i != 4) {
                layoutParams.setMargins((int)mPadding / 2, 0, (int)mPadding / 2, 0);
                points[i*2] = points[i*2-1]+mHalfStarWidth+mPadding;
                points[i*2+1] = points[i*2]+mHalfStarWidth;
            }
            if(i==4){
                layoutParams.setMargins((int) mPadding / 2, 0, 0, 0);
                points[i*2] = points[i*2-1]+mHalfStarWidth+mPadding;
                points[i*2+1] = points[i*2]+mHalfStarWidth;
                points[10] = points[9]+mHalfStarWidth;
            }
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(mStarOff);
            mList.add(imageView);
            addView(mList.get(i));
        }
        setOrientation(LinearLayout.HORIZONTAL);
        typedArray.recycle();
    }

    //处理滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsRatable) {
            //如果不可以滑动则不处理事件
            return super.onTouchEvent(event);
        }
        int mark = calculateStar(event.getX());//获取x坐标位置并根据坐标计算对应的分值
        setRate(mark);
        return true;
    }

    private void setRate(int mark) {
        removeAllViews();
        int count = mark/2;
        boolean isOdd;//判断分数奇数偶数
        if (mark % 2 == 0) {
            isOdd = false;
        } else {
            isOdd = true;
        }
        for(int i=0;i<mList.size();i++) {
            if (i < count) {
                mList.get(i).setImageDrawable(mStarOn);
            } else {
                mList.get(i).setImageDrawable(mStarOff);
            }
            if (isOdd && i == count) {
                mList.get(i).setImageDrawable(mStarHalf);
            }
            addView(mList.get(i));
        }
        if(mIsRatable&&mOnRateChangeListener!=null){
            mOnRateChangeListener.onRateChange(mark);
        }
    }
    public void setOnRateChangeListener(OnRateChangeListener l){
        mOnRateChangeListener = l;

    }
    /**
     * 评分改变的回调
     */
    public interface OnRateChangeListener{
        void onRateChange(int rate);

    }

    private int calculateStar(float x) {
        //处理时减去控件的左padding
        float realPosition = x - mPaddingLeft;
        for(int i = 0;i<points.length;i++) {
            if (points[i] > realPosition) {
                return i;
            }
        }
        //如果滑动到最右侧或者超出范围,返回10分
        return 10;
    }
}
