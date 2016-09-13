package com.example.qjm3662.newproject.myself.Care;

import android.app.ListActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Finding.HomePage;
import com.example.qjm3662.newproject.R;

public class care extends ListActivity implements View.OnClickListener {

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;

    private Care_other_Adapter adapter_other;
    private Care_me_Adapter adapter_me;

    private ChangeModeBroadCastReceiver receiver;

    private int flag;   //用来标记是关注还是被关注

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

        setContentView(R.layout.activity_care);
        Intent intent = getIntent();
        flag = intent.getFlags();
        initBar(flag);
        adapter_other = new Care_other_Adapter(this);
        adapter_me = new Care_me_Adapter(this);

        System.out.println(flag);
        if (flag == 1) {
            setListAdapter(adapter_other);
        } else {
            setListAdapter(adapter_me);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initBar(int flag) {
        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);

        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        img_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.getText().length();
        if (flag == 1) {
            tv_bar_center.setText("关注");
        } else {
            tv_bar_center.setText("粉丝");
        }

        img_bar_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_imageView_story:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, HomePage.class);
        i.putExtra("position", position);
        i.putExtra("FLAG_WHERE", flag);
        startActivity(i);
        overridePendingTransition(App.enterAnim, App.exitAnim);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }
}
