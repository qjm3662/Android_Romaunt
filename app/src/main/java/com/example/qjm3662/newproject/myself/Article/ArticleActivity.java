package com.example.qjm3662.newproject.myself.Article;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.Finding.StoryView;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.yalantis.phoenix.PullToRefreshView;

public class ArticleActivity extends ListActivity implements View.OnClickListener {

    public static MyArticleAdapter adapter;
    private UserBase userBase;
    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;

    public static PullToRefreshView mPullToRefreshView;
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


        setContentView(R.layout.activity_article);
        context = this;

        adapter = new MyArticleAdapter(this);
        setListAdapter(adapter);
        userBase = User.getInstance();

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetWorkOperator.Get_Person_Story_List(context, userBase.getId() + "", 1, 1);
            }
        });

        NetWorkOperator.Get_Person_Story_List(context, String.valueOf(userBase.getId()), 0, 1);
        initBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    private void initBar() {
        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);

        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        img_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.getText().length();
        img_bar_left.setOnClickListener(this);
        img_bar_left.setOnClickListener(this);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int p, long id) {
        super.onListItemClick(l, v, p, id);
        Intent intent = new Intent(this, StoryView.class);
        intent.putExtra("flag", 2);
        intent.putExtra("position", p);
        startActivity(intent);
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_imageView_story:
                onBackPressed();
                break;
            case R.id.add_imageView_story:

                break;
        }
    }
}
