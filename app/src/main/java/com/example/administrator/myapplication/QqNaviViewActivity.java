package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.example.administrator.myapplication.qqNaviView.QQMenuNavigation;

public class QqNaviViewActivity extends Activity {

    private QQMenuNavigation mBubbleView;

    public static void start(Context context) {
        Intent starter = new Intent(context, QqNaviViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_navi_view);
        mBubbleView = (QQMenuNavigation) findViewById(R.id.qq_view_bubble);
        mBubbleView.setBigIcon(R.drawable.bubble_big);
        mBubbleView.setSmallIcon(R.drawable.bubble_small);
    }

}
