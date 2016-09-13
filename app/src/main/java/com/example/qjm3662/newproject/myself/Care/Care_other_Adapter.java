package com.example.qjm3662.newproject.myself.Care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;

import lib.lhh.fiv.library.FrescoImageView;

/**
 * Created by qjm3662 on 2016/7/1 0001.
 */
public class Care_other_Adapter extends BaseAdapter{

    private class ViewHolder {
        private TextView tv_nickname;
        private TextView tv_sign;
        private ImageView img_avatar;
    }

    private LayoutInflater inflater = null;

    public Care_other_Adapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return App.Public_Care_Other.size();
    }

    @Override
    public Object getItem(int position) {
        return App.Public_Care_Other.get(position);
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
            convertView = inflater.inflate(R.layout.user_info_list_item, null);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.user_info_item_name);
            viewHolder.tv_sign = (TextView) convertView.findViewById(R.id.user_info_item_sign);
            viewHolder.img_avatar = (ImageView) convertView.findViewById(R.id.user_info_item_head);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User userBase = App.Public_Care_Other.get(position);
        viewHolder.tv_nickname.setText(userBase.getUserName());
        viewHolder.tv_sign.setText(userBase.getSign());
//        NetWorkOperator.Set_Avatar(userBase.getAvatar(), viewHolder.img_avatar);
        if(userBase.getBitmap() != null){
            viewHolder.img_avatar.setImageBitmap(userBase.getBitmap());
        }

        return convertView;
    }
}
