package com.example.qjm3662.newproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qjm3662.newproject.Data.Final_Static_data;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.Finding.Finding_fragment;
import com.example.qjm3662.newproject.Finding.HomePage;
import com.example.qjm3662.newproject.LoginAndRegister.LoginAndRegisterOperator;
import com.example.qjm3662.newproject.Main_UI.MainActivity;
import com.example.qjm3662.newproject.Tool.Tool;
import com.example.qjm3662.newproject.myself.Article.ArticleActivity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by qjm3662 on 2016/6/4 0004.
 */
public class NetWorkOperator {


    /**
     * 收藏故事
     *
     * @param context
     * @param storyId
     */
    public static void CollectStory(final Context context, final String storyId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_COLLECT)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("storyID", storyId)
                        .addParams("isCollect", "1")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                System.out.println(" Collect error : " + e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                getUserInfo(context, User.getInstance().getId() + "");
                            }
                        });
            }
        }).start();
    }

    /**
     * 获得个人故事列表
     *
     * @param id
     * @param flag  //flag = 1（表示是下滑刷新调用，需要刷新回执） flag = 2(表示adapter还未创建)
     * @param where //where = 0(别人主页的文章)，where = 1(自己主页的文章)
     */
    public static void Get_Person_Story_List(final Context context, final String id, final int flag, final int where) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_GET_USER_STORIES)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("userID", id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                System.out.println(e);
                            }

                            @Override
                            public void onResponse(String response) {
//                                System.out.println(response);
                                try {
                                    JSONObject j_ori = new JSONObject(response);
                                    JSONObject j_msg = j_ori.getJSONObject("msg");
                                    System.out.println(j_msg);

                                    final Gson gson = new Gson();
                                    final JSONArray jsonObject = j_msg.getJSONArray("stories");
                                    StoryBean storyBean;

                                    //如果下拉刷新，则清除链表数据
                                    App.Public_HomePage_StoryList.clear();
                                    App.Public_My_Article_StoryList.clear();
                                    for (int i = 0; i < jsonObject.length(); i++) {
                                        //用Gson解析器，将返回的Json数据转为Story对象
                                        storyBean = gson.fromJson(jsonObject.get(i).toString(), StoryBean.class);
                                        System.out.println(storyBean.getContent());
                                        if (where == 0) {
                                            App.Public_HomePage_StoryList.add(storyBean);
                                        } else {
                                            App.Public_My_Article_StoryList.add(storyBean);
                                        }
                                    }

                                    if (where == 0) {
                                        HomePage.adapter.notifyDataSetChanged();
                                        if (flag == 1) {
                                            HomePage.mPullToRefreshView.setRefreshing(false);
                                        }
                                    } else {
                                        if (flag != 2) {
                                            ArticleActivity.adapter.notifyDataSetChanged();
                                            ArticleActivity.mPullToRefreshView.setRefreshing(false);
                                        }
                                        Intent intent = new Intent();
                                        intent.setAction("UP_ARTICLE_NUM");
                                        context.sendBroadcast(intent);//传递过去
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();
    }


    /**
     * 关注某人
     *
     * @param context
     * @param id
     */
    public static void Concern_sb(final Context context, final String id) {
        System.out.println("lululu");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_CONCERN_SB)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("starID", id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                System.out.println("WTF");
                                System.out.println(response);
                                getUserInfo(context, String.valueOf(User.getInstance().getId()));
                            }
                        });
            }
        }).start();
    }


    /**
     * 更新用户信息
     *
     * @param context
     */
    public static void UpDateUserInfo(final Context context) {
        final User user = User.getInstance();
        String sign = user.getSign();
        if (sign.equals("")) {
            sign = "默认签名";
        }
        final String finalSign = sign;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_GET_USER_INFO_UPDATE)
                        .addHeader("LoginToken", user.getLoginToken())
                        .addParams("avatar", user.getAvatar())
                        .addParams("userName", user.getUserName())
                        .addParams("sign", finalSign)
                        .addParams("sex", String.valueOf(user.getSex()))
                        .addParams("updateNotice", String.valueOf(user.getUpdateNotice()))
                        .addParams("noticeEnable", String.valueOf(user.getNoticeEnable()))
                        .addParams("followingEnable", String.valueOf(user.getFollowingEnable()))
                        .addParams("followerEnable", String.valueOf(user.getFollowerEnable()))
                        .addParams("aboutNotice", String.valueOf(user.getAboutNotice()))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {

                                Log.e("更新用户信息回执:", response);

                                try {
                                    JSONObject j = new JSONObject(response);
                                    //如果登陆失效则重新获取Token,再次执行该函数
                                    if (j.getString("msg").equals("LoginToken")) {
                                        getNew_Token(User.getInstance().getToken());
                                        UpDateUserInfo(context);
                                    }

                                    if (j.getBoolean("status")) {

                                        Log.e("获取用户信息:", "begin");
                                        try {
                                            Log.e("USERINFO", response);
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONObject object = jsonObject.getJSONObject("msg");
                                            JSONObject user_info = object.getJSONObject("user");
                                            JSONArray js_array_follower = object.getJSONArray("follower");
                                            JSONArray js_array_following = object.getJSONArray("following");
                                            System.out.println(object);
                                            User user = User.getInstance();

                                            //将用户信息存储到SharePreferences中
                                            SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("user_info", jsonObject.getString("msg"));
                                            editor.apply();

                                            user.setAvatar(user_info.getString(User.USER_AVATAR));
                                            //user.setCollectedStoriesCount(user_info.getInt(User.USER_COLLECTED_STORIES_COUNT));
                                            user.setSign(user_info.getString(User.USER_SIGN));
                                            user.setUserName(user_info.getString(User.USER_USER_NAME));
                                            user.setSex(user_info.getInt(User.USER_SEX));


                                            //获取我关注的和关注我的；
                                            Gson gson = new Gson();
                                            List<UserBase> list_user_base = new ArrayList<UserBase>();
                                            UserBase userBase = null;
                                            App.Public_Care_Me.clear();
                                            if (js_array_follower.length() != 0) {
                                                if (!js_array_follower.get(0).toString().equals("false")) {
                                                    //同步关注我的人信息
                                                    for (int i = 0; i < js_array_follower.length(); i++) {
                                                        userBase = gson.fromJson(js_array_follower.get(i).toString(), UserBase.class);
                                                        System.out.println("有人关注我 ： " + userBase.getId());
                                                        list_user_base.add(userBase);
                                                        App.Public_Care_Me.add(userBase);
                                                    }
                                                    if (list_user_base.size() != 0) {
                                                        user.setFollower(list_user_base);
                                                    }
                                                }
                                            }

                                            list_user_base.clear();
                                            App.Public_Care_Other.clear();
                                            //同步我关注的人信息
                                            if (js_array_following.length() != 0) {
                                                if (!js_array_following.get(0).toString().equals("false")) {
                                                    for (int i = 0; i < js_array_following.length(); i++) {
                                                        userBase = gson.fromJson(js_array_following.get(i).toString(), UserBase.class);
                                                        System.out.println("我在关注TA ： " + userBase.getId());
                                                        list_user_base.add(userBase);
                                                        App.Public_Care_Other.add(userBase);
                                                        System.out.println(js_array_following);
                                                    }

                                                    if (list_user_base.size() != 0) {
                                                        user.setFollowing(list_user_base);
                                                    }
                                                }
                                            }

                                            list_user_base.clear();

                                            //同步收藏文章数目
                                            user.setCollectedStoriesCount(object.getInt("collectedStoriesCount"));

                                            //获取自己上传的文章
                                            Get_Person_Story_List(context, String.valueOf(user.getId()), 2, 1);

                                            Intent intent = new Intent();
                                            intent.setAction("GET_INFO");
                                            context.sendBroadcast(intent);//传递过去

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();
    }

    /**
     * 获得广场故事
     *
     * @param flag 0表示下拉刷新， 1表示上拉加载更多
     */
    public static void Get_finding_story(final int flag, final String timestamps) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_GET_PUBLIC_STORIES)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("timestamps", timestamps)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                //System.out.println("error");
                            }

                            @Override
                            public void onResponse(String response) {
                                //System.out.println("RESPONSE  : " + response);
                                try {
                                    JSONObject object = new JSONObject(response);

                                    //如果登陆失效则重新获取Token,再次执行该函数
                                    if (object.getString("msg").equals("LoginToken")) {
                                        getNew_Token(User.getInstance().getToken());
                                        Get_finding_story(flag, timestamps);
                                    }

                                    final Gson gson = new Gson();
                                    final JSONArray jsonObject = object.getJSONArray("msg");

                                    //如果下拉刷新，则清除链表数据
                                    if (flag == 0) {
                                        App.Public_StoryList.clear();
                                        App.Public_Story_User.clear();
                                    }
                                    final int length_before = App.Public_StoryList.size();
                                    Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what) {
                                                case 1:
                                                    for (int i = 0; i < jsonObject.length(); i++) {
                                                        StoryBean story = null;
                                                        System.out.println(App.Public_Story_User.size() + "  " + App.Public_StoryList.size());
                                                        try {
                                                            //用Gson解析器，将返回的Json数据转为Story对象
                                                            story = gson.fromJson(jsonObject.get(i).toString(), StoryBean.class);
                                                            //将故事对应的用户信息加到故事里去
                                                            story.setUser(App.Public_Story_User.get(i + length_before));
                                                            if (!App.Public_StoryList.contains(story)) {
                                                                App.Public_StoryList.add(story);
                                                                Log.e("Story", story.toString());
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                    //在所有数据获取完毕后，更新UI界面
                                                    Finding_fragment.adapter.notifyDataSetChanged();
                                                    if (flag == 0) {
                                                        Finding_fragment.swipeRefreshListView.setRefreshing(false);
                                                    } else {
                                                        Finding_fragment.swipeRefreshListView.setLoading(false);
                                                    }
                                                    break;
                                            }
                                        }
                                    };
                                    //通过故事中包含的作者的AuthorID获取作者信息
                                    GET_USER_INFORMATION_BY_ID(jsonObject, handler);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();
    }


    /**
     * 获取故事的用户信息
     *
     * @param jsonArray
     * @param handler
     */
    public static void GET_USER_INFORMATION_BY_ID(final JSONArray jsonArray, final Handler handler) {

        //用来标识用户信息返回的个数（每调用一次onResponse（），++）
        final int[] flag_if_over = {0};
        //暂时存储用户ID
        String id;
        final int length_before = App.Public_Story_User.size();

        //先创建对应数量的实例
        for (int i = 0; i < jsonArray.length(); i++) {
            App.Public_Story_User.add(new UserBase());
        }

        //获取用户信息操作
        for (int i = 0; i < jsonArray.length(); i++) {

            //用户的Json数据实例
            JSONObject jb_ = null;

            //用来标记这是第几个故事对应的用户信息（与  App.Public_Story_User.set(flag, userBase);  相配合）
            final int flag = i;
            try {
                jb_ = new JSONObject(jsonArray.get(i).toString());
                id = jb_.getString("AuthorID");
                System.out.println("ID : " + id);
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_GET_USER_INFO)
                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                        .addParams("AuthorID", id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                //Error 处理
                                System.out.println(e);
                            }

                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                try {
                                    flag_if_over[0]++;
                                    System.out.println("flag : " + flag);
                                    //将返回的用户信息转成Json数据
                                    JSONObject response_jb = new JSONObject(response);

                                    //System.out.println(response_jb);

                                    //将用户信息转为UserBase对象
                                    UserBase userBase = gson.fromJson(response_jb.getJSONObject("msg").getJSONObject("user").toString(), UserBase.class);
                                    App.Public_Story_User.set(flag + length_before, userBase);

                                    System.out.println(App.Public_Story_User.get(flag + length_before).getId());

                                    //System.out.println("finalI :"+finalI);
                                    //Log.e("GET_USER_INFO_FLAG",i_num[0] + "  " + finalI);

                                    //所有信息转换完毕，通知上层函数
                                    if (flag_if_over[0] == jsonArray.length()) {
                                        System.out.println("Handler + " + flag_if_over[0]);
                                        handler.sendEmptyMessage(1);
                                        System.out.println(User.getInstance().getUserName());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 上传故事
     *
     * @param context
     * @param story
     */
    public static void UpLoad_story(final Context context, final Story story, final String fileName) {
        if (Tool.JudgeIsLongin(context)) {
            final String[] contents = story.getContent().split("<img>");
            final Map<String, String> imgMap = Collections.synchronizedMap(new HashMap<String, String>());

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    System.out.println("contents.length : " + contents.length);
                    System.out.println(imgMap);
                    if(imgMap.size() == contents.length/2){
                        switch (msg.what) {
                            case 0:
                                String content = "";
                                for(int i = 0; i < contents.length; i++){
                                    if(i%2 != 0){
                                        contents[i] = imgMap.get(i + "");
                                    }
                                    content += contents[i];
                                }
//                                story.setContent(content);
                                OkHttpUtils
                                        .post()
                                        .url(Final_Static_data.URL_ADD_STORY)
                                        .addHeader("LoginToken", User.getInstance().getLoginToken())
                                        .addParams("title", story.getTitle())
                                        .addParams("flags", story.getFlags())
                                        .addParams("content", content)
                                        .addParams("publicEnable", story.getPublicEnable() + "")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e) {
                                                if (User.getInstance().getLoginToken() == null) {
                                                    Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.getString("msg").equals("LoginToken")) {
                                                        getNew_Token(User.getInstance().getToken());
                                                        UpLoad_story(context, story, fileName);
                                                    } else {
                                                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                break;
                        }
                    }else{
                        System.out.println("图片还没上传完呢！！！");
                    }
                }
            };
            if (contents.length == 1){//纯文字故事上传
                handler.sendEmptyMessage(0);
            }
            for(int i = 0; i < contents.length; i++){
                if(i % 2 != 0){//保存的是添加图片的路径
                    System.out.println("i : " + contents[i]);
                    File file1 = new File(contents[i]);
                    final int finalI = i;
                    upFile(fileName, finalI, file1, imgMap, handler);
                }else{
                    System.out.println("not img" + i);
                }
            }
        }else{
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 上传文件到服务器（图片）
     * @param fileName
     * @param finalI
     * @param file
     * @param imgMap
     * @param handler
     */
    public static void upFile(final String fileName, final int finalI, final File file, final Map imgMap, final Handler handler){
        OkHttpUtils
                .post()//
                .url(Final_Static_data.UP_FILE)
                .addFile(fileName, fileName + finalI, file)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("UpLoadFile fail : " + e.toString());
                        upFile(fileName, finalI, file, imgMap, handler);
                    }

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        System.out.println("i : " + finalI);
                        imgMap.put(finalI + "", "<img>" + response + "<img>");
                        handler.sendEmptyMessage(0);
                    }
                });
    }


    /**
     * 失效后获取新Token
     *
     * @param token
     */
    public static void getNew_Token(String token) {
        OkHttpUtils
                .post()
                .url(Final_Static_data.URL_GET_TOKEN)
                .addParams("token", User.getInstance().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                            if (jsonObject1.getString("token").equals(User.getInstance().getToken())) {
                                User.getInstance().setLoginToken(jsonObject1.getString("LoginToken"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            assert fileUrl != null;
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            return null;
        }
        return bitmap;
    }


    /**
     * 登陆函数
     *
     * @param et_username
     * @param et_password
     * @param connect_flag
     * @param context
     * @return
     */

    public static void Login(EditText et_username, final EditText et_password, boolean connect_flag, final Context context) {
        System.out.println(connect_flag);
        if (et_username.getText().toString().equals("")) {
            Toast.makeText(context, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (et_password.getText().toString().equals("")) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (!connect_flag) {
                Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
            } else {
                OkHttpUtils
                        .post()
                        .url(Final_Static_data.URL_LOGIN)
                        .addParams("mobile", et_username.getText().toString())
                        .addParams("password", et_password.getText().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                LoginAndRegisterOperator.Login_error_tip(context, "账号或密码错误！", et_password);
                            }

                            @Override
                            public void onResponse(String response) {
                                //System.out.println(response + "啦啦啦");
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                                    System.out.println(jsonObject1);

                                    if (jsonObject.getBoolean("status")) {
                                        Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();

                                        //将用户信息存储到SharePreferences中
                                        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("user_info", String.valueOf(jsonObject1));
                                        editor.apply();

                                        //将用户信息存储到SharePreferences中
                                        sp = context.getSharedPreferences("User_", Context.MODE_PRIVATE);
                                        editor = sp.edit();
                                        editor.putString("user_info", String.valueOf(jsonObject1));
                                        editor.apply();

                                        //获取用户信息
                                        User user = User.getInstance();
                                        user.setId(jsonObject1.getInt(User.USER_ID));
                                        user.setLoginToken(jsonObject1.getString(User.USER_LOGIN_TOKEN));
                                        user.setToken(jsonObject1.getString(User.USER_TOKEN));
                                        getUserInfo(context, String.valueOf(User.getInstance().getId()));

                                        context.startActivity(new Intent(context, MainActivity.class));
                                        ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        ((Activity) context).finish();
                                    } else {
                                        System.out.println("Callback error");
                                        LoginAndRegisterOperator.Login_error_tip(context, "账号或密码错误！", et_password);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    LoginAndRegisterOperator.Login_error_tip(context, "账号或密码错误！", et_password);
                                }
                            }
                        });
            }
        }
    }


    /**
     * 注册函数
     *
     * @param et_password
     * @param et_phone_number
     * @param context
     * @return
     */

    public static void register(final EditText et_password, final EditText et_phone_number, final Context context) {

        if (et_phone_number.getText().toString().equals("")) {
            Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (et_password.getText().toString().equals("")) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            OkHttpUtils
                    .post()
                    .url(Final_Static_data.URL_REGISTER)
                    .addParams("mobile", et_phone_number.getText().toString())
                    .addParams("password", et_password.getText().toString())
                    .addParams("userName", "未命名")
                    .addParams("avatar", "http://cdnq.duitang.com/uploads/item/201410/08/20141008104934_vhuuX.jpeg")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            System.out.println(e.toString());
                            LoginAndRegisterOperator.Login_error_tip(context, "手机号不存在或已注册！", et_password);
                        }

                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                JSONObject object = jsonObject.getJSONObject("msg");
                                System.out.println(jsonObject);
                                if (jsonObject.getBoolean("status")) {

                                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();

                                    //将用户信息存储到SharePreferences中
                                    SharedPreferences sp = context.getSharedPreferences("User_", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("user_info", String.valueOf(object));
                                    editor.apply();

                                    Login(et_phone_number, et_password, true, context);

                                } else {
                                    System.out.println("Callback error");
                                    LoginAndRegisterOperator.Login_error_tip(context, "手机号不存在或已注册！", et_password);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                LoginAndRegisterOperator.Login_error_tip(context, "手机号不存在或已注册！", et_password);
                            }
                        }
                    });
        }
    }


    /**
     * 根据url获取并设置头像
     *
     * @param url
     * @param img_avatar
     */
    public static void Set_Avatar(final String url, final ImageView img_avatar) {
        final Bitmap[] bitmap = {null};
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        img_avatar.setImageBitmap(bitmap[0]);
                        break;
                }
            }
        };
        if (url.equals("111")) {
            img_avatar.setImageResource(R.drawable.img_defaultavatar);
        } else {
            System.out.println("setAvatar : " + url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bitmap[0] = NetWorkOperator.returnBitmap(url);
                    if (bitmap[0] != null) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            }).start();
        }
    }


    /**
     * 获取用户信息
     *
     * @param context
     */

    public static void getUserInfo(final Context context, final String id) {
        System.out.println("Begin getUserInfo!!!!!");
        System.out.println(User.getInstance().getLoginToken());
        if (Tool.JudgeIsLongin(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils
                            .post()
                            .url(Final_Static_data.URL_GET_USER_INFO)
                            .addHeader("LoginToken", User.getInstance().getLoginToken())
                            .addParams("AuthorID", id)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    System.out.println("E_R_R_O_R");
                                }

                                @Override
                                public void onResponse(String response) {


                                    try {
                                        Log.e("USERINFO", response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        //如果登陆失效则重新获取Token,再次执行该函数
                                        if (jsonObject.getString("msg").equals("LoginToken")) {
                                            getNew_Token(User.getInstance().getToken());
                                            getUserInfo(context, id);
                                        }

                                        JSONObject object = jsonObject.getJSONObject("msg");
                                        JSONObject user_info = object.getJSONObject("user");
                                        JSONArray js_array_follower = object.getJSONArray("follower");
                                        JSONArray js_array_following = object.getJSONArray("following");
                                        System.out.println(object);
                                        User user = User.getInstance();

                                        //将用户信息存储到SharePreferences中
                                        SharedPreferences sp = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("user_info", jsonObject.getString("msg"));
                                        editor.apply();

                                        user.setAvatar(user_info.getString(User.USER_AVATAR));
                                        //user.setCollectedStoriesCount(user_info.getInt(User.USER_COLLECTED_STORIES_COUNT));
                                        user.setSign(user_info.getString(User.USER_SIGN));
                                        user.setUserName(user_info.getString(User.USER_USER_NAME));
                                        user.setSex(user_info.getInt(User.USER_SEX));


                                        //获取我关注的和关注我的；
                                        Gson gson = new Gson();
                                        List<UserBase> list_user_base = new ArrayList<UserBase>();
                                        UserBase userBase = null;
                                        App.Public_Care_Me.clear();
                                        if (js_array_follower.length() != 0) {
                                            if (!js_array_follower.get(0).toString().equals("false")) {
                                                //同步关注我的人信息
                                                for (int i = 0; i < js_array_follower.length(); i++) {
                                                    userBase = gson.fromJson(js_array_follower.get(i).toString(), UserBase.class);
                                                    System.out.println("有人关注我 ： " + userBase.getId());
                                                    list_user_base.add(userBase);
                                                    App.Public_Care_Me.add(userBase);
                                                }
                                                if (list_user_base.size() != 0) {
                                                    user.setFollower(list_user_base);
                                                }
                                            }
                                        }

                                        list_user_base.clear();

                                        App.Public_Care_Other.clear();
                                        //同步我关注的人信息
                                        if (js_array_following.length() != 0) {
                                            if (!js_array_following.get(0).toString().equals("false")) {
                                                for (int i = 0; i < js_array_following.length(); i++) {
                                                    userBase = gson.fromJson(js_array_following.get(i).toString(), UserBase.class);
                                                    System.out.println("我在关注TA ： " + userBase.getId());
                                                    list_user_base.add(userBase);
                                                    App.Public_Care_Other.add(userBase);
                                                    System.out.println(js_array_following);
                                                }

                                                if (list_user_base.size() != 0) {
                                                    user.setFollowing(list_user_base);
                                                }
                                            }
                                        }

                                        //同步收藏文章数目
                                        user.setCollectedStoriesCount(object.getInt("collectedStoriesCount"));

                                        //获取自己上传的文章
                                        Get_Person_Story_List(context, String.valueOf(user.getId()), 2, 1);

                                        Intent intent = new Intent();
                                        intent.setAction("GET_INFO");
                                        context.sendBroadcast(intent);//传递过去

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                }
            }).start();
        } else {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
        }

    }

}
