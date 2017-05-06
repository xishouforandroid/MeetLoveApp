package com.lbins.meetlove.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.OnClickContentItemListener;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.module.City;
import com.lbins.meetlove.module.HappyHandLike;
import com.lbins.meetlove.module.Province;
import com.lbins.meetlove.ui.LikesActivity;
import com.lbins.meetlove.ui.SearchPeopleActivity;
import com.lbins.meetlove.ui.SearchPeopleLikesActivity;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchOneFragment extends BaseFragment implements View.OnClickListener,OnClickContentItemListener {

    private View view;
    private Resources res;

    private TextView age;
    private TextView heightl;
    private TextView education;
    private TextView marragie;
    private TextView likes;
    //择偶要求
    private String agestart="";
    private String ageend="";
    private String heightlstart="";
    private String heightlend="";
    private String educationID2="";
    private String marragieID = "";
    private String likeids= "";

    private List<HappyHandLike> likeLists = new ArrayList<>();//兴趣爱好集合

    private Button btn_login;
    private EditText keywords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_one_fragment, null);
        res = getActivity().getResources();

        initView();

        return view;
    }

    void initView(){
        age = (TextView) view.findViewById(R.id.age);
        heightl = (TextView) view.findViewById(R.id.heightl);
        education = (TextView) view.findViewById(R.id.education);
        marragie = (TextView) view.findViewById(R.id.marragie);
        likes = (TextView) view.findViewById(R.id.likes);

        age.setOnClickListener(this);
        heightl.setOnClickListener(this);
        education.setOnClickListener(this);
        marragie.setOnClickListener(this);
        likes.setOnClickListener(this);

        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        keywords = (EditText) view.findViewById(R.id.keywords);
        keywords.addTextChangedListener(watcher);
        keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    if(!StringUtil.isNullOrEmpty(keywords.getText().toString())
                            || !StringUtil.isNullOrEmpty(agestart)
                            || !StringUtil.isNullOrEmpty(ageend)
                            || !StringUtil.isNullOrEmpty(heightlstart)
                            || !StringUtil.isNullOrEmpty(heightlend)
                            || !StringUtil.isNullOrEmpty(educationID2)
                            || !StringUtil.isNullOrEmpty(marragieID)
                            || !StringUtil.isNullOrEmpty(likeids)
                            )
                    {
//                        btn_login.setBackground(getActivity().getDrawable(R.drawable.btn_big_active));
                        btn_login.setBackgroundResource(R.drawable.btn_big_active);
                        btn_login.setTextColor(getResources().getColor(R.color.white));
                        Intent intent = new Intent(getActivity(), SearchPeopleActivity.class);
                        intent.putExtra("keywords", keywords.getText().toString());
                        intent.putExtra("agestart", agestart);
                        intent.putExtra("ageend", ageend);
                        intent.putExtra("heightlstart", heightlstart);
                        intent.putExtra("heightlend", heightlend);
                        intent.putExtra("educationID2", educationID2);
                        intent.putExtra("marragieID", marragieID);
                        intent.putExtra("likeids", likeids);
                        startActivity(intent);
                    } else {
                        btn_login.setBackgroundResource(R.drawable.btn_big_unactive);
                        btn_login.setTextColor(getResources().getColor(R.color.textColortwo));
                        Toast.makeText(getActivity(), "请选择查询条件!", Toast.LENGTH_SHORT).show();
                    }


                    return true;
                }
                return false;
            }
        });
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!StringUtil.isNullOrEmpty(keywords.getText().toString())
                    || !StringUtil.isNullOrEmpty(agestart)
                    || !StringUtil.isNullOrEmpty(ageend)
                    || !StringUtil.isNullOrEmpty(heightlstart)
                    || !StringUtil.isNullOrEmpty(heightlend)
                    || !StringUtil.isNullOrEmpty(educationID2)
                    || !StringUtil.isNullOrEmpty(marragieID)
                    || !StringUtil.isNullOrEmpty(likeids)
                    )
            {
                btn_login.setBackgroundResource(R.drawable.btn_big_active);
                btn_login.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn_login.setBackgroundResource(R.drawable.btn_big_unactive);
                btn_login.setTextColor(getResources().getColor(R.color.textColortwo));
            }
        }
    };

    private PopAgeWindow popAgeWindow;
    private PopHeightlWindow popHeightlWindow;
    private PopEducationWindow popEducationWindow;
    private PopMarryWindow popMarryWindow;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.age:
            {
                //年龄
                hiddenKeyBoard(v);
                showPopAge();
            }
                break;
            case R.id.heightl:
            {
                //身高
                hiddenKeyBoard(v);
                showPopHeightl();
            }
            break;
            case R.id.education:
            {
                //学历
                hiddenKeyBoard(v);
                showPopEducation2();
            }
            break;
            case R.id.marragie:
            {
                //婚姻状况
                hiddenKeyBoard(v);
                showPopMarry();
            }
            break;
            case R.id.likes:
            {
                //兴趣爱好
                Intent intent = new Intent(getActivity(), SearchPeopleLikesActivity.class);
                startActivityForResult(intent, 1001);
            }
            break;
            case R.id.btn_login:
            {
                if(!StringUtil.isNullOrEmpty(keywords.getText().toString())
                        || !StringUtil.isNullOrEmpty(agestart)
                        || !StringUtil.isNullOrEmpty(ageend)
                        || !StringUtil.isNullOrEmpty(heightlstart)
                        || !StringUtil.isNullOrEmpty(heightlend)
                        || !StringUtil.isNullOrEmpty(educationID2)
                        || !StringUtil.isNullOrEmpty(marragieID)
                        || !StringUtil.isNullOrEmpty(likeids)
                        ){
                    Intent intent = new Intent(getActivity(), SearchPeopleActivity.class);
                    intent.putExtra("keywords", keywords.getText().toString());
                    intent.putExtra("agestart", agestart);
                    intent.putExtra("ageend", ageend);
                    intent.putExtra("heightlstart", heightlstart);
                    intent.putExtra("heightlend", heightlend);
                    intent.putExtra("educationID2", educationID2);
                    intent.putExtra("marragieID", marragieID);
                    intent.putExtra("likeids", likeids);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "请选择查询条件!", Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1001:
            {
                if(resultCode == 1001){
                    String likeNames = (String) data.getExtras().get("likeNames");
                    likeids = (String) data.getExtras().get("likesids");
                    if(!StringUtil.isNullOrEmpty(likeNames)){
                        likes.setText(likeNames);
                    }
                    btn_login.setBackgroundResource(R.drawable.btn_big_active);
                    btn_login.setTextColor(getResources().getColor(R.color.white));
                }
            }
            break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showPopMarry(){
        popMarryWindow = new PopMarryWindow(getActivity(), itemsOnClickMarry);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popMarryWindow.setBackgroundDrawable(new BitmapDrawable());
        popMarryWindow.setFocusable(true);
        popMarryWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popMarryWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View.OnClickListener itemsOnClickMarry = new View.OnClickListener() {
        public void onClick(View v) {
            popMarryWindow.dismiss();
            btn_login.setBackgroundResource(R.drawable.btn_big_active);
            btn_login.setTextColor(getResources().getColor(R.color.white));
            switch (v.getId()) {
                case R.id.btn1: {
                    marragie.setText("未婚");
                    marragieID = "1";
                }
                break;
                case R.id.btn2: {
                    marragie.setText("离异");
                    marragieID = "2";
                }
                break;
                case R.id.btn3: {
                    marragie.setText("丧偶");
                    marragieID = "3";
                }
                break;
                default:
                    break;
            }
        }
    };

    public void showPopEducation2(){
        popEducationWindow = new PopEducationWindow(getActivity(), itemsOnClickEducation2);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popEducationWindow.setBackgroundDrawable(new BitmapDrawable());
        popEducationWindow.setFocusable(true);
        popEducationWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popEducationWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View.OnClickListener itemsOnClickEducation2 = new View.OnClickListener() {
        public void onClick(View v) {
            popEducationWindow.dismiss();
            btn_login.setBackgroundResource(R.drawable.btn_big_active);
            btn_login.setTextColor(getResources().getColor(R.color.white));
            switch (v.getId()) {
                case R.id.btn2: {
                    education.setText("专科以下");
                    educationID2 = "2";
                }
                break;
                case R.id.btn3: {
                    education.setText("专科");
                    educationID2 = "3";
                }
                break;
                case R.id.btn4: {
                    education.setText("本科");
                    educationID2 = "4";
                }
                break;
                case R.id.btn5: {
                    education.setText("研究生及以上");
                    educationID2 = "5";
                }
                break;
                default:
                    break;
            }
        }
    };



    private List<String> arrays1 = new ArrayList<String>();
    private List<String> arrays2 = new ArrayList<String>();

    public void showPopAge(){
        arrays1.add("不限");
        arrays1.add("1970");
        arrays1.add("1971");
        arrays1.add("1972");
        arrays1.add("1973");
        arrays1.add("1974");
        arrays1.add("1975");
        arrays1.add("1976");
        arrays1.add("1977");
        arrays1.add("1978");
        arrays1.add("1979");
        arrays1.add("1980");
        arrays1.add("1981");
        arrays1.add("1982");
        arrays1.add("1983");
        arrays1.add("1984");
        arrays1.add("1985");
        arrays1.add("1986");
        arrays1.add("1987");
        arrays1.add("1988");
        arrays1.add("1989");
        arrays1.add("1990");
        arrays1.add("1991");
        arrays1.add("1992");
        arrays1.add("1993");
        arrays1.add("1994");
        arrays1.add("1995");
        arrays1.add("1996");
        arrays1.add("1997");
        arrays1.add("1998");
        arrays1.add("1999");

        arrays2.add("不限");
        arrays2.add("1999");
        arrays2.add("1998");
        arrays2.add("1997");
        arrays2.add("1996");
        arrays2.add("1995");
        arrays2.add("1994");
        arrays2.add("1993");
        arrays2.add("1992");
        arrays2.add("1991");
        arrays2.add("1990");
        arrays2.add("1989");
        arrays2.add("1988");
        arrays2.add("1987");
        arrays2.add("1986");
        arrays2.add("1985");
        arrays2.add("1984");
        arrays2.add("1983");
        arrays2.add("1982");
        arrays2.add("1981");
        arrays2.add("1980");
        arrays2.add("1979");
        arrays2.add("1978");
        arrays2.add("1977");
        arrays2.add("1976");
        arrays2.add("1975");
        arrays2.add("1974");
        arrays2.add("1973");
        arrays2.add("1972");
        arrays2.add("1971");
        arrays2.add("1970");

        popAgeWindow = new PopAgeWindow(getActivity() ,arrays1 , arrays2);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popAgeWindow.setOnClickContentItemListener(this);

        popAgeWindow.setBackgroundDrawable(new BitmapDrawable());
        popAgeWindow.setFocusable(true);
        popAgeWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popAgeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = (getActivity()).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        (getActivity()).getWindow().setAttributes(lp);
    }


    private List<String> arrays3 = new ArrayList<String>();
    private List<String> arrays4 = new ArrayList<String>();

    public void showPopHeightl(){
        arrays3.add("不限");
        arrays3.add("150");
        arrays3.add("151");
        arrays3.add("152");
        arrays3.add("153");
        arrays3.add("154");
        arrays3.add("155");
        arrays3.add("156");
        arrays3.add("157");
        arrays3.add("158");
        arrays3.add("159");
        arrays3.add("160");
        arrays3.add("161");
        arrays3.add("162");
        arrays3.add("153");
        arrays3.add("163");
        arrays3.add("164");
        arrays3.add("165");
        arrays3.add("166");
        arrays3.add("167");
        arrays3.add("168");
        arrays3.add("169");
        arrays3.add("170");
        arrays3.add("171");
        arrays3.add("172");
        arrays3.add("173");
        arrays3.add("174");
        arrays3.add("175");
        arrays3.add("176");
        arrays3.add("177");
        arrays3.add("178");
        arrays3.add("179");
        arrays3.add("180");
        arrays3.add("181");
        arrays3.add("182");
        arrays3.add("183");
        arrays3.add("184");
        arrays3.add("185");
        arrays3.add("186");
        arrays3.add("187");
        arrays3.add("188");
        arrays3.add("189");
        arrays3.add("190");


        arrays4.add("不限");
        arrays4.add("190");
        arrays4.add("189");
        arrays4.add("188");
        arrays4.add("187");
        arrays4.add("186");
        arrays4.add("185");
        arrays4.add("184");
        arrays4.add("183");
        arrays4.add("182");
        arrays4.add("181");
        arrays4.add("180");
        arrays4.add("179");
        arrays4.add("178");
        arrays4.add("177");
        arrays4.add("176");
        arrays4.add("175");
        arrays4.add("174");
        arrays4.add("173");
        arrays4.add("172");
        arrays4.add("171");
        arrays4.add("170");
        arrays4.add("169");
        arrays4.add("168");
        arrays4.add("167");
        arrays4.add("166");
        arrays4.add("165");
        arrays4.add("164");
        arrays4.add("163");
        arrays4.add("162");
        arrays4.add("161");
        arrays4.add("160");
        arrays4.add("159");
        arrays4.add("158");
        arrays4.add("157");
        arrays4.add("156");
        arrays4.add("155");
        arrays4.add("154");
        arrays4.add("153");
        arrays4.add("152");
        arrays4.add("151");
        arrays4.add("150");


        popHeightlWindow = new PopHeightlWindow(getActivity() ,arrays3 , arrays4);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popHeightlWindow.setOnClickContentItemListener(this);

        popHeightlWindow.setBackgroundDrawable(new BitmapDrawable());
        popHeightlWindow.setFocusable(true);
        popHeightlWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popHeightlWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 0:
            {
                popAgeWindow.dismiss();
                btn_login.setBackgroundResource(R.drawable.btn_big_active);
                btn_login.setTextColor(getResources().getColor(R.color.white));
                String ageStr = (String) object;
                if(!StringUtil.isNullOrEmpty(ageStr)){
                    String[] arrs = ageStr.split(",");
                    if(arrs != null){
                        if(arrs.length > 1){
                            age.setText(arrs[0] + "-" + arrs[1]);
                            if("不限".equals(arrs[0])){
                                agestart = "1970";
                            }else {
                                agestart = arrs[0];
                            }
                            if("不限".equals(arrs[1])){
                                ageend = "1999";
                            }else {
                                ageend = arrs[1];
                            }
                        }
                    }
                }
            }
            break;
            case 1:
            {
                popHeightlWindow.dismiss();
                btn_login.setBackgroundResource(R.drawable.btn_big_active);
                btn_login.setTextColor(getResources().getColor(R.color.white));
                String ageStr = (String) object;
                if(!StringUtil.isNullOrEmpty(ageStr)){
                    String[] arrs = ageStr.split(",");
                    if(arrs != null){
                        if(arrs.length > 1){
                            heightl.setText(arrs[0] + "-" + arrs[1]);
                            if("不限".equals(arrs[0])){
                                heightlstart = "150";
                            }else {
                                heightlstart = arrs[0];
                            }
                            if("不限".equals(arrs[1])){
                                heightlend = "190";
                            }else {
                                heightlend = arrs[1];
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    void hiddenKeyBoard(View v){
         /*隐藏软键盘*/
        InputMethodManager imm = (InputMethodManager) v
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(
                    v.getApplicationWindowToken(), 0);
        }
    }
}
