package com.example.qjm3662.newproject.StoryView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static StoryListViewAdapter adapter;
    private ImageView img_add;
    private ImageView img_up_load;
    private View view;
    private Context context;
    private ListView listView;
    private SweetAlertDialog pDialog;
    private UploadingHandler handler;
    public static final int UPLOADING_SUCCESS = 254;
    public static final int UPLOADING_FAIL = 214;
    public static final int UPLOADING_FALL_NOT_LOGIN = 222;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_story2, container, false);
        context = getContext();
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Uploading");
        pDialog.setCancelable(false);
        handler = new UploadingHandler();
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
//        //列表内容出现动画
//        Tool.ViewGroupAppear(listView, 500);
        refreshStoryListView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_imageView_story:
                startActivityForResult(new Intent(view.getContext(), Edit_Story.class), 5);
                break;
            case R.id.cloud_imageView_story:
                break;

        }
    }

    /**
     *
     */
    private class UploadingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            TimePicker timePicker = new TimePicker(context);
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOADING_SUCCESS:
                    pDialog.setTitleText("Upload Success!");
                    timePicker.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismiss();
                            pDialog.setTitleText("Uploading");
                        }
                    }, 500);
                    break;
                case UPLOADING_FAIL:
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("Upload Fail!");
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.setTitleText("Uploading");
                            pDialog.dismiss();
                        }
                    });
                    break;
                case UPLOADING_FALL_NOT_LOGIN:
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("No account login, please login");
                    timePicker.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismiss();
                            pDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.setTitleText("Uploading");
                        }
                    }, 1000);
                    break;
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        Story story = App.StoryList.get(position);
        Date date = new Date(System.currentTimeMillis());
        story.setUpdatedAt(date.toString());
        if (User.getInstance().getLoginToken() == null) {
            Toast.makeText(view.getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("开始上传");
            pDialog.show();
            NetWorkOperator.UpLoad_story(view.getContext(), story, "fileName", handler);
        }
        return true;
    }

    public boolean isNetWork() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo == null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
        //打开编辑界面，查看故事
        Intent i = new Intent(view.getContext(), Edit_Story.class);
        //加一个标记，true表示是已有的故事查看
        i.putExtra("JUDGE", true);
        i.putExtra("ID", App.StoryList.get(position).getLocal_id());
        i.putExtra("position", position);
        this.startActivity(i);
    }


    public static void refreshStoryListView() {
        //更新数据（查询出所有的数据）
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 5:
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
