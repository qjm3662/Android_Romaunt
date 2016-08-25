package com.example.qjm3662.newproject.StoryView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;

import java.util.Date;

public class StoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static StoryListViewAdapter adapter;
    private ImageView img_add;
    private ImageView img_up_load;
    private View view;
    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_story2, container, false);
        initView();
        return view;
    }


    private void initView() {
        adapter = new StoryListViewAdapter(view.getContext());
        adapter.setMode(Attributes.Mode.Single);
        img_add = (ImageView) getActivity().findViewById(R.id.add_imageView_story);
        img_add.setOnClickListener(this);
        img_up_load = (ImageView) getActivity().findViewById(R.id.cloud_imageView_story);
        img_up_load.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        refreshStoryListView();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_imageView_story:
                startActivityForResult(new Intent(view.getContext(),Edit_Story.class),5);
                break;
            case R.id.cloud_imageView_story:

        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        Story story = App.StoryList.get(position);
        Date date = new Date(System.currentTimeMillis());
        story.setUpdatedAt(date.toString());
        if(User.getInstance().getLoginToken() == null){
            Toast.makeText(view.getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }else {
            System.out.println("开始上传");
            NetWorkOperator.UpLoad_story(view.getContext(), story, "fileName");
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
        //打开编辑界面，查看故事
        Intent i = new Intent(view.getContext(),Edit_Story.class);
        //加一个标记，true表示是已有的故事查看
        i.putExtra("JUDGE",true);
        i.putExtra("ID", App.StoryList.get(position).getLocal_id());
        i.putExtra("position",position);
        this.startActivity(i);
    }


    public static void refreshStoryListView() {
        //更新数据（查询出所有的数据）
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 5:
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
