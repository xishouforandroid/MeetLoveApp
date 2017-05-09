package com.lbins.meetlove;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.chat.Constant;
import com.lbins.meetlove.chat.DemoHelper;
import com.lbins.meetlove.chat.db.InviteMessgeDao;
import com.lbins.meetlove.chat.runtimepermissions.PermissionsManager;
import com.lbins.meetlove.chat.runtimepermissions.PermissionsResultAction;
import com.lbins.meetlove.chat.ui.ChatActivity;
import com.lbins.meetlove.chat.ui.GroupsActivity;
import com.lbins.meetlove.chat.util.SharePrefConstant;
import com.lbins.meetlove.dao.*;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.data.HappyHandGroupData;
import com.lbins.meetlove.data.MsgCountData;
import com.lbins.meetlove.data.VersonCodeObjData;
import com.lbins.meetlove.fragment.FourFragment;
import com.lbins.meetlove.fragment.OneFragment;
import com.lbins.meetlove.fragment.ThreeFragment;
import com.lbins.meetlove.fragment.TwoFragment;
import com.lbins.meetlove.module.MsgCount;
import com.lbins.meetlove.module.VersonCodeObj;
import com.lbins.meetlove.ui.AboutActivity;
import com.lbins.meetlove.ui.LoginActivity;
import com.lbins.meetlove.util.GuirenHttpUtils;
import com.lbins.meetlove.util.StringUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener , Runnable {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;

    private Button foot_one;
    private Button foot_two;
    private Button foot_three;
    private Button foot_four;

    //设置底部图标
    Resources res;

    protected static final String TAG = "MainActivity";
    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;

    private int index;
    private int currentTabIndex;
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;

    public static int msgCountUnRead = 0;
    public static int friendsCountUnRead = 0;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        requestPermissions();
        registerBoradcastReceiver();
        res = getResources();
        fm = getSupportFragmentManager();
        initView();
        boolean isFirstRun = getSp().getBoolean("isFirstRunMain", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor = getSp().edit();
            editor.putBoolean("isFirstRunMain", false);
            editor.commit();
            switchFragment(R.id.foot_one);
        } else {
            switchFragment(R.id.foot_two);
        }

        inviteMessgeDao = new InviteMessgeDao(this);
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        registerInternalDebugReceiver();

        //统计未读消息数
        getMsgCount();
        getFriendsCount();
        checkVersion();

        // 启动一个线程
        new Thread(MainActivity.this).start();
    }
    private void initView() {
        foot_one = (Button) this.findViewById(R.id.foot_one);
        foot_two = (Button) this.findViewById(R.id.foot_two);
        foot_three = (Button) this.findViewById(R.id.foot_three);
        foot_four = (Button) this.findViewById(R.id.foot_four);
        this.findViewById(R.id.foot_one).setOnClickListener(this);
        this.findViewById(R.id.foot_two).setOnClickListener(this);
        this.findViewById(R.id.foot_three).setOnClickListener(this);
        this.findViewById(R.id.foot_four).setOnClickListener(this);

        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_one:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_one.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_recommand_p), null, null);
                foot_two.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_msg), null, null);
                foot_three.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_contact), null, null);
                foot_four.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_mine), null, null);
                index = 0;
                break;
            case R.id.foot_two:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }

                foot_one.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_recommand), null, null);
                foot_two.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_msg_p), null,null);
                foot_three.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_contact), null, null);
                foot_four.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_mine), null, null);
                index = 1;
                break;
            case R.id.foot_three:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }

                foot_one.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_recommand), null, null);
                foot_two.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_msg), null, null);
                foot_three.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_contact_p), null, null);
                foot_four.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_mine), null, null);
                index = 2;
                break;
            case R.id.foot_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                foot_one.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_recommand), null, null);
                foot_two.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_msg), null, null);
                foot_three.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_contact), null, null);
                foot_four.setCompoundDrawablesWithIntrinsicBounds(null, res.getDrawable(R.drawable.tabicon_mine_p), null, null);
                index = 3;
                break;

        }
        fragmentTransaction.commit();
        currentTabIndex = index;
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }
        if (threeFragment != null) {
            ft.hide(threeFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
    }

    boolean isMobileNet, isWifiNet;

    @Override
    public void onClick(View v) {
        try {
            isMobileNet = GuirenHttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = GuirenHttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, R.string.net_work_error, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switchFragment(v.getId());
    }

    VersonCodeObj versionUpdateObj;

    public void checkVersion() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.getVersionCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    VersonCodeObjData data = getGson().fromJson(s, VersonCodeObjData.class);
                                    versionUpdateObj = data.getData();
                                    if(versionUpdateObj != null){
                                        if(!getV().equals(versionUpdateObj.getMm_version_code())){
                                            showVersionDialog();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    String getV(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showVersionDialog() {
        final Dialog picAddDialog = new Dialog(MainActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_version_dialog, null);
        Button btn_sure = (Button) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新
                final Uri uri = Uri.parse(InternetURL.UPDATE_URL);
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                picAddDialog.dismiss();
            }
        });

        //取消
        Button btn_cancel = (Button) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        moveTaskToBack(false);
        if (!isExit) {
            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次退出幸福牵手吧",
//                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private InviteMessgeDao inviteMessgeDao;
    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                // 先将头像和昵称保存在本地缓存
                try {
                    String ChatUserId = message.getStringAttribute(SharePrefConstant.ChatUserId);
                    String ChatUserPic = message.getStringAttribute(SharePrefConstant.ChatUserPic);
                    String ChatUserNick = message.getStringAttribute(SharePrefConstant.ChatUserNick);

                    Emp emp = new Emp();
                    emp.setEmpid(ChatUserId);
                    emp.setCover(ChatUserPic);
                    emp.setNickname(ChatUserNick);
                    DBHelper.getInstance(MainActivity.this).saveEmp(emp);

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }


                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 1) {
                    // refresh conversation list
                    if (twoFragment != null) {
                        twoFragment.refresh();
                    }
                }
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 1) {
                    // refresh conversation list
                    if (twoFragment != null) {
                        twoFragment.refresh();
                    }
                } else if (currentTabIndex == 2) {
//                    if(contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
                //red packet code : 处理红包回执透传消息
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                    if (conversationListFragment != null){
//                        conversationListFragment.refresh();
//                    }
//                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void run() {
        //查询公开库
        appPublicGroups();
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onFriendRequestAccepted(String username) {}
        @Override
        public void onFriendRequestDeclined(String username) {}
    }

    private void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal()+msgCountUnRead;
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
//                int count = getUnreadAddressCountTotal();
                if (friendsCountUnRead > 0) {
//                    if(currentTabIndex == 2){
                        threeFragment.refresh();
//                    }
                    unreadAddressLable.setText(String.valueOf(friendsCountUnRead));
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }
    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false,new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {}

                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    private MsgCount msgCount;
    //统计未读消息数据
    private void getMsgCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appMsgAllList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    MsgCountData data = getGson().fromJson(s, MsgCountData.class);
                                    if(data != null){
                                        msgCount = data.getData();
                                        if(msgCount != null){
                                            initDataCount();
                                        }
                                    }
                                }else {
                                    Toast.makeText(MainActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
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
                params.put("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
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

    void initDataCount(){
        msgCountUnRead = 0;
        if(msgCount != null){
            //系统消息
            List<HappyHandMessage> list1 = msgCount.getList1();
            if(list1 != null){
                for(HappyHandMessage happyHandMessage:list1){
                    HappyHandMessage tmpT = DBHelper.getInstance(MainActivity.this).getHappyHandMessageById(happyHandMessage.getMsgid());
                    if(tmpT != null){
                        //说明数据库里有该条记录
                    }else{
                        //说明数据库里没有该记录
                        DBHelper.getInstance(MainActivity.this).saveHappyHandMessage(happyHandMessage);
                    }
                }
            }

            //查询未读的系统消息
            List<HappyHandMessage> lists1 = DBHelper.getInstance(MainActivity.this).getHappyHandMessageQuery("0");
            if(lists1 != null){
                msgCountUnRead = msgCountUnRead+lists1.size();
            }

            //系统资讯
            List<HappyHandNews> list2 = msgCount.getList2();
            if(list2 != null){
                for(HappyHandNews happyHandNews:list2){
                    HappyHandNews tmpT =DBHelper.getInstance(MainActivity.this).getHappyHandNewsById(happyHandNews.getNewsid());
                    if(tmpT != null){
                        //说明数据库里有该条记录
                    }else{
                        //说明数据库里没有该记录
                        DBHelper.getInstance(MainActivity.this).saveHappyHandNews(happyHandNews);
                    }
                }
            }

            List<HappyHandNews> lists2 = DBHelper.getInstance(MainActivity.this).getHappyHandNewsQuery("0");
            if(lists2 != null){
                msgCountUnRead = msgCountUnRead+lists2.size();
            }

            //活动公告
            List<HappyHandNotice> list3 = msgCount.getList3();
            if(list3 != null){
                for(HappyHandNotice happyHandNotice:list3){
                    HappyHandNotice tmpT =DBHelper.getInstance(MainActivity.this).getHappyHandNoticeById(happyHandNotice.getNoticeid());
                    if(tmpT != null){
                        //说明数据库里有该条记录
                    }else{
                        //说明数据库里没有该记录
                        DBHelper.getInstance(MainActivity.this).saveHappyHandNotice(happyHandNotice);
                    }
                }
            }
            List<HappyHandNotice> lists3 = DBHelper.getInstance(MainActivity.this).getHappyHandNoticeQuery("0");
            if(lists3 != null){
                msgCountUnRead = msgCountUnRead+lists3.size();
            }

            //交往消息
            List<HappyHandJw> list4 = msgCount.getList4();
            if(list4 != null){
                msgCountUnRead = msgCountUnRead+list4.size();
            }
        }
        updateUnreadLabel();
    }



    private void getFriendsCount() {
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
                                        List<Friends> listsFriends = new ArrayList<>();
                                        listsFriends.addAll(data.getData());
                                        friendsCountUnRead = listsFriends.size();
                                        updateUnreadAddressLable();
                                    }
                                }else {
                                    Toast.makeText(MainActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
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
                params.put("empid2", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("is_check", "0");
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


    private void appPublicGroups() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appPublicGroups,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandGroupData data = getGson().fromJson(s, HappyHandGroupData.class);
                                    if(data != null){
                                        List<HappyHandGroup> groups = new ArrayList<>();
                                        groups.addAll(data.getData());
                                        if(groups != null){
                                            for(HappyHandGroup group:groups){
                                                DBHelper.getInstance(MainActivity.this).saveHappyHandGroup(group);
                                            }
                                        }
                                    }
                                }else {
                                    Toast.makeText(MainActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
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
            if (action.equals("update_message_success")) {
                getMsgCount();
//                if (currentTabIndex == 1) {
                    if (twoFragment != null) {
                        twoFragment.refresh();
//                    }
                }
            }
            if (action.equals("update_jwdx_success")) {
                getMsgCount();
            }
            if (action.equals("update_jwdx_refuse")) {
                getMsgCount();
            }
            if (action.equals("update_contact_success")) {
                getFriendsCount();
            }
            if (action.equals("update_state_to_1_success")) {
                save("state", "1");
            }
            if(action.equals("update_state_to_2_success")){
                //交往成功
                save("state", "2");
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_message_success");
        myIntentFilter.addAction("update_jwdx_success");
        myIntentFilter.addAction("update_jwdx_refuse");
        myIntentFilter.addAction("update_contact_success");
        myIntentFilter.addAction("update_state_to_1_success");
        myIntentFilter.addAction("update_state_to_2_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


}
