package com.lbins.meetlove.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.*;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.NetUtils;
import com.lbins.meetlove.MainActivity;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.ItemMessageAdapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.chat.Constant;
import com.lbins.meetlove.chat.db.InviteMessgeDao;
import com.lbins.meetlove.chat.ui.ChatActivity;
import com.lbins.meetlove.dao.DBHelper;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.dao.HappyHandJw;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.data.HappyHandJwDatas;
import com.lbins.meetlove.library.PullToRefreshBase;
import com.lbins.meetlove.library.PullToRefreshListView;
import com.lbins.meetlove.ui.MineMsgActivity;
import com.lbins.meetlove.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class TwoFragment  extends EaseConversationListFragment {

    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(),R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    if(conversation.getType() == EMConversation.EMConversationType.Chat){
                        //如果是单聊的话 判断是否您在交往状态  对方是否在交往状态
                        Emp empT = DBHelper.getInstance(getActivity()).getEmpById(username);

                        if("2".equals(getGson().fromJson(getSp().getString("state", ""), String.class))){
//                            showDialogMsg("对方不是你的交往对象，不能发消息");
//                            return;
                            getJwdx1(username);
                        }else {
                            if(empT != null && !StringUtil.isNullOrEmpty(empT.getState())){
                                if("2".equals(empT.getState())){
//                                    showDialogMsg("对方已有交往对象，不能发消息");
                                    getJwdx2(username);
                                }else {
                                    getEmpById(username);
                                }
                            }else{
                                getEmpById(username);
                            }
                        }
                    }else {
                        // start chat acitivity
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        if(conversation.isGroup()){
                            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                                // it's group chat
                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                            }else{
                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                            }

                        }
                        // it's single chat
                        intent.putExtra(Constant.EXTRA_USER_ID, username);
                        startActivity(intent);
                    }

                }
            }
        });

        //red packet code : 红包回执消息在会话列表最后一条消息的展示
//        conversationListView.setConversationListHelper(new EaseConversationList.EaseConversationListHelper() {
//            @Override
//            public String onSetItemSecondaryText(EMMessage lastMessage) {
//                if (lastMessage.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    String sendNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
//                    String receiveNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
//                    String msg;
//                    if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                        msg = String.format(getResources().getString(R.string.msg_someone_take_red_packet), receiveNick);
//                    } else {
//                        if (sendNick.equals(receiveNick)) {
//                            msg = getResources().getString(R.string.msg_take_red_packet);
//                        } else {
//                            msg = String.format(getResources().getString(R.string.msg_take_someone_red_packet), sendNick);
//                        }
//                    }
//                    return msg;
//                }
//                return null;
//            }
//        });
        super.setUpView();
        //end of red packet code
    }


    //查询我的交往对象
    List<HappyHandJw> listjwdxs = new ArrayList<>();
    private void getJwdx1(final String username) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appJiaowangs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandJwDatas data = getGson().fromJson(s, HappyHandJwDatas.class);
                                    listjwdxs.clear();
                                    listjwdxs.addAll(data.getData());
                                    if(listjwdxs != null && listjwdxs.size()>0){
                                        HappyHandJw happyHandJw = listjwdxs.get(0);
                                        if(happyHandJw != null){
                                            if(!username.equals(happyHandJw.getEmpid2())){
                                                showDialogMsg("对方不是你的交往对象，不能发消息");
                                            }else{
                                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
                                                intent.putExtra(Constant.EXTRA_USER_ID, username);
                                                startActivity(intent);
                                            }
                                        }
                                    }
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
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("is_check", "1");

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


    //查询对方的交往对象
    List<HappyHandJw> listjwdxs1 = new ArrayList<>();
    private void getJwdx2(final String username) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appJiaowangs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandJwDatas data = getGson().fromJson(s, HappyHandJwDatas.class);
                                    listjwdxs1.clear();
                                    listjwdxs1.addAll(data.getData());
                                    if(listjwdxs1 != null && listjwdxs1.size()>0){
                                        HappyHandJw happyHandJw = listjwdxs1.get(0);
                                        if(happyHandJw != null){
                                            if(!getGson().fromJson(getSp().getString("empid", ""), String.class).equals(happyHandJw.getEmpid2())){
                                                showDialogMsg("对方已有交往对象，不能发消息");
                                            }else{
                                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
                                                intent.putExtra(Constant.EXTRA_USER_ID, username);
                                                startActivity(intent);
                                            }
                                        }
                                    }
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
                params.put("empid1", username);
                params.put("is_check", "1");

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



    private void getEmpById(final String empid) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpByEmpId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    if(data != null){
                                        Emp emp = data.getData();
                                        if(emp != null){
                                            DBHelper.getInstance(getActivity()).saveEmp(emp);
                                            if("2".equals(emp.getState())){
                                                showDialogMsg("对方已有交往对象，不能发消息");
                                            }else {
                                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                intent.putExtra(Constant.EXTRA_USER_ID, empid);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("empid", empid);
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


    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    private void showDialogMsg(String msgStr) {
        final Dialog picAddDialog = new Dialog(getActivity(), R.style.dialog);
        View picAddInflate = View.inflate(getActivity(), R.layout.msg_msg_dialog, null);
        final TextView msg = (TextView) picAddInflate.findViewById(R.id.msg);
        msg.setText(msgStr);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if(tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }


}
