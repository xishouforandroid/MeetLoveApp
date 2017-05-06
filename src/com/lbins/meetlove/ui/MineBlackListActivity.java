package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineBlackListActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private BlacklistAdapter adapter;
    private ListView lstv;
    private List<String> blacklist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_blacklist_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("黑名单");

        blacklist = EMClient.getInstance().contactManager().getBlackListUsernames();
        lstv = (ListView) this.findViewById(R.id.lstv);

        if (blacklist != null) {
            Collections.sort(blacklist);
            adapter = new BlacklistAdapter(this, blacklist);
            lstv.setAdapter(adapter);
        }
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(blacklist.size()>position){
                    final String tobeRemoveUser = blacklist.get(position);
                    showDelDialog(tobeRemoveUser);

                }
            }
        });
    }
    private void showDelDialog(final String tobeRemoveUser) {
        final Dialog picAddDialog = new Dialog(MineBlackListActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_black_emp_dialog, null);
        Button btn_sure = (Button) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOutBlacklist(tobeRemoveUser);
                picAddDialog.dismiss();
            }
        });

        //取消
        Button btn_cancel = (Button) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineBlackListActivity.this, ProfileEmpActivity.class);
                intent.putExtra("empid", tobeRemoveUser);
                startActivity(intent);
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }


    void removeOutBlacklist(final String tobeRemoveUser) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.be_removing));
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(tobeRemoveUser);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            adapter.remove(tobeRemoveUser);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.Removed_from_the_failure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * adapter
     *
     */
    private class BlacklistAdapter extends ArrayAdapter<String> {

        public BlacklistAdapter(Context context, List<String> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.ease_row_contact, null);
            }
            String username = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);

            EaseUserUtils.setUserAvatar(getContext(), username, avatar);
            EaseUserUtils.setUserNick(username, name);

            return convertView;
        }

    }
}
