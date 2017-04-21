package com.lbins.meetlove.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.ItemMessageAdapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.library.PullToRefreshBase;
import com.lbins.meetlove.library.PullToRefreshListView;
import com.lbins.meetlove.ui.MineMsgActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class TwoFragment extends BaseFragment implements View.OnClickListener  {
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private View view;
    private Resources res;
    private TextView title;

    private PullToRefreshListView lstv;
    private ItemMessageAdapter adapter;
    List<String> lists = new ArrayList<String>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private LinearLayout headLiner;

    private ImageView notice_cover;
    private TextView notice_number;
    private TextView notice_title;
    private TextView notice_msg;
    private TextView notice_dateline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.two_fragment, null);
        res = getActivity().getResources();
        initView();
        return view;
    }

    void initView(){
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        view.findViewById(R.id.back).setVisibility(View.GONE);
        view.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("消息");

        lstv = (PullToRefreshListView) view.findViewById(R.id.lstv);
        adapter = new ItemMessageAdapter(lists, getActivity());
        headLiner = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.two_header, null);
        notice_cover = (ImageView) headLiner.findViewById(R.id.notice_cover);
        notice_number = (TextView) headLiner.findViewById(R.id.notice_number);
        notice_title = (TextView) headLiner.findViewById(R.id.notice_title);
        notice_msg = (TextView) headLiner.findViewById(R.id.notice_msg);
        notice_dateline = (TextView) headLiner.findViewById(R.id.notice_dateline);


        final ListView listView = lstv.getRefreshableView();
        listView.addHeaderView(headLiner);

        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>(position-1)){

                }

            }
        });

        headLiner.findViewById(R.id.relate_notice).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relate_notice:
            {
                //系统通知
                Intent intent = new Intent(getActivity(), MineMsgActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    void getData(){}

}
