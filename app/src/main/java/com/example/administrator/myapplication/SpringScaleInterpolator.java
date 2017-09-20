package com.example.administrator.myapplication;

import android.view.animation.Interpolator;

import static android.R.attr.factor;

/**
 * Created by Jorgejie on 2017/9/20.
 */

public class SpringScaleInterpolator implements Interpolator {
    private float mFactor;

    public SpringScaleInterpolator(float factor) {

        mFactor = factor;
    }

    @Override
    public float getInterpolation(float input) {
//        spring 的差值器
//        pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
