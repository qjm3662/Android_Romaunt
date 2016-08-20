package com.example.qjm3662.newproject.Finding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Tool.Tool;
import com.mylhyl.crlayout.IFooterLayout;
import com.mylhyl.crlayout.SwipeRefreshListView;
import com.mylhyl.crlayout.app.SwipeRefreshListFragment;


public class Finding_fragment extends SwipeRefreshListFragment {
    public static Finding_ListAdapter adapter;
    public static SwipeRefreshListView swipeRefreshListView;
    private static final int REFREASH = 0x110;


    //响应刷新操作
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFREASH:
                    if (User.getInstance().getLoginToken() != null) {
                        NetWorkOperator.Get_finding_story(0, String.valueOf(System.currentTimeMillis()));
                    } else {
                        Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        //注意在 setOnListLoadListener 或 setEnabledLoad 之前使用
        getSwipeRefreshLayout().setFooterResource(R.layout.swipe_refresh_footer);
        setEnabledLoad(true);

        IFooterLayout footerLayout = getSwipeRefreshLayout().getFooterLayout();
        footerLayout.setProgressBarVisibility(1);
        footerLayout.setFooterText("加载更多数据...");
        footerLayout.setFooterTextSize(18);
        //footerLayout.setFooterTextColor(Color.GREEN);

        swipeRefreshListView = getSwipeRefreshLayout();
        if (User.getInstance().getLoginToken() != null) {
            swipeRefreshListView.autoRefresh();
        } else {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }

        adapter = new Finding_ListAdapter(getContext());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onListItemClick(parent, view, position, id);
//        //打开编辑界面，查看故事
//        Intent i = new Intent(getContext(), Story_pre.class);
//        i.putExtra(Edit_Acticity.EDIT_TITLE, App.Public_StoryList.get(position).getTitle());
//        i.putExtra(Edit_Acticity.EDIT_CONTENT, App.Public_StoryList.get(position).getContent());
//        //加一个标记，true表示是已有的故事查看
//        i.putExtra("JUDGE", false);
//        i.putExtra("position", position);
//        startActivity(i);
        Intent i = new Intent(getContext(), StoryView.class);
        i.putExtra("position", position);
        startActivity(i);
        ((Activity)getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if(Tool.JudgeIsLongin(getContext())){
            handler.sendEmptyMessageDelayed(REFREASH, 0);
        }else{
            swipeRefreshListView.setRefreshing(false);
        }
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onListLoad() {
        NetWorkOperator.Get_finding_story(1, App.Public_StoryList.get(App.Public_StoryList.size() - 1).getUpdatedAt());
    }

}



