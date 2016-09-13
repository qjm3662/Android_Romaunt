package com.example.qjm3662.newproject.myself.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Finding.Finding_fragment;
import com.example.qjm3662.newproject.LoginAndRegister.LoginActivity;
import com.example.qjm3662.newproject.R;

public class my_settings extends Activity implements View.OnClickListener {

    private ViewGroup l_public;
    private ViewGroup l_notification;
    private ViewGroup l_wifi_sync;
    private ViewGroup l_refer;
    private Button btn_switch;
    private TextView tv_exit;

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;


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

        setContentView(R.layout.activity_my_settings);
        initView();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initView() {
        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);


        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        img_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.setText("设置");
        img_bar_left.setOnClickListener(this);


        l_public = (ViewGroup) findViewById(R.id.l_public);
        l_notification = (ViewGroup) findViewById(R.id.l_notification);
        l_refer = (ViewGroup) findViewById(R.id.l_refer);
        l_wifi_sync = (ViewGroup) findViewById(R.id.l_wifi_sync);
        btn_switch = (Button) findViewById(R.id.my_switch_button_settings);
        tv_exit = (TextView) findViewById(R.id.tv_exit);

        l_public.setOnClickListener(this);
        l_notification.setOnClickListener(this);
        l_refer.setOnClickListener(this);
        l_wifi_sync.setOnClickListener(this);
        btn_switch.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        System.out.println("alala");

        if (App.Switch_state_wifi_sync) {
            btn_switch.setBackgroundResource(R.drawable.img_switch_choose);
        } else {
            btn_switch.setBackgroundResource(R.drawable.img_switch);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_imageView_story:
                finish();
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.l_public:
                startActivity(new Intent(this, Public_settings.class));
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.l_notification:
                startActivity(new Intent(this, Notification.class));
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.l_refer:
                break;
            case R.id.l_wifi_sync:
                break;
            case R.id.my_switch_button_settings:
                if (App.Switch_state_wifi_sync) {
                    btn_switch.setBackgroundResource(R.drawable.img_switch);
                    App.Switch_state_wifi_sync = false;
                } else {
                    btn_switch.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Switch_state_wifi_sync = true;
                }
                App.updateSwitchInfo(this);
                break;
            case R.id.tv_exit:
                User.deleteUser();
                System.out.println("After delete" + User.getInstance().getAvatar());
                //finish();
                Intent intent = new Intent();
                intent.setAction("GET_INFO");
                sendBroadcast(intent);//传递过去

                //将用户信息存储到SharePreferences中
                SharedPreferences sp = getSharedPreferences("User_", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("user_info", "");
                editor.apply();
                sp = getSharedPreferences("User", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("user_info", "");
                editor.apply();

                App.clearInformation();
                if (Finding_fragment.adapter != null) {
                    Finding_fragment.adapter.notifyDataSetChanged();
                }

                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(App.enterAnim, App.exitAnim);
                finish();
                break;
        }
    }
}
