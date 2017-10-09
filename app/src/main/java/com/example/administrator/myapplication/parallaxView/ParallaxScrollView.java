package com.example.administrator.myapplication.parallaxView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

/**
 * Created by Jorgejie on 2017/9/22.
 */

public class ParallaxScrollView extends ScrollView {

    private int mScaledDoubleTapSlop;
    private boolean isRestoring;
    private int mActivitePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    private float mDistance;
    private float mScale;

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件
        mScaledDoubleTapSlop = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        if (isRestoring && action == MotionEvent.ACTION_DOWN) {
            isRestoring = false;
        }
        if (!isEnabled() || isRestoring || (!isScrollToTop() && !isScrollToBottom())) {
            return super.onInterceptTouchEvent(event);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //获取当前事件特定指针的标识符
                mActivitePointerId = event.getPointerId(0);
                mIsBeingDragged = false;
                float initialMotionY = getMotionEventY(event);
                if (initialMotionY == -1) {
                    return super.onInterceptTouchEvent(event);
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivitePointerId == MotionEvent.INVALID_POINTER_ID) {
                    return super.onInterceptTouchEvent(event);
                }
                final float y = getMotionEventY(event);
                if (y == -1f) {
                    return super.onInterceptTouchEvent(event);
                }
                if (isScrollToTop() && !isScrollToBottom()) {//在顶部非底部
                    float yDiff = y - mInitialMotionY;
                    if (yDiff > mScaledDoubleTapSlop && !mIsBeingDragged) {
                        mIsBeingDragged = true;
                    }
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    //在底部不在顶部
                    float yDiff = mInitialMotionY - y;
                    if (yDiff > mScaledDoubleTapSlop && !mIsBeingDragged) {
                        mIsBeingDragged = true;
                    }
                } else if (isScrollToBottom() && isScrollToTop()) {
                    //即在顶部又在底部
                    float yDiff = y - mInitialMotionY;
                    if (Math.abs(yDiff) > mScaledDoubleTapSlop && !mIsBeingDragged) {
                        mIsBeingDragged = true;
                    }
                } else {
                    //既不在顶部也不再底部
                    return super.onInterceptTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivitePointerId = MotionEvent.INVALID_POINTER_ID;
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged || super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                mActivitePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float y = getMotionEventY(ev);
                if (isScrollToTop() && !isScrollToBottom()) {
                    //在顶部不在底部
                    mDistance = y - mInitialMotionY;
                    if (mDistance < 0) {
                        return super.onTouchEvent(ev);
                    }
                    mScale = calculateRate(mDistance);
                    pull(mScale);
                    return true;
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    //在底部不在顶部
                    mDistance = mInitialMotionY - y;
                    if (mDistance < 0) {
                        return super.onTouchEvent(ev);
                    }
                    mScale = calculateRate(mDistance);
                    push(mScale);
                    return true;
                } else if (isScrollToTop() && isScrollToBottom()) {
                    //在底部也在顶部
                    mDistance = y - mInitialMotionY;
                    if (mDistance > 0) {
                        mScale = calculateRate(mDistance);
                        pull(mScale);
                    } else {
                        mScale = calculateRate(-mDistance);
                        push(mScale);
                    }
                    return true;
                } else {
                    return super.onTouchEvent(ev);
                }
            case MotionEvent.ACTION_POINTER_DOWN:
                mActivitePointerId = ev.getPointerId(MotionEventCompat.getActionIndex(ev));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isScrollToTop() && !isScrollToBottom()) {
                    animateRestore(true);
                } else if (!isScrollToTop() && isScrollToBottom()) {
                    animateRestore(false);
                } else if (isScrollToTop() && isScrollToBottom()) {
                    if (mDistance > 0) {
                        animateRestore(true);
                    } else {
                        animateRestore(false);
                    }
                } else {
                    return super.onTouchEvent(ev);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void animateRestore(final boolean isPullRestore) {
        ValueAnimator animator = ValueAnimator.ofFloat(mScale, 1f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator(2f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (isPullRestore) {
                    pull(animatedValue);
                } else {
                    push(animatedValue);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRestoring = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRestoring = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void pull(float scale) {
        this.setPivotX(0);
        this.setScaleY(scale);
    }

    private void push(float scale) {
        this.setPivotY(this.getHeight());
        this.setScaleY(scale);
    }


    private float calculateRate(float distance) {
        float originalDragPercent = distance / (getResources().getDisplayMetrics().heightPixels);
        float dragPercent = Math.min(1f, originalDragPercent);
        float rate = 2f * dragPercent - (float) Math.pow(dragPercent, 2f);
        return 1 + rate / 5f;
    }

    private void onSecondaryPointerUp(MotionEvent event) {
        int actionIndex = MotionEventCompat.getActionIndex(event);
        int pointerId = event.getPointerId(actionIndex);
        if (pointerId == mActivitePointerId) {
            int newPointerIndex = actionIndex == 0 ? 1 : 0;
            mActivitePointerId = event.getPointerId(newPointerIndex);
        }
    }


    private float getMotionEventY(MotionEvent event) {
        int pointerIndex = event.findPointerIndex(mActivitePointerId);
        return pointerIndex < 0 ? -1f : event.getY(pointerIndex);
    }

    //判断是否滑动到顶部
    private boolean isScrollToTop() {
        return !ViewCompat.canScrollVertically(this, -1);
    }

    //判断是否滑动到底部
    private boolean isScrollToBottom() {
        //1-->表示向上滑动,-1-->向相反方向滑动
        return !ViewCompat.canScrollVertically(this, 1);
    }
}
