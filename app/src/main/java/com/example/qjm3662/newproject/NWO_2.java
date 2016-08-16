package com.example.qjm3662.newproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;

import com.example.qjm3662.newproject.Data.CommentItem;
import com.example.qjm3662.newproject.Data.CommentItem_add;
import com.example.qjm3662.newproject.Data.Final_Static_data;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.Finding.Comment.CommentActivity;
import com.example.qjm3662.newproject.Finding.StoryView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by qjm3662 on 2016/7/6 0006.
 */
public class NWO_2 {


    /**
     * 获得评论列表
     *
     * @param context
     * @param storyID
     */
    public static void GetCommentList(final Context context, final String storyID, final Handler handler) {
        System.out.println("storyID:" + storyID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.GET_COMMENT_LIST + storyID)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("storyID", storyID)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                System.out.println("ERROR_GetCommentList : " + e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                Gson gson = new Gson();
                                JSONObject rev;
                                JSONArray rev_array;
                                CommentItem_add commentItem_add;
                                CommentItem commentItem;
                                App.Public_Comment_List.clear();
                                // List<CommentItem> list_rev = new ArrayList<CommentItem>();
                                try {
                                    JSONObject j_response = new JSONObject(response);
                                    JSONArray j_msg = j_response.getJSONObject("msg").getJSONArray("comment");
                                    for (int i = 0; i < j_msg.length(); i++) {
                                        commentItem_add = gson.fromJson(j_msg.get(i).toString(), CommentItem_add.class);
                                        System.out.println(commentItem_add.getId());
                                        //获得回复信息
                                        rev = new JSONObject(j_msg.get(i).toString());
                                        rev_array = rev.getJSONArray("RevComment");
                                        for (int j = 0; j < rev_array.length(); j++) {
                                            commentItem = gson.fromJson(rev_array.get(j).toString(), CommentItem.class);
                                            commentItem_add.getRevComment().add(commentItem);
                                        }
                                        System.out.println("rev_size : " + commentItem_add.getRevComment().size());
                                        if (commentItem_add.getRevComment().size() != 0) {
                                            System.out.println(commentItem_add.getRevComment().get(0).toString());
                                        }

                                        App.Public_Comment_List.add(commentItem_add);
                                    }

                                    for (int i = 0; i < App.Public_Comment_List.size(); i++) {
                                        GetUserById(context, App.Public_Comment_List.get(i).getUserId(), App.Public_Comment_List.get(i), handler, 1);
                                        for (int j = 0; j < App.Public_Comment_List.get(i).getRevComment().size(); j++) {
                                            GetUserById(context, App.Public_Comment_List.get(i).getRevComment().get(j).getUserId(), App.Public_Comment_List.get(i).getRevComment().get(j), handler, 0);
                                        }
                                    }
                                    System.out.println("finally flag" );
                                    CommentActivity.mPullToRefreshView.setRefreshing(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();
    }


    /**
     * 获取评论用户信息
     *
     * @param context
     * @param commentItem
     * @param handler
     */
    public static void GetUserById(Context context, final String AuthorID, final CommentItem commentItem, final Handler handler, final int flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_GET_USER_INFO)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("AuthorID", AuthorID)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                System.out.println("getCommentUserInfoFail : " + e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                UserBase userBase;
                                try {
                                    JSONObject j_response = new JSONObject(response);
                                    userBase = gson.fromJson(j_response.getJSONObject("msg").getString("user"), UserBase.class);
                                    commentItem.setUserBase(userBase);
                                    System.out.println("boo");
//                                    if (flag == 1) {
//                                        App.Public_Comment_List.add((CommentItem_add) commentItem);
//                                    }
                                    handler.sendEmptyMessage(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        }).start();
    }

    /**
     * 发表评论
     *
     * @param context
     * @param storyID
     */
    public static void GiveComment(final Context context, final String storyID, final String content, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.GIVE_COMMENT + storyID)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("commentContent", content)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                System.out.println("error:" + e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                System.out.println("response" + response);
                                //NWO_2.GetCommentList(context, App.comment_story.getId(), handler);
                            }
                        });
            }
        }).start();
    }

    /**
     * 点赞某人
     *
     * @param context
     * @param storyID
     * @param isLike
     */
    public static void PraiseSB(final Context context, final String storyID, final int isLike) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_CLICK_A_LIKE)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("storyID", storyID)
                        .addParams("isLike", String.valueOf(isLike))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                Intent intent = new Intent();
                                intent.setAction(StoryView.ACTION_PRAISE);
                                context.sendBroadcast(intent);//传递过去
                            }
                        });
            }
        }).start();
    }


    /**
     * 上传头像
     *
     * @param context
     * @param file
     * @param fileName
     * @return
     */
    public static String UpLoadHeader(final Context context, File file, String fileName) {
        OkHttpUtils
                .post()//
                .url(Final_Static_data.UP_FILE)
                .addFile("mFile", fileName, file)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("UpLoadHeader fail : " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        User.getInstance().setAvatar(response);
                        NetWorkOperator.UpDateUserInfo(context);
                    }
                });
        System.out.println(file.exists());
        return file.getPath();
    }


    public static String UpLoadFile(final Context context, File file, String fileName) {
        OkHttpUtils
                .post()//
                .url(Final_Static_data.UP_FILE)
                .addFile("mFile", fileName, file)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("UpLoadFile fail : " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
        System.out.println(file.exists());
        return file.getPath();
    }


    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = getSDPath() + "/header/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
            System.out.println("maki");
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        System.out.println("name : " + myCaptureFile.getName());
        System.out.println("path ： " + myCaptureFile.getPath());
        System.out.println(myCaptureFile.isFile());
        return myCaptureFile;
    }


    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
