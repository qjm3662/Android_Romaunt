package com.example.qjm3662.newproject.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qjm3662 on 2016/5/29 0029.
 */
public class StoryDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME_STORY = "story";
    public static final String TABLE_NAME_STORY_COLLECTIONS = "story2";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_FLAGS = "flags";                  //故事的标签
    public static final String COLUMN_NAME_CONTENT = "content";              //故事的内容
    public static final String COLUMN_NAME_PUBLIC_ENABLE = "publicEnable";   //是否公开
    public static final String COLUMN_NAME_CREATE_AT = "createAt";           //创建时间
    public static final String COLUMN_NAME_UPDATE_AT = "updateAt";           //更新时间
    public static final String COLUMN_NAME_AUTHOR_ID = "AuthorID";           //作者的Id
    public static final String COLUMN_NAME_LIKE_COUNT = "likeCount";          //点赞总数
    public static final String COLUMN_NAME_ISO_WN = "isOwn";                  //是否是自己的故事

    public static final String TABLE_NAME_USER = "user";
    public static final String COLUMN_NAME_MOBILE = "mobile";
    public static final String COLUMN_NAME_AVATAR = "avatar";
    public static final String COLUMN_NAME_SIGN = "sign";
    public static final String COLUMN_NAME_USER_NAME = "userName";
    public static final String COLUMN_NAME_TOKEN = "token";
    public static final String COLUMN_NAME_LOGIN_TOKEN = "LoginToken";
    public static final String COLUMN_NAME_SEX = "sex";
    public static final String COLUMN_NAME_NOTICE_ENABLE = "noticeEnable";
    public static final String COLUMN_NAME_FOLLOWING_ENABLE = "followingEnable";
    public static final String COLUMN_NAME_FOLLOWER_ENABLE = "followerEnable";
    public static final String COLUMN_NAME_COLLECTED_STORIES_COUNT = "collectedStoriesCount";

    public static final int SQLiteVersion = 2;

    public StoryDB(Context context){
        super(context,"Story",null,SQLiteVersion);
    }
    public StoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_STORY+"("+
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_NAME_TITLE + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_FLAGS + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_CONTENT + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_PUBLIC_ENABLE + " BIT NOT NULL DEFAULT 0,"+
                COLUMN_NAME_CREATE_AT + " DATETIME NOT NULL DEFAULT \"\","+
                COLUMN_NAME_UPDATE_AT + " DATETIME NOT NULL DEFAULT \"\","+
                COLUMN_NAME_AUTHOR_ID + " INTEGER NOT NULL DEFAULT 0,"+
                COLUMN_NAME_LIKE_COUNT + " INTEGER NOT NULL DEFAULT 0,"+
                COLUMN_NAME_ISO_WN + " TEXT NOT NULL DEFAULT \"\""+
                ")");
        db.execSQL("CREATE TABLE " + TABLE_NAME_USER+"("+
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_NAME_MOBILE + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_AVATAR + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_SIGN + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_USER_NAME + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_TOKEN + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_SEX + " INTEGER NOT NULL DEFAULT 0,"+
                COLUMN_NAME_LOGIN_TOKEN + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_NOTICE_ENABLE + " BIT NOT NULL DEFAULT 0,"+
                COLUMN_NAME_FOLLOWING_ENABLE + " BIT NOT NULL DEFAULT 0,"+
                COLUMN_NAME_FOLLOWER_ENABLE + " BIT NOT NULL DEFAULT 0,"+
                COLUMN_NAME_COLLECTED_STORIES_COUNT + " INTEGER NOT NULL DEFAULT 0"+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_STORY_COLLECTIONS+"("+
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_NAME_TITLE + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_FLAGS + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_CONTENT + " TEXT NOT NULL DEFAULT \"\","+
                COLUMN_NAME_PUBLIC_ENABLE + " BIT NOT NULL DEFAULT 0,"+
                COLUMN_NAME_CREATE_AT + " DATETIME NOT NULL DEFAULT \"\","+
                COLUMN_NAME_UPDATE_AT + " DATETIME NOT NULL DEFAULT \"\","+
                COLUMN_NAME_AUTHOR_ID + " INTEGER NOT NULL DEFAULT 0,"+
                COLUMN_NAME_LIKE_COUNT + " INTEGER NOT NULL DEFAULT 0,"+
                COLUMN_NAME_ISO_WN + " TEXT NOT NULL DEFAULT \"\""+
                ")");
    }
}
