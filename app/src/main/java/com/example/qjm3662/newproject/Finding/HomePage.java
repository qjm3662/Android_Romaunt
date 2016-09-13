package com.example.qjm3662.newproject.Finding;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.ChangeModeBroadCastReceiver;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Tool.GuiUtils;
import com.yalantis.phoenix.PullToRefreshView;

public class HomePage extends ListActivity implements View.OnClickListener {

    public static HomePage_story_adapter adapter;
    private Intent intent;
    private User userBase;
    private int position;
    private int flag;       //用来标记来源

    private ImageView img_bar_left;
    private ImageView img_bar_right;
    private TextView tv_bar_center;
    private TextView tv_bar_right;

    private ImageView img_head;
    private ImageView img_sex;
    private TextView tv_nickname;
    private TextView tv_sign;
    private TextView hps_fan_num;
    private TextView hps_concern_num;
    public static TextView hps_article_num;
    private ViewGroup l_private_letter;
    private ViewGroup l_concern;
    public static PullToRefreshView mPullToRefreshView;
    private long REFRESH_DELAY = 1000;
    private Context context;
    private ChangeModeBroadCastReceiver receiver;
    private LinearLayout mRlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("mRlContainer is null : ", (mRlContainer == null) + "");

        if (App.Switch_state_mode) {
            this.setTheme(R.style.AppTheme_night);
        } else {
            this.setTheme(R.style.AppTheme_day);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("CHANGE_MODE");
        receiver = new ChangeModeBroadCastReceiver(this);
        registerReceiver(receiver, intentFilter);

        setContentView(R.layout.activity_home_page);
        mRlContainer = (LinearLayout) findViewById(R.id.container___);

        initView();
        adapter = new HomePage_story_adapter(this);
        setListAdapter(adapter);
        context = this;

        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        flag = intent.getIntExtra("FLAG_WHERE", 0);
        if (flag == 0) {  //自故事页面跳转
            userBase = App.Public_Story_User.get(position);
        } else if (flag == 1) {          //自关注页面跳转
            userBase = App.Public_Care_Other.get(position);
        } else if (flag == 2) {          //自被关注页面跳转
            userBase = App.Public_Care_Me.get(position);
        }
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetWorkOperator.Get_Person_Story_List(context, String.valueOf(userBase.getId()), 1, 0);
            }
        });

        NetWorkOperator.Get_Person_Story_List(context, String.valueOf(userBase.getId()), 0, 0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setupEnterAnimation(); // 入场动画
//            setupExitAnimation(); // 退场动画
//        } else {
//            fillInformation();
//        }
        fillInformation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    // 入场动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override public void onTransitionStart(Transition transition) {

            }

            @Override public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override public void onTransitionCancel(Transition transition) {

            }

            @Override public void onTransitionPause(Transition transition) {

            }

            @Override public void onTransitionResume(Transition transition) {

            }
        });
    }

    // 动画展示
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        Log.e("mRlContainer is null : ", (mRlContainer == null) + "");
        GuiUtils.animateRevealShow(
                this, mRlContainer,
                img_head.getWidth() / 2, R.color.colorAccent,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override public void onRevealHide() {

                    }

                    @Override public void onRevealShow() {
//                        initViews();
                        fillInformation();
                    }
                });
    }

    // 退出动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(300);
    }

    // 退出按钮
    public void backActivity(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            onBackPressed();
        } else {
            defaultBackPressed();
        }
    }

    // 退出事件
    @Override public void onBackPressed() {
//        GuiUtils.animateRevealHide(
//                this, mRlContainer,
//                img_head.getWidth() / 2, R.color.green,
//                new GuiUtils.OnRevealAnimationListener() {
//                    @Override
//                    public void onRevealHide() {
//                        defaultBackPressed();
//                    }
//
//                    @Override
//                    public void onRevealShow() {
//
//                    }
//                });


        defaultBackPressed();
    }

    private void fillInformation() {

//        new Handler(Looper.getMainLooper()).post(() -> {
//            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
//            animation.setDuration(300);
//            mTVContainer.startAnimation(animation);
//            mIvClose.setAnimation(animation);
//            mTVContainer.setVisibility(View.VISIBLE);
//            mIvClose.setVisibility(View.VISIBLE);
//        });
        //加载头像
//        final String url = userBase.getAvatar();
//        NetWorkOperator.Set_Avatar(url, img_head);
        if(userBase.getBitmap() != null){
            img_head.setImageBitmap(userBase.getBitmap());
        }

        tv_nickname.setText(userBase.getUserName());
        tv_sign.setText(userBase.getSign());
        tv_bar_center.setText(userBase.getUserName() + "的主页");
        if (userBase.getSex() == 1) {
            img_sex.setImageResource(R.drawable.img_male_mine);
        } else {
            img_sex.setImageResource(R.drawable.img_female_mine);
        }

//        hps_fan_num.setText(userBase.getFollower().size() + "");
//        hps_concern_num.setText(userBase.getFollowing().size() + "");
        hps_article_num.setText(App.Public_HomePage_StoryList.size() + "");
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cloud_imageView_story:
                onBackPressed();
                break;
            case R.id.hps_head:
                break;
            case R.id.hps_sex:
                break;
            case R.id.hps_nickname:
                break;
            case R.id.hps_sign:
                break;
            case R.id.l_private_letter:
                break;
            case R.id.l_concern:
                NetWorkOperator.Concern_sb(this, String.valueOf(userBase.getId()));
                break;
        }
    }

    public void defaultBackPressed() {
        super.onBackPressed();
        App.Public_HomePage_StoryList.clear();
        finish();
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }

    private void initView() {
        initBar();
        img_head = (ImageView) findViewById(R.id.hps_head);
        img_sex = (ImageView) findViewById(R.id.hps_sex);
        tv_nickname = (TextView) findViewById(R.id.hps_nickname);
        tv_sign = (TextView) findViewById(R.id.hps_sign);
        hps_fan_num = (TextView) findViewById(R.id.hps_fan_num);
        hps_concern_num = (TextView) findViewById(R.id.hps_concern_num);
        hps_article_num = (TextView) findViewById(R.id.hps_article_num);
        l_private_letter = (ViewGroup) findViewById(R.id.l_private_letter);
        l_concern = (ViewGroup) findViewById(R.id.l_concern);

        img_head.setOnClickListener(this);
        img_sex.setOnClickListener(this);
        tv_nickname.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        hps_fan_num.setOnClickListener(this);
        hps_concern_num.setOnClickListener(this);
        hps_article_num.setOnClickListener(this);
        l_private_letter.setOnClickListener(this);
        l_concern.setOnClickListener(this);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int p, long id) {
        super.onListItemClick(l, v, p, id);
        Intent intent = new Intent(this, StoryView.class);
        intent.putExtra("position", position);
        intent.putExtra("position_child", p);
        intent.putExtra("flag", 1);
        intent.putExtra("FLAG_WHERE", flag);
        startActivity(intent);
        overridePendingTransition(App.enterAnim, App.exitAnim);
    }
}
