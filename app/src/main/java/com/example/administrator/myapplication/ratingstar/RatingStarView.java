package com.example.administrator.myapplication.ratingstar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;

/**
 * Created by Jorgejie on 2017/10/13.
 */

public class RatingStarView extends LinearLayout {

    private Drawable mStarOn;
    private Drawable mStarHalf;
    private Drawable mStarOff;
    private boolean mIsRatable;
    private float mPadding;

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
        mPadding = typedArray.getDimension(R.styleable.RatingStarView_star_padding,0);

        int halfStarWidth = mStarOn.getIntrinsicWidth() / 2;
        int starWidth = mStarOn.getIntrinsicWidth();

    }
}
