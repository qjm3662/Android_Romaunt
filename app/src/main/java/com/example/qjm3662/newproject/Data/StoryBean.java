package com.example.qjm3662.newproject.Data;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by qjm3662 on 2016/6/9 0009.
 */
public class StoryBean {
    private String id;
    private String title;
    private String flags;               //故事的标签
    private String content;             //故事的内容
    private int publicEnable;       //是否公开
    private String createdAt;         //创建时间
    private String updatedAt;         //上传（更新）时间
    private int AuthorID;               //作者的Id
    private int likeCount;              //点赞总数
    private List<User> Users;
    private User user;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    @Override
    public String toString() {
        return "StoryBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", flags='" + flags + '\'' +
                ", content='" + content + '\'' +
                ", publicEnable=" + publicEnable +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", AuthorID=" + AuthorID +
                ", likeCount=" + likeCount +
                ", Users=" + Users +
                ", user=" + user +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublicEnable(int publicEnable) {
        this.publicEnable = publicEnable;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAuthorID(int authorID) {
        AuthorID = authorID;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getFlags() {
        return flags;
    }

    public String getContent() {
        return content;
    }

    public int getPublicEnable() {
        return publicEnable;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getAuthorID() {
        return AuthorID;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public List<User> getUsers() {
        return Users;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StoryBean) {
            StoryBean storyBean = (StoryBean) obj;
//            Log.e("StoryBean-->equals",(this.id).equals(storyBean.getId())+"");
            return this.id.equals(storyBean.getId());
        }else{
//            System.out.println("mdzz");
            return false;
        }
    }
}
