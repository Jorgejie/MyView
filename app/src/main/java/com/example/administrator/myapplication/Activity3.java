package com.example.administrator.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class Activity3 extends Activity implements View.OnClickListener {

    private ElesticCurveThree mEct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        mEct = (ElesticCurveThree) findViewById(R.id.ect);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                mEct.setMode(true);
                break;
            case R.id.btn2:
                mEct.setMode(false);
                break;
        }
    }
}
