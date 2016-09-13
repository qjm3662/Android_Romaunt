package com.example.qjm3662.newproject.Finding;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Finding_ListAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    //时间操作
    private SimpleDateFormat sdr_hm = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdr_year = new SimpleDateFormat("yy");
    private SimpleDateFormat sdr_month = new SimpleDateFormat("MM");
    private SimpleDateFormat sdr_day = new SimpleDateFormat("dd");
    private SimpleDateFormat sdr_yM = new SimpleDateFormat("yy-MM");
    private SimpleDateFormat sdr_Md = new SimpleDateFormat("MM-dd");

    public Finding_ListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sdr_hm.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_year.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_month.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_day.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_yM.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
        sdr_Md.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 中国北京时间，东八区
    }

    private class ViewHolder {
        private TextView nickname;
        private TextView flags;
        private TextView title;
        private TextView time;
        private TextView introduce;
        private ImageView icon;
        private TextView sign;
        private TextView likeCount;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return App.Public_StoryList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return App.Public_StoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.listview, null);
            viewHolder.flags = (TextView) view.findViewById(R.id.finding_listView_flags);
            viewHolder.time = (TextView) view.findViewById(R.id.finding_listView_time);
            viewHolder.title = (TextView) view.findViewById(R.id.finding_listView_title);
            viewHolder.icon = (ImageView) view.findViewById(R.id.finding_listView_icon_my);
            viewHolder.introduce = (TextView) view.findViewById(R.id.finding_listView_introduce);
            viewHolder.nickname = (TextView) view.findViewById(R.id.finding_listView_nickname);
            viewHolder.sign = (TextView) view.findViewById(R.id.finding_listview_gexingqianming);
            viewHolder.likeCount = (TextView) view.findViewById(R.id.finding_listView_likeCount);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        StoryBean listInfo = App.Public_StoryList.get(i);
        if(listInfo != null){
            if(listInfo.getCreatedAt() != null){
                Date date = new Date(Long.parseLong(listInfo.getCreatedAt()) * 1000L);
                Date cur_date = new Date(System.currentTimeMillis());
                String s_date = "";
                if(sdr_year.format(date).equals(sdr_year.format(cur_date))){
                    if(sdr_month.format(date).equals(sdr_month.format(cur_date)) && sdr_day.format(date).equals(sdr_day.format(cur_date))){
                        s_date = sdr_hm.format(date);
                    }else{
                        s_date = sdr_Md.format(date);
                    }
                }else{
                    s_date = sdr_yM.format(date);
                }


                viewHolder.time.setText(s_date);
            }
            viewHolder.title.setText(listInfo.getTitle());
            viewHolder.flags.setText(listInfo.getFlags());
            if(viewHolder.likeCount != null){
                System.out.println(viewHolder.likeCount);
                viewHolder.likeCount.setText(listInfo.getLikeCount() + "");
            }else{
                Log.e("LikeCount_TextView","null");
            }

            //设置广场列表显示的文章预览内容（只显示文字内容）
            String[] contents = listInfo.getContent().split("<img>");
            String temp = "";
//            Log.e("FindingList contents", Arrays.toString(contents));
            for(int j = 0; j < contents.length; j++){
                if(j % 2 == 0){
                    temp += contents[j];
                    Log.e("j", j + temp);
                }
            }
//            System.out.println("temp : " + temp.trim());
            viewHolder.introduce.setText(temp.trim());
            if(listInfo.getUser() != null){

                //显示用户头像
                if(listInfo.getUser().getBitmap() != null){
                    viewHolder.icon.setImageBitmap(listInfo.getUser().getBitmap());
                    System.out.println("Bitmap : " + listInfo.getUser().getBitmap());
                }else{
                    viewHolder.icon.setImageResource(R.drawable.img_defaultavatar);
                    System.out.println("Bitmap is null !!");
                }
                viewHolder.nickname.setText(listInfo.getUser().getUserName());
                viewHolder.sign.setText(listInfo.getUser().getSign());
//                Finding_fragment.adapter.notifyDataSetChanged();
            }
        }
        return view;
    }

}
