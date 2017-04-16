package com.lbins.meetlove.base;

public class InternetURL {
    public static final String INTERNAL = "http://192.168.0.225:8080/";
//    public static final String INTERNAL = "http://157j1274e3.iask.in/";
//    public static final String INTERNAL =  "http://120.27.108.66:8080/";

    public static final String QINIU_URL =  "http://oo4c4r583.bkt.clouddn.com/";
    //更新链接
    public static final String UPDATE_URL =  "http://a.app.qq.com/o/simple.jsp?pkgname=com.lbins.myapp";
    //mob
    public static final String APP_MOB_KEY = "1cadf0af8fa55";
    public static final String APP_MOB_SCRECT = "0f7d21f71e2d1a02376e142298da8003";

    public static final String WEIXIN_APPID = "wx4da8b73a07135cd1";
    public static final String WEIXIN_SECRET = "a393cc92c26041c3cdad965a931ba537";
    public static final String WX_API_KEY="a393cc92c26041c3cdad965a931ba537";

    public static final String QINIU_SPACE = "meetlove-pic";
    //多媒体文件上传接口
    public static final String UPLOAD_FILE = INTERNAL + "uploadImage.do";
    public static final String UPLOAD_TOKEN = INTERNAL + "token.json";


    //1登陆
    public static final String appLogin = INTERNAL + "appLogin.do";
    //注册
    public static final String appReg = INTERNAL + "appReg.do";
    //更新头像
    public static final String appUpdateCover = INTERNAL + "appUpdateCover.do";
    //获得省份
    public static final String appProvinces = INTERNAL + "appProvinces.do";
    //获得城市
    public static final String appCitys = INTERNAL + "appCitys.do";
    //获得兴趣爱好列表
    public static final String appLikes = INTERNAL + "appLikes.do";
    //完善资料
    public static final String appUpdateProfile = INTERNAL + "appUpdateProfile.do";
    //根据兴趣爱好IDs查询爱好兴趣集合
    public static final String appLikesBylikeIds = INTERNAL + "appLikesBylikeIds.do";
    //获取公司介绍
    public static final String appAboutUs = INTERNAL + "appAboutUs.do";

    //版本检查
    public static final String getVersionCode = INTERNAL +   "getVersionCode.do";

}
