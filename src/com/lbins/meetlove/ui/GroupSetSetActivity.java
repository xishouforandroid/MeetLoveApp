package com.lbins.meetlove.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 */
public class GroupSetSetActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "GroupSetSetActivity";

    private TextView title;
    private String groupId;
    private EMGroup group;

    private EaseSwitchButton switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_set_activity);
        groupId = getIntent().getExtras().getString("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        if(group == null){
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("详细设置");

        this.findViewById(R.id.liner5).setOnClickListener(this);

        RelativeLayout rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
        rl_switch_block_groupmsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner5:
            {
                //投诉
                Intent intent = new Intent(GroupSetSetActivity.this, AddReportActivity.class);
                intent.putExtra("name" , (group.getGroupName()==null?"":group.getGroupName()));
                startActivity(intent);
            }
            break;
            case R.id.rl_switch_block_groupmsg:
                // 屏蔽或取消屏蔽群组
                toggleBlockGroup();
                break;
        }
    }

    private ProgressDialog progressDialog;

    private void toggleBlockGroup() {
        if(switchButton.isSwitchOpen()){
            EMLog.d(TAG, "change to unblock group msg");
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupSetSetActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(getString(R.string.Is_unblock));
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().unblockGroupMessage(groupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                switchButton.closeSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.remove_group_of, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();

        } else {
            String st8 = getResources().getString(R.string.group_is_blocked);
            final String st9 = getResources().getString(R.string.group_of_shielding);
            EMLog.d(TAG, "change to block group msg");
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupSetSetActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(st8);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().blockGroupMessage(groupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                switchButton.openSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }

}
