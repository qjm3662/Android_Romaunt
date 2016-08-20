package com.example.qjm3662.newproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 日、夜间
 * Created by qjm3662 on 2016/7/20 0020.
 */
public class ChangeModeBroadCastReceiver extends BroadcastReceiver {

    private Activity activity;

    public ChangeModeBroadCastReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.finish();
        activity.startActivity(new Intent(activity ,activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        System.out.println("finish : " + activity.getPackageName());
        System.out.println("STATE3 : " + App.Switch_state_mode);
    }
}
