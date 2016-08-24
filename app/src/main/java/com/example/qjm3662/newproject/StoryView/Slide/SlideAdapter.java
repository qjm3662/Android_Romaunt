package com.example.qjm3662.newproject.StoryView.Slide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.StoryDB;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Tool.Tool;


public class SlideAdapter extends BaseAdapter implements SlideView.OnSlideListener {

    private LayoutInflater mInflater;
    private SlideView mLastSlideViewWithStatusOn;
    private Context context;

    public SlideAdapter(Context context) {
        super();
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return App.StoryList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return App.StoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        /******新发现******/
        /*
		 * 在通过adapter生成view时，convertView并不是一直是一个，也就是说下面的if语句会运行多次
		 * 执行的次数应该就是第一次显示ListView时在屏幕上显示的item的个数
		 * 也就是说  第七个item的convertView 就是第一个item的convertView
		 */

		/*
		 * 调用notifyDataSetChanged()方法  会重新调用getView方法
		 * 但和生成ListView时不一样，这次使用的 concertView还是以前的
		 * 也就是说 这次调用 listView不会进入if语句
		 */

        if (slideView == null) {
            //Log.e(TAG,"进来了吗");
            View itemView = mInflater.inflate(R.layout.story_list_item, null);
            slideView = new SlideView(context);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(this);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }

        Story item = App.StoryList.get(position);
        item.slideView = slideView;
        //调用notifyDataSetChanged()后重新绘制ListView时 使用的还是上次的convertView
        //也就是说convertView的状态不会改变  比如打开状态
        //这时就需要手动滑回去  也就是下一行代码
        //注意shrink()不能用smoothScrollTo() 用这个会缓慢的滑回去  效果就是删掉了item2
        //重绘ListView  原本显示item2信息的convertView 显示item3的信息
        //并且缓慢的由 打开 到关闭
        //所以应该直接调用  scrollTo()
        item.slideView.shrink();

        holder.tv_title.setText(item.getTitle());
        holder.tv_content.setText(item.getContent());
        holder.tv_time.setText(item.getCreatedAt() + "");
        if (item.getPublicEnable() == 1) {
            holder.img_public_able.setImageResource(R.drawable.img_public);
        } else {
            holder.img_public_able.setImageResource(R.drawable.img_privacy);
        }
        holder.tv_flag.setText(item.getFlags());
        System.out.println("Item Date : ======>" + item.getCreatedAt());

        //为什么不会产生 线程问题？ 为什么可以在getView中对tv_delete操作？
        holder.tv_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log.e(TAG,"position"+position);
                System.out.println("Begin delete!!!!");
                Story story = App.StoryList.get(position);
                String[] contents = story.getContent().split("<img>");
                //删除本地故事时删除其缓存的图片
                for(int i = 0; i < contents.length; i++){
                    if(i % 2 != 0){
                        Tool.deleteFile(contents[i]);
                    }
                }
                App.dbWrite.delete(StoryDB.TABLE_NAME_STORY, StoryDB.COLUMN_NAME_ID + "=?", new String[]{String.valueOf(story.getLocal_id())});
                App.StoryList.remove(position);
                notifyDataSetChanged();
                System.out.println("End delete!!");
            }
        });
        return slideView;
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }

    }

    private static class ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_delete;
        private TextView tv_time;
        private ImageView img_public_able;
        private TextView tv_flag;

        ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.story_list_item_title);
            tv_content = (TextView) view.findViewById(R.id.story_list_item_introduce);
            tv_delete = (TextView) view.findViewById(R.id.delete_slide);
            tv_time = (TextView) view.findViewById(R.id.story_list_item_clock);
            img_public_able = (ImageView) view.findViewById(R.id.story_list_item_public_able);
            tv_flag = (TextView) view.findViewById(R.id.story_list_item_flag);
        }
    }

}