package com.example.administrator.myapplication.animibutton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

public class AnimButtonActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AnimButtonActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_button);
        final AnimiButton animiButton = (AnimiButton) findViewById(R.id.animibutton);
        animiButton.setAnimationButtonListener(new AnimiButton.AnimationButtonListener() {
            @Override
            public void onClickListener() {
                animiButton.start();
            }

            @Override
            public void animationFinish() {
                Toast.makeText(AnimButtonActivity.this, "执行完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
