package com.lbins.meetlove.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MainActivity;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.ContactAdapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.DBHelper;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.dao.Friends;
import com.lbins.meetlove.data.EmpsData;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.ui.FriendsApplyActivity;
import com.lbins.meetlove.ui.ProfileEmpActivity;
import com.lbins.meetlove.util.PinyinComparator;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.SideBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class ThreeFragment extends BaseFragment implements View.OnClickListener  {
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private View view;
    private Resources res;
    private TextView title;

    List<Friends> friendses = new ArrayList<Friends>();

    private ListView lvContact;
    private SideBar indexBar;
    private WindowManager mWindowManager;
    private TextView mDialogText;
    private ContactAdapter adapter;

    private LinearLayout headLiner;
    private RelativeLayout relate_new_friends;
    private TextView new_friends_number;
    private RelativeLayout relate_groups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.three_fragment, null);
        registerBoradcastReceiver();
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        res = getActivity().getResources();
        initView();
        progressDialog = new CustomProgressDialog(getActivity(), "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getFriends();
        refresh();
        return view;
    }


   public void refresh(){
       if(MainActivity.friendsCountUnRead > 0){
           new_friends_number.setVisibility(View.VISIBLE);
           new_friends_number.setText(String.valueOf(MainActivity.friendsCountUnRead));
       }else {
           new_friends_number.setVisibility(View.INVISIBLE);
       }
    }

    void initView(){
        view.findViewById(R.id.back).setVisibility(View.GONE);
        view.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("通讯录");

        lvContact = (ListView) view.findViewById(R.id.lvContact);
        adapter = new ContactAdapter(getActivity(), friendses);
        headLiner = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.three_header, null);
        relate_new_friends = (RelativeLayout) headLiner.findViewById(R.id.relate_new_friends);
        new_friends_number = (TextView) headLiner.findViewById(R.id.new_friends_number);


        lvContact.setAdapter(adapter);
        indexBar = (SideBar) view.findViewById(R.id.sideBar);
        indexBar.setListView(lvContact);
        lvContact.addHeaderView(headLiner);

        mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    if(friendses.size() > (position-1)){
                        Friends friends = friendses.get((position-1));
                        if(friends != null){
                            Intent intent = new Intent(getActivity(), ProfileEmpActivity.class);
                            intent.putExtra("empid", friends.getEmpid2());
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        headLiner.findViewById(R.id.relate_new_friends).setOnClickListener(this);
        headLiner.findViewById(R.id.relate_groups).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relate_new_friends:
            {
                //新的朋友申请
                Intent intent = new Intent(getActivity(), FriendsApplyActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_groups:
            {
                //群聊
            }
                break;
        }
    }

    private void getFriends() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appFriends,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    FriendsData data = getGson().fromJson(s, FriendsData.class);
                                    if(data != null){
                                        friendses.clear();
                                        friendses.addAll(data.getData());
                                        Collections.sort(friendses, new PinyinComparator());
                                        adapter.notifyDataSetChanged();

                                    }
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_check", "1");
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("update_contact_success")) {
                getFriends();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_contact_success");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

}
