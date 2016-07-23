package com.example.qjm3662.newproject.Finding;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.Finding.Comment.CommentActivity;
import com.example.qjm3662.newproject.NWO_2;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.StoryView.Edit_Story.Edit_Story;
import com.example.qjm3662.newproject.Tool.DestinyUtil;
import com.example.qjm3662.newproject.Tool.Tool;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StoryView extends FragmentActivity implements View.OnClickListener, OnMenuItemClickListener {


    private ImageView img_head;
    private TextView tv_name;
    private TextView tv_sign;
    private TextView tv_praise_num;
    private TextView tv_flag;
    private TextView tv_content;

    private ViewGroup rl_transmit;
    private ViewGroup rl_praise;
    private ViewGroup rl_collect;
    private ViewGroup rl_comment;
    private ImageView img_praise;
    private ImageView img_collect;
    private TextView tv_favour;
    private TextView tv_collect;

    private Intent intent;
    private StoryBean story;
    private UserBase userBase;
    private int position;
    private int position_child;


    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;
    private int flag;

    private boolean STATE_COLLECT;
    private boolean STATE_PRAISE;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private StoryOperatorReceiver storyOperatorReceiver;
    public static String ACTION_PRAISE = "click praise";

    private boolean praise_state = false;
    private boolean collect_state = false;

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

        setContentView(R.layout.activity_story_view);
        //获取故事包含的信息
        intent = getIntent();
        flag = intent.getIntExtra("flag", -1);
        position = intent.getIntExtra("position", 0);
        position_child = intent.getIntExtra("position_child", 0);
        int where = intent.getIntExtra("FLAG_WHERE", 0);      //0->广场列表， 1->关注列表， 2->被关注列表
        if (flag == 1) {
            Not_First_View(where);
        } else if (flag == 2) {    //浏览自己已上传的文章
            story = App.Public_My_Article_StoryList.get(position);
            userBase = User.getInstance();
        } else {
            story = App.Public_StoryList.get(position);
            userBase = story.getUser();
        }
        initView();
        fillInformation();
        initContextMenu();
        initReceiver();
        //NWO_2.GetCommentList(this, story.getId());
        App.comment_story = story;
    }


    private void initReceiver() {
        storyOperatorReceiver = new StoryOperatorReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PRAISE);//为BroadcastReceiver指定action，即要监听的消息名字。
        registerReceiver(storyOperatorReceiver, filter);//注册监听
    }

    private void initContextMenu() {
        Bitmap bitmap = null;
        MenuObject transmit = new MenuObject("转发");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_transmit);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30));
        transmit.setBitmap(bitmap);
        transmit.setBgResource(R.drawable.white);
        transmit.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        transmit.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject praise = new MenuObject("赞");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_favour_choose);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30));
        praise.setBitmap(bitmap);
        praise.setBgResource(R.drawable.white);
        praise.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        praise.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject collect = new MenuObject("收藏");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_collect_choose);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30));
        collect.setBitmap(bitmap);
        collect.setBgResource(R.drawable.white);
        collect.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        collect.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject comment = new MenuObject("评论");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_comment);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30));
        comment.setBitmap(bitmap);
        comment.setBgResource(R.drawable.white);
        comment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        comment.setMenuTextAppearanceStyle(R.style.TextViewStyle);
        comment.setTextColor(R.color.dark_normal);

        List<MenuObject> menuObjects = new ArrayList<>();
        menuObjects.add(transmit);
        menuObjects.add(praise);
        menuObjects.add(collect);
        menuObjects.add(comment);

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(DestinyUtil.dip2px(this, 80));
        menuParams.setMenuObjects(menuObjects);
        menuParams.setClosableOutside(true);
        // set other settings to meet your needs
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        Context context1 = mMenuDialogFragment.getContext();
        fragmentManager = getSupportFragmentManager();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.context_menu:
