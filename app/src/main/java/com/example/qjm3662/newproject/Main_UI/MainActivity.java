package com.example.qjm3662.newproject.Main_UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Finding.Finding_fragment;
import com.example.qjm3662.newproject.Message.MessageFragment;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.StoryView.Edit_Acticity;
import com.example.qjm3662.newproject.StoryView.StoryFragment;
import com.example.qjm3662.newproject.myself.main.Myself;


public class MainActivity extends FragmentActivity implements OnClickListener {

    private ImageView story;
    private ImageView find;
    private ImageView message;
    private ImageView my;
    private StoryFragment storyFragment;
    private Finding_fragment findFragment;
    private MessageFragment messageFragment;
    private Myself myFragment;
    private FragmentManager fragmentManager;

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);
        img_bar_left.setImageResource(R.drawable.img_cloud);
        img_bar_right.setImageResource(R.drawable.img_add);
        tv_bar_right.setVisibility(View.INVISIBLE);
        tv_bar_center.setText("故事");

        fragmentManager = getSupportFragmentManager();
        init();
        setTab_selection(0);
        App.getSwitchInfo(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.story:
                img_bar_left.setImageResource(R.drawable.img_cloud);
                img_bar_right.setImageResource(R.drawable.img_add);
                tv_bar_center.setText("故事");
                setTab_selection(0);
                break;
            case R.id.find:
                img_bar_left.setImageResource(R.color.green);
                img_bar_right.setImageResource(R.color.green);
                tv_bar_center.setText("发现");
                setTab_selection(1);
                break;
            case R.id.message:
                img_bar_left.setImageResource(R.color.green);
                img_bar_right.setImageResource(R.color.green);
                tv_bar_center.setText("信息");
                setTab_selection(2);
                break;
            case R.id.my:
                img_bar_left.setImageResource(R.color.green);
                img_bar_right.setImageResource(R.color.green);
                tv_bar_center.setText("我");
                setTab_selection(3);
                break;
        }
    }


    private void clearSelection() {
        story.setImageResource(R.drawable.img_story);
        find.setImageResource(R.drawable.img_findings);
        message.setImageResource(R.drawable.img_message);
        my.setImageResource(R.drawable.img_my);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (storyFragment != null) {
            transaction.hide(storyFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    private void setTab_selection(int index) {
        //每次选中前先清除掉上次选中的状态
        clearSelection();
        //开启一个Fragment事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的fragment,以防有多个fragment显示在界面上
        hideFragments(transaction);

        switch (index) {
            case 0:
                story.setImageResource(R.drawable.img_story_choose);
                if (storyFragment == null) {
                    storyFragment = new StoryFragment();
                    transaction.add(R.id.frameLayout, storyFragment);
                } else {
                    transaction.show(storyFragment);
                }
                break;

            case 1:
                find.setImageResource(R.drawable.img_findings_choose);
                if (findFragment == null) {
                    findFragment = new Finding_fragment();
                    transaction.add(R.id.frameLayout, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;

            case 2:
                message.setImageResource(R.drawable.img_message_choose);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.frameLayout, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;

            case 3:
                my.setImageResource(R.drawable.img_my_choose);
                if (myFragment == null) {
                    myFragment = new Myself();
                    transaction.add(R.id.frameLayout, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
        }

        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.dbWrite.close();
        App.dbRead.close();
    }


    private void init() {
        story = (ImageView) findViewById(R.id.story);
        find = (ImageView) findViewById(R.id.find);
        message = (ImageView) findViewById(R.id.message);
        my = (ImageView) findViewById(R.id.my);

        story.setOnClickListener(this);
        find.setOnClickListener(this);
        message.setOnClickListener(this);
        my.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Edit_Acticity.REQUEST_CODE_SAVE:
                System.out.println("save fail");
                if (storyFragment != null) {
                    System.out.println("save success");
                    storyFragment.slideAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

}
