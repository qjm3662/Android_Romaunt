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

/**
 * Created by qjm3662 on 2016/7/1 0001.
 */
public class Care_me_Adapter extends BaseAdapter {

    private class ViewHolder {
        private TextView tv_nickname;
        private TextView tv_sign;
        private ImageView img_avatar;
        private ImageView img_sex;
    }

    private LayoutInflater inflater = null;

    public Care_me_Adapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return App.Public_Care_Me.size();
    }

    @Override
    public Object getItem(int position) {
        return App.Public_Care_Me.get(position);
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
            viewHolder.img_sex = (ImageView) convertView.findViewById(R.id.user_info_item_sex);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User userBase = App.Public_Care_Me.get(position);
        viewHolder.tv_nickname.setText(userBase.getUserName());
        viewHolder.tv_sign.setText(userBase.getSign());

        //1男2女
        if(userBase.getSex() == 1){
            viewHolder.img_sex.setImageResource(R.drawable.img_small_male);
        }else{
            viewHolder.img_sex.setImageResource(R.drawable.img_small_female);
        }
//        NetWorkOperator.Set_Avatar(userBase.getAvatar(), viewHolder.img_avatar);
        viewHolder.img_avatar.setImageBitmap(userBase.getBitmap());

        return convertView;
    }
}
