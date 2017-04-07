package com.lbins.meetlove;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.fragment.FourFragment;
import com.lbins.meetlove.fragment.OneFragment;
import com.lbins.meetlove.fragment.ThreeFragment;
import com.lbins.meetlove.fragment.TwoFragment;
import com.lbins.meetlove.ui.LoginActivity;
import com.lbins.meetlove.util.GuirenHttpUtils;
import com.lbins.meetlove.util.StringUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;

    private ImageView foot_one;
    private ImageView foot_two;
    private ImageView foot_three;
    private ImageView foot_four;


    //设置底部图标
    Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        res = getResources();
        fm = getSupportFragmentManager();
        initView();

        switchFragment(R.id.foot_liner_one);

    }
    private void initView() {
        foot_one = (ImageView) this.findViewById(R.id.foot_one);
        foot_two = (ImageView) this.findViewById(R.id.foot_two);
        foot_three = (ImageView) this.findViewById(R.id.foot_three);
        foot_four = (ImageView) this.findViewById(R.id.foot_four);
        this.findViewById(R.id.foot_liner_one).setOnClickListener(this);
        this.findViewById(R.id.foot_liner_two).setOnClickListener(this);
        this.findViewById(R.id.foot_liner_three).setOnClickListener(this);
        this.findViewById(R.id.foot_liner_four).setOnClickListener(this);
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_liner_one:
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_one.setImageResource(R.drawable.tabicon_recommand_p);
                foot_two.setImageResource(R.drawable.tabicon_msg);
                foot_three.setImageResource(R.drawable.tabicon_contact);
                foot_four.setImageResource(R.drawable.tabicon_mine);

                break;
            case R.id.foot_liner_two:
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }
                foot_one.setImageResource(R.drawable.tabicon_recommand);
                foot_two.setImageResource(R.drawable.tabicon_msg_p);
                foot_three.setImageResource(R.drawable.tabicon_contact);
                foot_four.setImageResource(R.drawable.tabicon_mine);

                break;
            case R.id.foot_liner_three:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }
                foot_one.setImageResource(R.drawable.tabicon_recommand);
                foot_two.setImageResource(R.drawable.tabicon_msg);
                foot_three.setImageResource(R.drawable.tabicon_contact_p);
                foot_four.setImageResource(R.drawable.tabicon_mine);

                break;
            case R.id.foot_liner_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                foot_one.setImageResource(R.drawable.tabicon_recommand);
                foot_two.setImageResource(R.drawable.tabicon_msg);
                foot_three.setImageResource(R.drawable.tabicon_contact);
                foot_four.setImageResource(R.drawable.tabicon_mine_p);
                break;

        }
        fragmentTransaction.commit();
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

//    VersonCodeObj versionUpdateObj;
//
//    public void checkVersion() {
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.CHECK_VERSION_CODE_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code1 = jo.getString("code");
//                                if (Integer.parseInt(code1) == 200) {
//                                    VersionUpdateObjData data = getGson().fromJson(s, VersionUpdateObjData.class);
//                                    versionUpdateObj = data.getData();
//                                    if("true".equals(versionUpdateObj.getFlag())){
//                                        showVersionDialog();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        }
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        if (progressDialog != null) {
//                            progressDialog.dismiss();
//                        }
//                        Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("mm_version_code", getV());
//                params.put("mm_version_package", "com.Lbins.Mlt");
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        getRequestQueue().add(request);
//    }

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

//    private void showVersionDialog() {
//        final Dialog picAddDialog = new Dialog(MainActivity.this, R.style.dialog);
//        View picAddInflate = View.inflate(this, R.layout.dialog_new_version, null);
//        ImageView btn_sure = (ImageView) picAddInflate.findViewById(R.id.btn_sure);
//        btn_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //更新
//                final Uri uri = Uri.parse(versionUpdateObj.getDurl());
//                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
//                picAddDialog.dismiss();
//            }
//        });
//
//        //取消
//        ImageView btn_cancel = (ImageView) picAddInflate.findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                picAddDialog.dismiss();
//            }
//        });
//        picAddDialog.setContentView(picAddInflate);
//        picAddDialog.show();
//    }

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
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出幸福牵手吧",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
