package com.example.qjm3662.newproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TimePicker;

import com.example.qjm3662.newproject.Main_UI.MainActivity;
import com.example.qjm3662.newproject.Tool.ActivityAnim;
import com.example.qjm3662.newproject.Tool.Titanic;
import com.example.qjm3662.newproject.Tool.TitanicTextView;

public class LunchActivity extends Activity {

    private Context context;
    private Titanic titanic;
    private TitanicTextView myTitanicTextView;

//    @Override
//    protected void onResume() {
//        JPushInterface.onResume(this);
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        JPushInterface.onPause(this);
//        super.onPause();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        myTitanicTextView = (TitanicTextView) findViewById(R.id.titanic_tv);
        context = this;
        titanic = new Titanic(this);
        titanic.start(myTitanicTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);


        TimePicker timePicker = new TimePicker(this);
        timePicker.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(context, MainActivity.class);
//                startActivity(intent);
                ActivityAnim.startActivityWithAnim(LunchActivity.this, intent, ActivityAnim.ANIM_MODE_EXPLODE);
                finish();
//                overridePendingTransition(App.enterAnim, App.exitAnim);
            }
        }, 3000);

        System.out.println("this.getExternalCacheDir()" + this.getExternalCacheDir());
        System.out.println(this.getCacheDir());
        System.out.println(this.getFilesDir());
        System.out.println(Environment.getExternalStorageDirectory());
    }
}
