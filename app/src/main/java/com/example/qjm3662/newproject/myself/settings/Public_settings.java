package com.example.qjm3662.newproject.myself.settings;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.R;

public class Public_settings extends Activity implements View.OnClickListener {

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;

    private ViewGroup l_is_public_fan;
    private ViewGroup l_is_public_like;
    private Button btn_switch_fan;
    private Button btn_switch_like;

    private ChangeModeBroadCastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.Switch_state_mode) {
            this.setTheme(R.style.AppTheme_night);
        } else {
            this.setTheme(R.style.AppTheme_day);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CHANGE_MODE");
        receiver = new ChangeModeBroadCastReceiver(this);
        registerReceiver(receiver, intentFilter);
        setContentView(R.layout.activity_public_settings);

        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);
        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        img_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.setText("设置");

        l_is_public_fan = (ViewGroup) findViewById(R.id.l_is_public_fan);
        l_is_public_like = (ViewGroup) findViewById(R.id.l_is_public_like);
        btn_switch_fan = (Button) findViewById(R.id.public_switch_fan);
        btn_switch_like = (Button) findViewById(R.id.public_switch_like);

        img_bar_left.setOnClickListener(this);
        l_is_public_fan.setOnClickListener(this);
        l_is_public_like.setOnClickListener(this);
        btn_switch_fan.setOnClickListener(this);
        btn_switch_like.setOnClickListener(this);

        initState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initState() {
        if (App.Switch_state_is_public_fan) {
            btn_switch_fan.setBackgroundResource(R.drawable.img_switch_choose);
        } else {
            btn_switch_fan.setBackgroundResource(R.drawable.img_switch);
        }
        if (App.Switch_state_is_public_like) {
            btn_switch_like.setBackgroundResource(R.drawable.img_switch_choose);
        } else {
            btn_switch_like.setBackgroundResource(R.drawable.img_switch);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_imageView_story:
                finish();
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.l_is_public_fan:

                break;
            case R.id.l_is_public_like:

                break;
            case R.id.public_switch_fan:
                if (App.Switch_state_is_public_fan) {
                    btn_switch_fan.setBackgroundResource(R.drawable.img_switch);
                    App.Switch_state_is_public_fan = false;
                } else {
                    btn_switch_fan.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Switch_state_is_public_fan = true;
                }
                App.updateSwitchInfo(this);
                break;
            case R.id.public_switch_like:
                if (App.Switch_state_is_public_like) {
                    btn_switch_like.setBackgroundResource(R.drawable.img_switch);
                    App.Switch_state_is_public_like = false;
                } else {
                    btn_switch_like.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Switch_state_is_public_like = true;
                }
                App.updateSwitchInfo(this);
                break;
        }
    }
}
