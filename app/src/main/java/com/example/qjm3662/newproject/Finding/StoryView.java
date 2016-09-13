package com.example.qjm3662.newproject.Finding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.Bitmap_file_dir;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Finding.Comment.CommentActivity;
import com.example.qjm3662.newproject.Finding.Comment.UploadingHandler;
import com.example.qjm3662.newproject.NWO_2;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.StoryView.Edit_Story;
import com.example.qjm3662.newproject.Tool.DestinyUtil;
import com.example.qjm3662.newproject.Tool.Tool;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StoryView extends FragmentActivity implements View.OnClickListener, OnMenuItemClickListener {


    private ImageView img_head;
    private TextView tv_name;
    private TextView tv_sign;
    private TextView tv_praise_num;
    private TextView tv_flag;
    private EditText tv_content;

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
    private User user;
    private int position;
    private int position_child;


    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;
    private int flag;

    private ContextMenuDialogFragment mMenuDialogFragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private StoryOperatorReceiver storyOperatorReceiver;
    public static String ACTION_PRAISE = "click praise";


    public static final String FLAG_WHOSE_STORY = "flag";
    public static final int FLAG_WHOSE_STORY_NOT_FIRST_VIEW = 1;
    public static final int FLAG_WHOSE_STORY_MY_STORY = 2;

    public static final String FLAG_POSITION = "position";
    public static final String FLAG_CHILD_POSITION = "position_child";


    public static final String FLAG_WHERE = "FLAG_WHERE";       //故事点击来源
    public static final int FLAG_WHERE_PUBLIC = 0;              //广场故事
    public static final int FLAG_WHERE_CARE_OTHER = 1;          //关注的人列表
    public static final int FLAG_WHERE_CARE_ME = 2;             //粉丝列表

    private boolean praise_state = false;
    private boolean collect_state = false;

    private ChangeModeBroadCastReceiver receiver;

    private float width;
    private SweetAlertDialog pDialog;

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

        //获取手机的高度和宽度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）


        //获取故事包含的信息
        intent = getIntent();
        flag = intent.getIntExtra(FLAG_WHOSE_STORY, -1);
        position = intent.getIntExtra(FLAG_POSITION, 0);
        position_child = intent.getIntExtra(FLAG_CHILD_POSITION, 0);
        int where = intent.getIntExtra(FLAG_WHERE, 0);      //0->广场列表， 1->关注列表， 2->被关注列表

        if (flag == FLAG_WHOSE_STORY_NOT_FIRST_VIEW) {
            Not_First_View(where);
        } else if (flag == FLAG_WHOSE_STORY_MY_STORY) {    //浏览自己已上传的文章
            story = App.Public_My_Article_StoryList.get(position);
            user = User.getInstance();
        } else {
            story = App.Public_StoryList.get(position);
            user = story.getUser();
        }
        initSweetAlertDialog();
        initView();
        fillInformation();
        initContextMenu();
        initReceiver();
        //NWO_2.GetCommentList(this, story.getId());
        App.comment_story = story;

    }

    private void initSweetAlertDialog() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("获取中");
        pDialog.setCancelable(false);
        Handler handler = new UploadingHandler(this, pDialog);
    }


    private void Not_First_View(int where) {
        if (where == FLAG_WHERE_PUBLIC) {
            user = App.Public_Story_User.get(position);

        } else if (where == FLAG_WHERE_CARE_OTHER) {
            user = App.Public_Care_Other.get(position);

        } else if (where == FLAG_WHERE_CARE_ME) {
            user = App.Public_Care_Me.get(position);

        }
        story = App.Public_HomePage_StoryList.get(position_child);
    }

    // img_head的跳转事件
    public void startOtherActivity(View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptions options =
//                    ActivityOptions.makeSceneTransitionAnimation(this, img_head, img_head.getTransitionName());
//            startActivity(new Intent(this, HomePage.class), options.toBundle());
//        } else {
//            startActivity(new Intent(this, HomePage.class));
//        }
        startActivity(new Intent(this, HomePage.class));
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
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30)).getBitmap();
        transmit.setBitmap(bitmap);
        transmit.setBgResource(R.drawable.white);
        transmit.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        transmit.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject praise = new MenuObject("赞");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_favour_choose);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30)).getBitmap();
        praise.setBitmap(bitmap);
        praise.setBgResource(R.drawable.white);
        praise.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        praise.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject collect = new MenuObject("收藏");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_collect_choose);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30)).getBitmap();
        collect.setBitmap(bitmap);
        collect.setBgResource(R.drawable.white);
        collect.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        collect.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject comment = new MenuObject("评论");
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.img_comment);
        bitmap = Tool.resize_bitmap(bitmap, DestinyUtil.dip2px(this, 30), DestinyUtil.dip2px(this, 30)).getBitmap();
        comment.setBitmap(bitmap);
        comment.setBgResource(R.drawable.white);
        comment.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        comment.setMenuTextAppearanceStyle(R.style.TextViewStyle);

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


    private void initView() {
        img_head = (ImageView) findViewById(R.id.user_info_item_head);
        tv_name = (TextView) findViewById(R.id.user_info_item_name);
        tv_sign = (TextView) findViewById(R.id.user_info_item_sign);
        tv_praise_num = (TextView) findViewById(R.id.tv_praise);
        tv_flag = (TextView) findViewById(R.id.tv_flag);
        tv_content = (EditText) findViewById(R.id.tv_content);

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
            if (App.Switch_state_mode) {
                tv_favour.setTextColor(getResources().getColor(R.color.gray_light));
            } else {
                tv_favour.setTextColor(getResources().getColor(R.color.dark_normal));
            }
        }

        if (collect_state) {
            img_collect.setImageResource(R.drawable.img_collect_choose);
            tv_collect.setTextColor(getResources().getColor(R.color.green));
        } else {
            img_collect.setImageResource(R.drawable.img_collect);
            if (App.Switch_state_mode) {
                tv_collect.setTextColor(getResources().getColor(R.color.gray_light));
            } else {
                tv_collect.setTextColor(getResources().getColor(R.color.dark_normal));
            }
        }


        //设置可滑动
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void fillInformation() {
//        //加载头像
//        final String url = user.getAvatar();
//        NetWorkOperator.Set_Avatar(url, img_head);
        img_head.setImageBitmap(user.getBitmap());

        tv_name.setText(user.getUserName());
        tv_sign.setText(user.getSign());
        tv_praise_num.setText(story.getLikeCount() + "");
        tv_flag.setText(story.getFlags());
//        tv_content.setText(story.getContent());
        final String content = story.getContent();
        final String[] sa = content.split("<img>");
        final Map<String, Bitmap> bmMap = Collections.synchronizedMap(new HashMap<String, Bitmap>());
        String temp = "";
        //先将图片以外的文字显示出来
        for (int i = 0; i < sa.length; i++) {
            if (i % 2 == 0) {
                temp += sa[i];
            }
        }
        tv_content.setText(temp);
        pDialog.show();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        tv_content.setText("");
                        Editable edit_text = tv_content.getEditableText();
                        if (!content.equals("")) {
                            System.out.println("Display== " + Arrays.toString(sa));
                            for (int i = 0; i < sa.length; i++) {
                                System.out.println(sa[i]);
                                pDialog.dismiss();
                                if (i % 2 == 0) {
                                    edit_text.append(sa[i]);
                                } else {
                                    Bitmap bm = bmMap.get(i + "");
                                    float multiple = width / (float) bm.getWidth();
                                    Bitmap_file_dir bfd = App.Public_Picture_Cache.get(sa[i]);
                                    if (bfd == null) {
                                        bfd = Tool.resize_bitmap(bm, width - 80, multiple * bm.getHeight() - 80);
                                        App.Public_Picture_Cache.put(sa[i], bfd);
                                        System.out.println("Get new bfd : " + i);
                                    } else {
                                        System.out.println("bfd already exist : " + i);
                                    }
                                    System.out.println("Begin insert!!!");
                                    Tool.insertPic(bfd, sa[i], StoryView.this, tv_content);
                                    System.out.println("End insert!!!");
                                }

                            }
                        }
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap_file_dir bfd;
                for (int i = 0; i < sa.length; i++) {
                    if (i % 2 != 0) {
                        bfd = App.Public_Picture_Cache.get(sa[i]);
                        if (bfd == null) {
                            bmMap.put(i + "", NetWorkOperator.returnBitmap(sa[i]));
                            System.out.println("Woc, 又给我下图片 : " + i);
                        } else {
                            bmMap.put(i + "", bfd.getBitmap());
                            System.out.println("这图我看过23333 : " + i);
                        }
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
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
//                    startOtherActivity(img_head);
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
                overridePendingTransition(App.enterAnim, App.exitAnim);
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
                overridePendingTransition(App.enterAnim, App.exitAnim);
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
            if (App.Switch_state_mode) {
                tv_collect.setTextColor(getResources().getColor(R.color.gray_light));
            } else {
                tv_collect.setTextColor(getResources().getColor(R.color.dark_normal));
            }
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
            if (App.Switch_state_mode) {
                tv_favour.setTextColor(getResources().getColor(R.color.gray_light));
            } else {
                tv_favour.setTextColor(getResources().getColor(R.color.dark_normal));
            }
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
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }

    private class StoryOperatorReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "click praise":
                    System.out.println("get praise broadcast");
                    System.out.println(story.getLikeCount());
                    System.out.println(story.toString());
                    //System.out.println(user.toString());
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
