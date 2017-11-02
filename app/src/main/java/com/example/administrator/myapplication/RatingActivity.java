package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.example.administrator.myapplication.ratingstar.RatingNoHalfStarView;
import com.example.administrator.myapplication.ratingstar.RatingStarView;

public class RatingActivity extends Activity {
    public static void start(Context context) {
        Intent starter = new Intent(context, RatingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        final TextView tv = (TextView) findViewById(R.id.tv);
        RatingStarView srv_ratable = (RatingStarView) findViewById(R.id.srv_ratable);
        RatingNoHalfStarView rnhsv = (RatingNoHalfStarView) findViewById(R.id.rating_nohalf);
        srv_ratable.setOnRateChangeListener(new RatingStarView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
//                tv.setText(rate+"分");
            }
        });
//        rnhsv.setIsRatable(false);
        int mark = 10;
        rnhsv.setStarCountByUser(this, mark/2);
        rnhsv.setRateNotRatable(mark);
        rnhsv.setOnRateChangeListener(new RatingNoHalfStarView.OnRateChangeListener() {
            @Override
            public void onRateChange(int rate) {
                tv.setText(rate+"分");
            }
        });
    }

}