//                mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        //Do something here
    }

    private void Not_First_View(int where) {
        if(where == 0){
            userBase = App.Public_Story_User.get(position);
        }else if(where == 1){
            userBase = App.Public_Care_Other.get(position);
        }else if(where == 2){
            userBase = App.Public_Care_Me.get(position);
        }
        story = App.Public_HomePage_StoryList.get(position_child);
    }

    private void initView() {
        img_head = (ImageView) findViewById(R.id.user_info_item_head);
        tv_name = (TextView) findViewById(R.id.user_info_item_name);
        tv_sign = (TextView) findViewById(R.id.user_info_item_sign);
        tv_praise_num = (TextView) findViewById(R.id.tv_praise);
        tv_flag = (TextView) findViewById(R.id.tv_flag);
        tv_content = (TextView) findViewById(R.id.tv_content);

        rl_transmit = (ViewGroup) findViewById(R.id.rl_transmit);
        rl_praise = (ViewGroup) findViewById(R.id.rl_praise);
        rl_collect = (ViewGroup) findViewById(R.id.rl_collect);
        rl_comment = (ViewGroup) findViewById(R.id.rl_comment);
        img_praise = (ImageView) findViewById(R.id.praise);
        img_collect = (ImageView) findViewById(R.id.collect);
        tv_favour = (TextView) findViewById(R.id.tv_favour);
        tv_collect = (TextView) findViewById(R.id.tv_collect);

        img_head.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        tv_praise_num.setOnClickListener(this);
        tv_flag.setOnClickListener(this);
        tv_content.setOnClickListener(this);
        rl_transmit.setOnClickListener(this);
        rl_praise.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        rl_comment.setOnClickListener(this);
//        img_praise.setOnClickListener(this);
//        img_collect.setOnClickListener(this);


        img_bar_left = (ImageView) findViewById(R.id.cloud_imageView_story);
        img_bar_right = (ImageView) findViewById(R.id.add_imageView_story);
        tv_bar_center = (TextView) findViewById(R.id.tv_bar_center);
        tv_bar_right = (TextView) findViewById(R.id.tv_bar_right_text);

        img_bar_left.setImageResource(R.drawable.img_arrow_register);
        if (flag == 2) {
            img_bar_right.setImageResource(R.drawable.img_edit);
        } else {
            img_bar_right.setVisibility(View.INVISIBLE);
        }
        tv_bar_right.setVisibility(View.INVISIBLE);

        img_bar_left.setOnClickListener(this);
        img_bar_right.setOnClickListener(this);


        if (praise_state) {
            img_praise.setImageResource(R.drawable.img_favour_choose);
            tv_favour.setTextColor(getResources().getColor(R.color.green));
        } else {
            img_praise.setImageResource(R.drawable.img_favour);
            tv_favour.setTextColor(getResources().getColor(R.color.gray_normal));
        }

        if (collect_state) {
            img_collect.setImageResource(R.drawable.img_collect_choose);
            tv_collect.setTextColor(getResources().getColor(R.color.green));
        } else {
            img_collect.setImageResource(R.drawable.img_collect);
            tv_collect.setTextColor(getResources().getColor(R.color.gray_normal));
        }


        //设置可滑动
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void fillInformation() {
        //加载头像
        final String url = userBase.getAvatar();
        NetWorkOperator.Set_Avatar(url, img_head);

        tv_name.setText(userBase.getUserName());
        tv_sign.setText(userBase.getSign());
        tv_praise_num.setText(story.getLikeCount() + "");
        tv_flag.setText(story.getFlags());
        tv_content.setText(story.getContent());

        tv_bar_center.setText(story.getTitle());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_item_head:
                if (flag != 1) {
                    Intent intent = new Intent(this, HomePage.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
                break;
            case R.id.user_info_item_name:
                break;
            case R.id.user_info_item_sign:
                break;
            case R.id.tv_praise:

                break;
            case R.id.praise:

                break;
            case R.id.collect:

                break;
            case R.id.tv_flag:
                break;
            case R.id.tv_content:
                break;
            case R.id.cloud_imageView_story:
                onBackPressed();
                break;
            case R.id.add_imageView_story:
//                mMenuDialogFragment.show(fragmentManager, "lalala");
                Intent i = new Intent(this, Edit_Story.class);
                i.putExtra(Edit_Story.FLAG_WHERE_ARE_YOU_FROM, Edit_Story.FLAG_FROM_ONLINE_ARTICLE);
                i.putExtra("JUDGE", true);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.rl_praise:
                change_praise_state();
                NWO_2.PraiseSB(this, story.getId(), 1);
                break;
            case R.id.rl_collect:
                change_collect_state();
                NetWorkOperator.CollectStory(this, story.getId());
                break;
            case R.id.rl_comment:
                Intent intent = new Intent(this, CommentActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //NWO_2.GiveComment(this,story.getId());
                break;
            case R.id.rl_transmit:
                break;

        }
    }

    private void change_collect_state() {
        if (collect_state) {
            collect_state = false;
            img_collect.setImageResource(R.drawable.img_collect);
            tv_collect.setTextColor(getResources().getColor(R.color.gray_normal));
        } else {
            collect_state = true;
            img_collect.setImageResource(R.drawable.img_collect_choose);
            tv_collect.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private void change_praise_state() {
        if (praise_state) {
            praise_state = false;
            img_praise.setImageResource(R.drawable.img_favour);
            tv_favour.setTextColor(getResources().getColor(R.color.gray_normal));
        } else {
            praise_state = true;
            img_praise.setImageResource(R.drawable.img_favour_choose);
            tv_favour.setTextColor(getResources().getColor(R.color.green));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private class StoryOperatorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "click praise":
                    System.out.println("get praise broadcast");
                    System.out.println(story.getLikeCount());
                    System.out.println(story.toString());
                    //System.out.println(userBase.toString());
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(storyOperatorReceiver);
        unregisterReceiver(receiver);
    }
}
