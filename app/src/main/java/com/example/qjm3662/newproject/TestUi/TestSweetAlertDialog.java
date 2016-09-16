package com.example.qjm3662.newproject.TestUi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Tool.EasySweetAlertDialog;

public class TestSweetAlertDialog extends AppCompatActivity implements View.OnClickListener {

    private Button btn_waring;
    private Button btn_success;
    private Button btn_error;
    private Button btn_process;
    private Button btn_normal;
    private Button btn_custom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sweet_alert_dialog);

        btn_custom = (Button) findViewById(R.id.btn_custom);
        btn_error = (Button) findViewById(R.id.btn_error);
        btn_normal = (Button) findViewById(R.id.btn_normal);
        btn_process = (Button) findViewById(R.id.btn_process);
        btn_success = (Button) findViewById(R.id.btn_success);
        btn_waring = (Button) findViewById(R.id.btn_waring);

        btn_success.setOnClickListener(this);
        btn_custom.setOnClickListener(this);
        btn_error.setOnClickListener(this);
        btn_normal.setOnClickListener(this);
        btn_process.setOnClickListener(this);
        btn_waring.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_custom:
                EasySweetAlertDialog.ShowCustom(this, "Test");
                break;
            case R.id.btn_success:
                EasySweetAlertDialog.ShowSuccess(this, "Test");
                break;
            case R.id.btn_error:
                EasySweetAlertDialog.ShowError(this, "Test");
                break;
            case R.id.btn_normal:
                EasySweetAlertDialog.ShowNormal(this, "Test");
                break;
            case R.id.btn_process:
                EasySweetAlertDialog.ShowProcess(this, "Test");
                break;
            case R.id.btn_waring:
                EasySweetAlertDialog.ShowTip(this, "Test");
                break;
        }
    }
}
