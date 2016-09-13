package com.example.qjm3662.newproject.myself.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NWO_2;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Tool.Tool;
import com.example.qjm3662.newproject.myself.Article.ArticleActivity;
import com.example.qjm3662.newproject.myself.Care.care;
import com.example.qjm3662.newproject.myself.Edit_sign;
import com.example.qjm3662.newproject.myself.FeedBack;
import com.example.qjm3662.newproject.myself.collection.collection;
import com.example.qjm3662.newproject.myself.settings.my_settings;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qjm3662 on 2016/7/1 0001.
 */
public class Myself extends Fragment implements View.OnClickListener {
    private RoundedImageView img_avatar;
    private TextView tv_name;
    private ImageView img_sex;
    private TextView tv_sign;
    private TextView tv_fan_num;
    private TextView tv_concern_num;
    private TextView tv_article_num;
    private TextView tv_care_other;
    private TextView tv_care_me;
    private TextView tv_collect;
    private TextView tv_feedback;
    private ViewGroup vg_night_mode;
    private TextView tv_settings;
    private Button switch_button;
    private ViewGroup ll_my_article_num;

    private Context context;
    //用户信息获取成功广播接收器
    private User_info_receiver get_info_receiver;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.myself, container, false);
        context = getContext();

        get_info_receiver = new User_info_receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("GET_INFO"); //为BroadcastReceiver指定action，即要监听的消息名字。
        intentFilter.addAction("UP_ARTICLE_NUM");
        context.registerReceiver(get_info_receiver, intentFilter); //注册监听


        img_avatar = (RoundedImageView) view.findViewById(R.id.img_avatar);
        tv_name = (TextView) view.findViewById(R.id.my_name);
        img_sex = (ImageView) view.findViewById(R.id.img_sex);
        tv_sign = (TextView) view.findViewById(R.id.my_sign);
        tv_fan_num = (TextView) view.findViewById(R.id.my_fan_num);
        tv_concern_num = (TextView) view.findViewById(R.id.my_concern_num);
        tv_article_num = (TextView) view.findViewById(R.id.my_article_num);
        tv_care_other = (TextView) view.findViewById(R.id.my_care_other);
        tv_care_me = (TextView) view.findViewById(R.id.my_care_me);
        tv_collect = (TextView) view.findViewById(R.id.my_collection);
        tv_feedback = (TextView) view.findViewById(R.id.my_feedback);
        vg_night_mode = (ViewGroup) view.findViewById(R.id.ll_night_mode);
        tv_settings = (TextView) view.findViewById(R.id.my_settings);
        switch_button = (Button) view.findViewById(R.id.my_switch_button);
        ll_my_article_num = (ViewGroup) view.findViewById(R.id.ll_my_article_num);


        if (Tool.JudgeIsLogin(getContext())) {
            tv_name.setText(User.getInstance().getUserName());
            if (!User.getInstance().getSign().equals("")) {
                tv_sign.setText(User.getInstance().getSign());
            }
            //加载头像
            final String url = User.getInstance().getAvatar();
            NetWorkOperator.Set_Avatar(url, img_avatar);
            tv_concern_num.setText(App.Public_Care_Other.size() + "");
            tv_fan_num.setText(App.Public_Care_Me.size() + "");
            tv_article_num.setText(User.getInstance().getCollectedStoriesCount() + "");
            if (User.getInstance().getSex() == 1) {
                img_sex.setImageResource(R.drawable.img_male_mine);
            } else {
                img_sex.setImageResource(R.drawable.img_female_mine);
            }
            if (App.Switch_state_mode) {
                switch_button.setBackgroundResource(R.drawable.img_switch_choose);
            } else {
                switch_button.setBackgroundResource(R.drawable.img_switch);
            }
            if (App.Public_My_Article_StoryList.isEmpty()) {
                System.out.println("null");
            } else {
                tv_article_num.setText(App.Public_My_Article_StoryList.size()+"");
            }
        }

        tv_settings.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_sign.setOnClickListener(this);
        tv_care_other.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        img_sex.setOnClickListener(this);
        switch_button.setOnClickListener(this);
        tv_feedback.setOnClickListener(this);
        tv_concern_num.setOnClickListener(this);
        tv_article_num.setOnClickListener(this);
        tv_fan_num.setOnClickListener(this);
        tv_care_me.setOnClickListener(this);
        vg_night_mode.setOnClickListener(this);
        img_avatar.setOnClickListener(this);
        ll_my_article_num.setOnClickListener(this);

        NetWorkOperator.getUserInfo(getContext(), String.valueOf(User.getInstance().getId()));
        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                startPhotoZoom(Uri.fromFile(tempFile));
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null) {
                    System.out.println("11================");
                    startPhotoZoom(data.getData());
                } else {
                    System.out.println("================");
                }
                break;
            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null) {
                    //获取文件的绝对路径
                    Bitmap bm = data.getParcelableExtra("data");
                    if (bm != null) {
                        System.out.println("bm is valued");
                        System.out.println(data);
                        //img.setImageBitmap(bm);
                    }
                    try {
                        NWO_2.UpLoadHeader(getContext(), NWO_2.saveFile(bm, "dog.jpg"), "dog.jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 使用系统当前日期加以调整作为照片的名称

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private void getCamera() {
        Intent cameraintent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(tempFile));
        startActivityForResult(cameraintent,
                PHOTO_REQUEST_TAKEPHOTO);
    }

    //从相册获取图片并裁剪
    private void getAlbum() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 调用系统裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        System.out.println("22================");
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(get_info_receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_settings:
                Intent intent = new Intent(getActivity(), my_settings.class);
                ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                startActivity(intent);
                break;
            case R.id.img_avatar:
                //从相册中获取图片并裁剪
                getAlbum();
                break;
            case R.id.my_fan_num:
                break;
            case R.id.my_concern_num:
                //NetWorkOperator.Concern_sb(context, "14");
                break;
            case R.id.ll_my_article_num:
                startActivity(new Intent(context, ArticleActivity.class));
                break;
            case R.id.my_care_me:
                Intent intent_care_me = new Intent(context, care.class);
                intent_care_me.addFlags(2);
                startActivity(intent_care_me);
                ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.my_care_other:
                Intent intent_care_other = new Intent(context, care.class);
                intent_care_other.addFlags(1);
                startActivity(intent_care_other);
                ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.my_collection:
                startActivity(new Intent(context, collection.class));
                ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.my_feedback:
                startActivity(new Intent(context, FeedBack.class));
                ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                break;
            case R.id.my_name:
                if (Tool.JudgeIsLogin(context)) {
                    Tool.ShowDialog(getContext(), User.getInstance().getUserName(), "编辑用户名");
                    ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                }
                break;
            case R.id.my_sign:
                if (Tool.JudgeIsLogin(context)) {
                    Intent intent1 = new Intent(getContext(), Edit_sign.class);
                    startActivity(intent1);
                    ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                }
                break;
            case R.id.img_sex:
                if (Tool.JudgeIsLogin(context)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //    指定下拉列表的显示数据
                    final String[] cities = {"男", "女"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User.getInstance().setSex(which + 1);
                            NetWorkOperator.UpDateUserInfo(getContext());
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                    ((Activity) context).overridePendingTransition(App.enterAnim, App.exitAnim);
                }
                break;
            case R.id.my_switch_button:
                changeTheme();
                App.updateSwitchInfo(getContext());
                break;
        }
    }


    private void changeTheme(){

        System.out.println("STATE1 : " + App.Switch_state_mode);
        if(App.Switch_state_mode){
            switch_button.setBackgroundResource(R.drawable.img_switch);
            context.setTheme(R.style.AppTheme_day);
            App.Switch_state_mode = false;
        }else{
            context.setTheme(R.style.AppTheme_night);
            switch_button.setBackgroundResource(R.drawable.img_switch_choose);
            App.Switch_state_mode = true;
        }
        System.out.println("STATE2 : " + App.Switch_state_mode);

        Intent intent = new Intent();
        intent.setAction("CHANGE_MODE");
        getActivity().sendBroadcast(intent);
        getActivity().finish();
        Intent intent1 = new Intent(getActivity(), getActivity().getClass());
        intent1.putExtra("FLAG", true);
        startActivity(intent1);
        App.isChangingMode = true;
        getActivity().overridePendingTransition(App.enterAnim, App.exitAnim);

    }


    private class User_info_receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "GET_INFO":
                    tv_name.setText(User.getInstance().getUserName());
                    tv_sign.setText(User.getInstance().getSign());
                    //加载头像
                    final String url = User.getInstance().getAvatar();
                    NetWorkOperator.Set_Avatar(url, img_avatar);
                    tv_concern_num.setText(App.Public_Care_Other.size() + "");
                    tv_fan_num.setText(App.Public_Care_Me.size() + "");
                    tv_article_num.setText(User.getInstance().getCollectedStoriesCount() + "");
                    if (User.getInstance().getSex() == 1) {
                        img_sex.setImageResource(R.drawable.img_male_mine);
                    } else {
                        img_sex.setImageResource(R.drawable.img_female_mine);
                    }
                    if (!App.Public_My_Article_StoryList.isEmpty()) {
                        tv_article_num.setText(App.Public_My_Article_StoryList.size()+"");
                    }
                    break;
                case "UP_ARTICLE_NUM":
                    if(!App.Public_My_Article_StoryList.isEmpty()){
                        tv_article_num.setText(App.Public_My_Article_StoryList.size()+"");
                    }else{
                        System.out.println("Public_My_Article_StoryList is null");
                    }
                    break;
            }
        }
    }
}
