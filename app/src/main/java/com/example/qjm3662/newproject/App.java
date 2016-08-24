package com.example.qjm3662.newproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.qjm3662.newproject.Data.CommentItem_add;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.Data.StoryDB;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.Tool.Tool;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by qjm3662 on 2016/5/30 0030.
 */
public class App extends Application {

    //可读数据库
    public static SQLiteDatabase dbRead;
    public static StoryDB storyDB;
    public static SQLiteDatabase dbWrite;

    //存储本地故事对象列表
    public static List<Story> StoryList = new ArrayList<Story>();

    public static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");


    //广场故事列表
    public static List<StoryBean> Public_StoryList = Collections.synchronizedList(new ArrayList<StoryBean>());
    //与广场故事配套的，储存作者相关信息
    public static List<UserBase> Public_Story_User = Collections.synchronizedList(new ArrayList<UserBase>());

    //关注我的人列表
    public static List<UserBase> Public_Care_Me = Collections.synchronizedList(new ArrayList<UserBase>());
    //我关注的人列表
    public static List<UserBase> Public_Care_Other = Collections.synchronizedList(new ArrayList<UserBase>());

    //收藏故事列表
    public static List<StoryBean> Public_Collected_StoryList = Collections.synchronizedList(new ArrayList<StoryBean>());

    //个人主页的文章列表
    public static List<StoryBean> Public_HomePage_StoryList = Collections.synchronizedList(new ArrayList<StoryBean>());

    //我的文章列表
    public static List<StoryBean> Public_My_Article_StoryList = Collections.synchronizedList(new ArrayList<StoryBean>());

    //用于记录当前评论的是哪个文章
    public static StoryBean comment_story = new StoryBean();

    //评论列表
    public static List<CommentItem_add> Public_Comment_List = Collections.synchronizedList(new ArrayList<CommentItem_add>());

    //暂时缓存以阅读文章的图片资源，App退出后自动删除
    public static Map<String, Bitmap> Public_Picture_Cache = Collections.synchronizedMap(new HashMap<String, Bitmap>());


    public static boolean Switch_state_mode = false;
    public static boolean Switch_state_wifi_sync = false;
    public static boolean Switch_state_is_public_fan = false;
    public static boolean Switch_state_is_public_like = false;
    public static boolean Switch_state_is_inform_praise = false;
    public static boolean Switch_state_is_inform_comment = false;
    public static boolean Switch_state_is_inform_collect = false;


    public static float width;
    public static String pro_cache_dir;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
//        //极光推送初始化
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);

        //获取数据库
        storyDB = new StoryDB(this);
        dbRead = storyDB.getReadableDatabase();
        dbWrite = storyDB.getWritableDatabase();

        //获取用户信息
        getUserInfo(this);

        App.format.setTimeZone(TimeZone.getTimeZone("GMT+08:00:00"));// 中国北京时间，东八区

        getStory_from_SQLite();
        getSwitchInfo(this);

        pro_cache_dir = String.valueOf(this.getExternalCacheDir());
    }


    public static void openDB() {
        if(dbWrite.isOpen()){
            dbWrite.close();
        }
        if(dbRead.isOpen()){
            dbRead.close();
        }
        dbRead = storyDB.getReadableDatabase();
        dbWrite = storyDB.getWritableDatabase();
    }

    public static void getUserInfo(Context context) {
        //用户详细信息
        SharedPreferences sp = context.getSharedPreferences("User", MODE_PRIVATE);
        //登陆返回信息
        SharedPreferences sp1 = context.getSharedPreferences("User_", MODE_PRIVATE);
        if (sp != null) {
            Tool.str_to_user(sp.getString("user_info", null), sp1.getString("user_info", null));
        }
    }

    public static void getSwitchInfo(Context context) {
        //用户详细信息
        SharedPreferences sp = context.getSharedPreferences("Set_info", MODE_PRIVATE);
        Switch_state_mode = sp.getBoolean("Switch_state_mode", false);
        Switch_state_wifi_sync = sp.getBoolean("Switch_state_wifi_sync", false);
        Switch_state_is_public_fan = sp.getBoolean("Switch_state_is_public_fan", false);
        Switch_state_is_public_like = sp.getBoolean("Switch_state_is_public_like", false);
        Switch_state_is_inform_praise = sp.getBoolean("Switch_state_is_inform_praise", false);
        Switch_state_is_inform_comment = sp.getBoolean("Switch_state_is_inform_comment", false);
        Switch_state_is_inform_collect = sp.getBoolean("Switch_state_is_inform_collect", false);
    }

    public static void updateSwitchInfo(Context context) {
        //将设置信息存储到SharePreferences中
        SharedPreferences sp = context.getSharedPreferences("Set_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("Switch_state_mode", Switch_state_mode);
        editor.putBoolean("Switch_state_wifi_sync", Switch_state_wifi_sync);
        editor.putBoolean("Switch_state_is_public_fan", Switch_state_is_public_fan);
        editor.putBoolean("Switch_state_is_public_like", Switch_state_is_public_like);
        editor.putBoolean("Switch_state_is_inform_praise", Switch_state_is_inform_praise);
        editor.putBoolean("Switch_state_is_inform_comment", Switch_state_is_inform_comment);
        editor.putBoolean("Switch_state_is_inform_collect", Switch_state_is_inform_collect);
        editor.apply();
    }


    private void getStory_from_SQLite() {
        Cursor c = dbRead.query(StoryDB.TABLE_NAME_STORY, null, null, null, null, null, null);
        while (c.moveToNext()) {
            Story story = new Story();
            story.setTitle(c.getString(c.getColumnIndex(StoryDB.COLUMN_NAME_TITLE)));
            story.setContent(c.getString(c.getColumnIndex(StoryDB.COLUMN_NAME_CONTENT)));
            story.setLocal_id(c.getInt(c.getColumnIndex(StoryDB.COLUMN_NAME_ID)));
            story.setCreatedAt(c.getString(c.getColumnIndex(StoryDB.COLUMN_NAME_CREATE_AT)));
            story.setFlags(c.getString(c.getColumnIndex(StoryDB.COLUMN_NAME_FLAGS)));
            story.setPublicEnable(c.getInt(c.getColumnIndex(StoryDB.COLUMN_NAME_PUBLIC_ENABLE)));
            System.out.println("Item Date : ======" + story.getCreatedAt());
            StoryList.add(story);
        }
    }


    public static void clearInformation() {
        //广场故事列表
        Public_StoryList.clear();
        //与广场故事配套的，储存作者相关信息
        Public_Story_User.clear();
        //关注我的人列表
        Public_Care_Me.clear();
        //我关注的人列表
        Public_Care_Other.clear();
        //收藏故事列表
        Public_Collected_StoryList.clear();
        //个人主页的文章列表
        Public_HomePage_StoryList.clear();
    }
}
