package com.example.qjm3662.newproject.StoryView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.StoryView.Edit_Story.Edit_Story;
import com.example.qjm3662.newproject.StoryView.Slide.SlideAdapter;

import java.sql.Date;


public class StoryFragment extends Fragment implements OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
	
	private ListViewCompat mListView;
	private View view;
	public static SlideAdapter slideAdapter;
	private ImageView img_add;
	private ImageView img_up_load;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_story, container, false);
		initView();



		return view;
	}
	
	private void initView() {
		mListView = (ListViewCompat) view.findViewById(android.R.id.list);
		slideAdapter = new SlideAdapter(getActivity());
		mListView.setAdapter(slideAdapter);
		img_add = (ImageView) getActivity().findViewById(R.id.add_imageView_story);
		img_add.setOnClickListener(this);
		img_up_load = (ImageView) getActivity().findViewById(R.id.cloud_imageView_story);
		img_up_load.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		refreshStoryListView();
	}

	public static void refreshStoryListView() {
		//更新数据（查询出所有的数据）
		slideAdapter.notifyDataSetChanged();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();

		//打开编辑界面，查看故事
		Intent i = new Intent(view.getContext(),Edit_Story.class);
		//加一个标记，true表示是已有的故事查看
		i.putExtra("JUDGE",true);
		i.putExtra("ID",App.StoryList.get(position).getLocal_id());
		i.putExtra("position",position);
		this.startActivity(i);
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.add_imageView_story:
				startActivityForResult(new Intent(view.getContext(),Edit_Story.class),5);
				break;
			case R.id.cloud_imageView_story:

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case 5:
				slideAdapter.notifyDataSetChanged();
				break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
}
