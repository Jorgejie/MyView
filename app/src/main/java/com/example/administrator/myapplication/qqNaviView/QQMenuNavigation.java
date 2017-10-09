package com.example.administrator.myapplication.qqNaviView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.R;

/**
 * Created by Jorgejie on 2017/10/9.
 */

public class QQMenuNavigation extends LinearLayout {

    private Context mContext;
    private int mBigPic;//初始化大图icon
    private int mSmallPic;//初始化小图icon

    public QQMenuNavigation(Context context) {
        super(context);
    }

    public QQMenuNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QQMenuNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQNaviView, defStyleAttr, 0);
        mBigPic = typedArray.getResourceId(R.styleable.QQNaviView_bigIconSrc, R.drawable.bubble_big);
        mSmallPic = typedArray.getResourceId(R.styleable.QQNaviView_bigIconSrc, R.drawable.bubble_small);

    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
