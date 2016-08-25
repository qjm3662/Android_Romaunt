package com.example.qjm3662.newproject.myself.Article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by qjm3662 on 2016/7/8 0008.
 */
public class MyArticleAdapter extends BaseAdapter {


    private LayoutInflater inflater = null;

    public MyArticleAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        private TextView hps_flag;
        private TextView hps_title;
        private TextView hps_time;
        private TextView hps_introduce;
        private TextView hps_like_count;

    }

    @Override
    public int getCount() {
        return App.Public_My_Article_StoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return App.Public_My_Article_StoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.home_page_story_list_item, null);
            viewHolder.hps_flag = (TextView) convertView.findViewById(R.id.hps_flag);
            viewHolder.hps_introduce = (TextView) convertView.findViewById(R.id.hps_introduce);
            viewHolder.hps_like_count = (TextView) convertView.findViewById(R.id.hps_likeCount);
            viewHolder.hps_time = (TextView) convertView.findViewById(R.id.hps_time);
            viewHolder.hps_title = (TextView) convertView.findViewById(R.id.hps_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StoryBean storyBean = App.Public_My_Article_StoryList.get(position);


        //时间操作
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        sdr.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        if(storyBean != null){
            if(storyBean.getCreatedAt() != null){
                Date date = new Date(Long.parseLong(storyBean.getCreatedAt()) * 1000L);
                String s_date = sdr.format(date);
                viewHolder.hps_time.setText(s_date);
            }
            viewHolder.hps_title.setText(storyBean.getTitle());
            viewHolder.hps_like_count.setText(storyBean.getLikeCount() + "");
            viewHolder.hps_introduce.setText(storyBean.getContent());
            viewHolder.hps_flag.setText(storyBean.getFlags());
        }
        return convertView;
    }
}
