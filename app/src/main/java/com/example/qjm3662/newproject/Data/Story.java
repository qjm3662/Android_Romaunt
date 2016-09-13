package com.example.qjm3662.newproject.Data;


/**
 * 故事类
 * Created by qjm3662 on 2016/5/29 0029.
 */
public class Story extends StoryBean{
    int local_id;
    boolean isOwn;              //是否是自己的故事

    @Override
    public String toString() {
        return super.toString() + "Story{" +
                "local_id=" + local_id +
                ", isOwn=" + isOwn +
                '}';
    }

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public boolean isOwn() {
        return isOwn;
    }

    public void setOwn(boolean own) {
        isOwn = own;
    }

}
