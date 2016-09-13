package com.example.qjm3662.newproject.LoginAndRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.Final_Static_data;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;

public class Register_UI extends Activity implements View.OnClickListener {

    private TextView register_to_login;
    private Button btn_register;
    private Intent intent;
    private EditText et_phone_number;
    private EditText et_password;
    private Context context;

    private ChangeModeBroadCastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(App.Switch_state_mode){
            this.setTheme(R.style.AppTheme_night);
        }else {
            this.setTheme(R.style.AppTheme_day);
        }

        receiver = new ChangeModeBroadCastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CHANGE_MODE");
        registerReceiver(receiver, intentFilter);

        setContentView(R.layout.activity_register__ui);
        context = Register_UI.this;

        register_to_login = (TextView) findViewById(R.id.register_to_login);
        btn_register = (Button) findViewById(R.id.register_btn);
        et_phone_number = (EditText) findViewById(R.id.register_phone);
        et_password = (EditText) findViewById(R.id.register_password);

        register_to_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_to_login:
                intent = new Intent(Register_UI.this,LoginActivity.class);
                startActivityForResult(intent,Final_Static_data.REQUEST_CODE_REG_TO_LOGIN);
                overridePendingTransition(App.enterAnim, App.exitAnim);
                finish();
                break;
            case R.id.register_btn:
                NetWorkOperator.register(et_password, et_phone_number, Register_UI.this);
                break;
        }
    }

}
