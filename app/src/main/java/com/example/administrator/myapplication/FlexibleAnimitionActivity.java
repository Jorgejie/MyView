package com.example.administrator.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class FlexibleAnimitionActivity extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private Button normalAnimation;
    private Button springAnimationOne;
    private Button springAnimationTwo;
    private Button springAnimationThree;

    public static void start(Context context) {
        Intent starter = new Intent(context, FlexibleAnimitionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexible_animition);
        imageView = (ImageView) findViewById(R.id.animation_image);

        normalAnimation = (Button) findViewById(R.id.normal_animation);//普通动画
        springAnimationOne = (Button) findViewById(R.id.spring_animation_one);//插值器弹性动画
        springAnimationTwo = (Button) findViewById(R.id.spring_animation_two);//rebound_弹性动画
        springAnimationThree = (Button) findViewById(R.id.spring_animation_three);//SpringAnimation

        normalAnimation.setOnClickListener(this);
        springAnimationOne.setOnClickListener(this);
        springAnimationTwo.setOnClickListener(this);
        springAnimationThree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_animation:
                onScaleAnimation();
                break;
            case R.id.spring_animation_one:
                //带有差值器的缩放
                onScaleAnimationBySpringWayOne();
                break;
            case R.id.spring_animation_two:
                onScaleAnimationBySpringWayTwo(); //利用rebound完成弹性动画
                break;
            case R.id.spring_animation_three:
                onScaleAnimationBySpringWayThree();//利用SprignAnimation完成动画
                break;
        }
    }




    private void onScaleAnimation() {
        /**
         * 第二个参数propertyName详解:
         *    1,缩放
         *          scaleX:x轴向缩放
         *          scaleY:y轴向缩放
         *    2,平移
         *          translationX
         *          translationY
         *    3,旋转
         *          rotationX:围绕X轴旋转
         *          rotationY:围绕Y轴旋转
         *          rotation:围绕z轴旋转
         *    4,透明度
         *          alpha
         */
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.8f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(scaleX, scaleY);
        set.start();
    }

    private void onScaleAnimationBySpringWayOne() {
        /**
         * 计算差值器的网站
         * http://inloop.github.io/interpolator/
         */
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.8f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new SpringScaleInterpolator(0.4f));
        set.playTogether(scaleX, scaleY);
        set.start();
    }


    private void onScaleAnimationBySpringWayTwo() {
        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.setCurrentValue(1.0f);
        //springConfig中第一个参数是拉力值,第二个为摩擦因数
        spring.setSpringConfig(new SpringConfig(50, 5));
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float currentValue = (float) spring.getCurrentValue();
                imageView.setScaleX(currentValue);
                imageView.setScaleY(currentValue);
            }
        });
        spring.setEndValue(3.0f);
    }

    private void onScaleAnimationBySpringWayThree() {
        SpringAnimation animationX = new SpringAnimation(imageView, SpringAnimation.SCALE_X, 1.8f);
        SpringAnimation animationY = new SpringAnimation(imageView, SpringAnimation.SCALE_Y, 1.8f);
        animationX.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        animationX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        animationX.setStartValue(1.0f);

        animationY.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        animationY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        animationY.setStartValue(1.0f);

        animationX.start();
        animationY.start();
    }
}
