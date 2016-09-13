package com.example.qjm3662.newproject.myself.settings;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.R;

public class Notification extends Activity implements View.OnClickListener {

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;

    private Button btn_switch_inform_praise;
    private Button btn_switch_inform_comment;
    private Button btn_switch_inform_collect;

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

        setContentView(R.layout.activity_notification);

        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);
        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        img_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.setText("设置");

        img_bar_left.setOnClickListener(this);
        btn_switch_inform_praise = (Button) findViewById(R.id.inform_is_praise_switch);
        btn_switch_inform_comment = (Button) findViewById(R.id.inform_is_comment_switch);
        btn_switch_inform_collect = (Button) findViewById(R.id.inform_is_collect_switch);

        btn_switch_inform_praise.setOnClickListener(this);
        btn_switch_inform_comment.setOnClickListener(this);
        btn_switch_inform_collect.setOnClickListener(this);

        initState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initState() {
        if (App.Switch_state_is_inform_praise) {
            btn_switch_inform_praise.setBackgroundResource(R.drawable.img_switch_choose);
        } else {
            btn_switch_inform_praise.setBackgroundResource(R.drawable.img_switch);
        }
        if (App.Switch_state_is_inform_comment) {
            btn_switch_inform_comment.setBackgroundResource(R.drawable.img_switch_choose);
        } else {
            btn_switch_inform_comment.setBackgroundResource(R.drawable.img_switch);
        }
        if (App.Switch_state_is_inform_collect) {
            btn_switch_inform_collect.setBackgroundResource(R.drawable.img_switch_choose);
        } else {
            btn_switch_inform_collect.setBackgroundResource(R.drawable.img_switch);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inform_is_praise_switch:
                System.out.println("a");
                if (App.Switch_state_is_inform_praise) {
                    btn_switch_inform_praise.setBackgroundResource(R.drawable.img_switch);
                    App.Switch_state_is_inform_praise = false;
                } else {
                    btn_switch_inform_praise.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Switch_state_is_inform_praise = true;
                }
                App.updateSwitchInfo(this);
                break;
            case R.id.inform_is_comment_switch:
                System.out.println("b");
                if (App.Switch_state_is_inform_comment) {
                    btn_switch_inform_comment.setBackgroundResource(R.drawable.img_switch);
                    App.Switch_state_is_inform_comment = false;
                } else {
                    btn_switch_inform_comment.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Switch_state_is_inform_comment = true;
                }
                App.updateSwitchInfo(this);
                break;
            case R.id.inform_is_collect_switch:
                System.out.println("c");
                if (App.Switch_state_is_inform_collect) {
                    btn_switch_inform_collect.setBackgroundResource(R.drawable.img_switch);
                    App.Switch_state_is_inform_collect = false;
                } else {
                    btn_switch_inform_collect.setBackgroundResource(R.drawable.img_switch_choose);
                    App.Switch_state_is_inform_collect = true;
                }
                App.updateSwitchInfo(this);
                break;
            case R.id.cloud_imageView_story:
                finish();
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;

        }
    }
}
