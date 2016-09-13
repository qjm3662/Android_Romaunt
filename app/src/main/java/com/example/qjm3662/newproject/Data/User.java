package com.example.qjm3662.newproject.Data;

import android.graphics.Bitmap;

import java.util.List;

/**
 *
 * Created by qjm3662 on 2016/5/17 0017.
 */
public class User extends UserBase{

    private UserStory UserStory;
    private List<User> follower;
    private List<User> following;
    private int collectedStoriesCount;      //收藏的故事数目
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public static void setInstance(User instance) {
        User.instance = instance;
    }

    @Override
    public String toString() {
        return super.toString() + "User{" +
                "UserStory=" + UserStory +
                ", follower=" + follower +
                ", following=" + following +
                ", collectedStoriesCount=" + collectedStoriesCount +
                '}';
    }

    public void setUserStory(com.example.qjm3662.newproject.Data.UserStory userStory) {
        UserStory = userStory;
    }

    public com.example.qjm3662.newproject.Data.UserStory getUserStory() {
        return UserStory;
    }

    public List<User> getFollower() {
        return follower;
    }

    public void setFollower(List<User> follower) {
        this.follower = follower;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public int getCollectedStoriesCount() {
        return collectedStoriesCount;
    }

    public void setCollectedStoriesCount(int collectedStoriesCount) {
        this.collectedStoriesCount = collectedStoriesCount;
    }


    // {"id":1,
    // "mobile":"18340861710",
    // "avatar":"www.baidu.com",
    // "sign":"test",
    // "userName":"changed",
    // "token":"VPpBrnWtvWDbVMGXgCvYPBhnkWM=",
    // "sex":1,"
    // noticeEnable":1,
    // "followingEnable":1,
    // "followerEnable":1,
    // "aboutNotice":1,
    // "updateNotice":1,


    public static final String USER_ID = "userID";
    public static final String USER_TOKEN = "token";
    public static final String USER_MOBILE = "mobile";
    public static final String USER_AVATAR = "avatar";
    public static final String USER_SIGN = "sign";
    public static final String USER_SEX = "sex";

    public static final String USER_USER_NAME = "userName";
    public static final String USER_LOGIN_TOKEN = "LoginToken";
    public static final String USER_NOTICE_ABLE = "noticeEnable";
    public static final String USER_FOLLOWINGENABLE = "followingEnable";
    public static final String USER_FOLLOWERENABLE = "followerEnable";
    public static final String USER_COLLECTED_STORIES_COUNT = "collectedStoriesCount";


    private static User instance = null;

    public static User getInstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }
    public static void deleteUser(){
        instance = null;
        getInstance();
    }
}
