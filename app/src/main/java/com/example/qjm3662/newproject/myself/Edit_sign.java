package com.example.qjm3662.newproject.myself;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;

public class Edit_sign extends Activity implements View.OnClickListener {

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private EditText et_sign;
    private TextView tv_bat_right;

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


        setContentView(R.layout.activity_edit_sign);
        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        et_sign = (EditText) findViewById(R.id.sign_edit);
        tv_bat_right = (TextView) findViewById(R.id.tv_bar_right_text);

        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        img_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.setText("编辑签名");
        et_sign.setText(User.getInstance().getSign());

        img_bar_left.setOnClickListener(this);
        tv_bat_right.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_imageView_story:
                finish();
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.tv_bar_right_text:
                User.getInstance().setSign(et_sign.getText().toString());
                NetWorkOperator.UpDateUserInfo(this);
                finish();
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }

}
