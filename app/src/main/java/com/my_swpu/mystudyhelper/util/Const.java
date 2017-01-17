package com.my_swpu.mystudyhelper.util;

/**
 * 常量类
 * Created by dsx on 2015/12/16 0016.
 */
public class Const {
    private static int EVENTCODE = 0;
    public static double LATITUDE_OF_MY_LOCATION;                      //纬度
    public static double LONGITUDE_OF_MY_LOCATION;                     //经度
    public static String BAIDU_API_KEY = "4bnt9GVp1V7hyqDBugqoP9lK";
    public static final int GET_MY_LOCAZTION_EVENT = EVENTCODE++;      //定位
    public static final String API_KEY = "你的apiKey";  //api key
    public static final String KEY_1 = "你的apiKey";
    public static final String KEY_STATUS = "KEY_STATUS";
    public static final String KEY_DATA = "KEY_DATA";
    //新闻
    public static final String ACTION_GET_NEWS = "ACTION_GET_NEWS";
    public static final String GET_NEWS_SUCCESS = "GET_NEWS_SUCCESS";
    public static final String GET_NEWS_FAILURE = "GET_NEWS_FAILURE";
    public static final int GET_NEWS_ACTION = EVENTCODE++;
    public static final int GET_NEWS_INFO_ACTION = EVENTCODE++;
    //电影
    public static final String ACTION_GET_MOVIE = "ACTION_GET_MOVIE";
    public static final String GET_MOVIE_SUCCESS = "GET_MOVIE_SUCCESS";
    public static final int GET_MOVIES_ACTION = EVENTCODE++;
    public static final int LOGIN_EVENT = EVENTCODE++;
    //笑话
    public static final String ACTION_GET_JOKE = "ACTION_GET_JOKE";
    public static final String GET_JOKE_SUCCESS = "GET_JOKE_SUCCESS";
    public static final int GET_JOKES_ACTION = EVENTCODE++;
    //图灵
    public static final  String TURING_MESSAGE = "TURING_MESSAGE";
    public static final  int TURING_TEXT = 100000;
    public final static String APIKEY = "你的apiKey";    //以前的api key
    public static final String KEY = "你的apiKey";       //key
    public final static String USERID = "eb2edb736";    //
    //个人信息
    public static final int PERSONALINFO_EVENT = EVENTCODE++;
    //成绩绩点
    public static final int GET_GRADE_LIST_ACTION = EVENTCODE++;
    //我的课表
    public static final int GET_COURSE_LIST_ACTION = EVENTCODE++;
    //考试信息
    public static final int GET_EXAM_INFO_ACTION = EVENTCODE++;
    //翻译
    public static final String TRANSLATW_ACTION = "TRANSLATW_ACTION";
    public static final int TRANSLATE_EVENT= EVENTCODE++;
    public static final String APPID = "你的appId";  //百度反意思的appId
    public static final String TRANSLATE_PASSWORD = "你的apiKey";  //百度翻译的apikey
    //培养方案
    public static final int GET_PLANS_ACTION = EVENTCODE++;
    //修改密码
    public static final int CHANGE_PASSWORD_ACTION = EVENTCODE++;
    //高校
    public static final int SWPU = 1;   //西南石油大学
    public static final int SCU = 2;    //四川大学
}
