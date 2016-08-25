package com.example.qjm3662.newproject.StoryView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.StoryDB;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Tool.Tool;

/**
 * Created by qjm3662 on 2016/8/24 0024.
 */
public class StoryListViewAdapter extends BaseSwipeAdapter {

    private LayoutInflater inflater = null;
    private Context context;
    private boolean isSwipe = false;


    public StoryListViewAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
//        private TextView tv_delete;
        private TextView tv_time;
        private ImageView img_public_able;
        private TextView tv_flag;

        ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.story_list_item_title);
            tv_content = (TextView) view.findViewById(R.id.story_list_item_introduce);
//            tv_delete = (TextView) view.findViewById(R.id.delete_slide);
            tv_time = (TextView) view.findViewById(R.id.story_list_item_clock);
            img_public_able = (ImageView) view.findViewById(R.id.story_list_item_public_able);
            tv_flag = (TextView) view.findViewById(R.id.story_list_item_flag);
        }
    }


    @Override
    public int getCount() {
        return App.StoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return App.StoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sample;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.activity_story_list_item, null);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.sample);
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
                isSwipe = false;
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
                isSwipe = true;
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                isSwipe = true;
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
        return view;
    }

    @Override
    public void fillValues(final int position, View view) {
        ViewHolder viewHolder = null;
        viewHolder = new ViewHolder(view);
        TextView tv = (TextView) view.findViewById(R.id.delete_slide);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        Story item = App.StoryList.get(position);
        viewHolder.tv_title.setText(item.getTitle());
        viewHolder.tv_content.setText(item.getContent());
        viewHolder.tv_time.setText(item.getCreatedAt() + "");
        if (item.getPublicEnable() == 1) {
            viewHolder.img_public_able.setImageResource(R.drawable.img_public);
        } else {
            viewHolder.img_public_able.setImageResource(R.drawable.img_privacy);
        }
        viewHolder.tv_flag.setText(item.getFlags());
        System.out.println("Item Date : ======>" + item.getCreatedAt());
    }
}
