package com.lbins.meetlove.base;

public class InternetURL {
//    public static final String INTERNAL = "http://157j1274e3.iask.in/";
    public static final String INTERNAL =  "http://120.27.108.66:8080/";

    public static final String QINIU_URL =  "http://7xt74j.com1.z0.glb.clouddn.com/";
    //mob
    public static final String APP_MOB_KEY = "1cadf0af8fa55";
    public static final String APP_MOB_SCRECT = "0f7d21f71e2d1a02376e142298da8003";

    public static final String WEIXIN_APPID = "wx4da8b73a07135cd1";
    public static final String WEIXIN_SECRET = "a393cc92c26041c3cdad965a931ba537";
    public static final String WX_API_KEY="a393cc92c26041c3cdad965a931ba537";

    public static final String QINIU_SPACE = "paopao-pic";
    //多媒体文件上传接口
    public static final String UPLOAD_FILE = INTERNAL + "uploadImage.do";
    public static final String UPLOAD_TOKEN = INTERNAL + "token.json";
    //1登陆
    public static final String LOGIN__URL = INTERNAL + "memberLogin.do";
    //2根据用户id获得用户信息
    public static final String GET_MEMBER_URL = INTERNAL + "getMemberInfoById.do";
    //3获得所有省份
    public static final String GET_PROVINCE_URL = INTERNAL + "getProvince.do";
    //4获得城市
    public static final String GET_CITY_URL = INTERNAL + "getCity.do";
    //5获得地区
    public static final String GET_COUNTRY_URL = INTERNAL + "getCountry.do";
    //6注册
    public static final String REG_URL = INTERNAL + "memberRegister.do";

    //版本检查
    public static final String CHECK_VERSION_CODE_URL = INTERNAL +   "getVersionCode.do";

}
