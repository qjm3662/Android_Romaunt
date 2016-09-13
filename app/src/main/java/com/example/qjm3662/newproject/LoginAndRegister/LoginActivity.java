package com.example.qjm3662.newproject.LoginAndRegister;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.Final_Static_data;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back;
    private Button btn_login;
    private TextView tv_forget_password;
    private EditText et_username;
    private EditText et_password;
    private boolean connect_flag = false;
    private ConnectivityManager manager;
    private Context context;

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
        setContentView(R.layout.login_ui);

        img_back = (ImageView) findViewById(R.id.bar_back);
        btn_login = (Button) findViewById(R.id.login_btn);
        tv_forget_password = (TextView) findViewById(R.id.forget_password);
        et_username = (EditText) findViewById(R.id.login_phone);
        et_password = (EditText) findViewById(R.id.login_password);
        context = LoginActivity.this;

        img_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    /**
     * 设置网络状态标记
     */
    public void SetConect_flag() {
        //得到网络连接信息
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        //去进行判断网络是否连接
        if (networkInfo != null) {
            System.out.println(networkInfo.getTypeName());
            tv_forget_password.setText(networkInfo.getTypeName());
            switch (networkInfo.getTypeName()) {
                case "WIFI":
                    if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                        connect_flag = true;
                    } else {
                        tv_forget_password.setText("对不起");
                        connect_flag = false;
                    }
                    break;
                case "MOBILE":
                case "mobile":
                    connect_flag = true;
                    break;
            }
        } else {
            tv_forget_password.setText("没有检测到4G网络");
            connect_flag = false;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bar_back:
//                Intent intent = new Intent(context,Register_UI.class);
//                startActivityForResult(intent, Final_Static_data.REQUEST_CODE_LOG_BACK);
//                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
                break;
            case R.id.login_btn:
                SetConect_flag();
                NetWorkOperator.Login(et_username, et_password, connect_flag, context);
                break;
            case R.id.forget_password:
                Intent intent = new Intent(context, Register_UI.class);
                startActivityForResult(intent, Final_Static_data.REQUEST_CODE_LOG_BACK);
                overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
        }
    }
}
