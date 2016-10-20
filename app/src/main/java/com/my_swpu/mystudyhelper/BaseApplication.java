package com.my_swpu.mystudyhelper;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.my_swpu.mystudyhelper.activity.LoginActivity;
import com.my_swpu.mystudyhelper.biz.CourseInfoBiz;
import com.my_swpu.mystudyhelper.entity.CourseEntity;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.LocationService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dsx on 2015/12/7 0007.
 */
public class BaseApplication extends Application {
    public static BaseApplication instance;
    /**
     * 全局Activity管理
     */
    public static List<Activity> activities = new ArrayList<Activity>();
    public LocationService locationService;
    public Vibrator mVibrator;
    public List<CourseEntity> others;
    private  boolean isLocal = false;
    private LinkedList<CourseEntity> courseInfoList;
    private CourseInfoBiz cInfoDao;
    /**
     * 选择高校
     */
    private int university = 0;

    /**
     * 当前周
     */
    public int currentWeek = 0;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();  //不能删除
        instance = this;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext());
        courseInfoList = new LinkedList<>();
        try {
            cInfoDao = new CourseInfoBiz(this);
            Cursor cursor = cInfoDao.select();
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.move(i);
                    Log.i("本地课表", cursor.getString(cursor.getColumnIndex("name")) + "\n");
                    CourseEntity courseEntity = new CourseEntity();
                    courseEntity.setWeekfrom(cursor.getInt(cursor.getColumnIndex("weekfrom")));
                    courseEntity.setWeekto(cursor.getInt(cursor.getColumnIndex("weekto")));
                    courseEntity.setWeektype(cursor.getInt(cursor.getColumnIndex("weektype")));
                    courseEntity.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                    courseEntity.setLessonfrom(cursor.getInt(cursor.getColumnIndex("lesson_from")));
                    courseEntity.setLessonto(cursor.getInt(cursor.getColumnIndex("lesson_to")));
                    courseEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
                    courseEntity.setCredit(cursor.getString(cursor.getColumnIndex("credit")));
                    courseEntity.setAttr(cursor.getString(cursor.getColumnIndex("attr")));
                    courseEntity.setExam(cursor.getString(cursor.getColumnIndex("exam")));
                    courseEntity.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                    courseEntity.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
                    courseEntity.setBld(cursor.getString(cursor.getColumnIndex("bld")));
                    courseEntity.setPlace(cursor.getString(cursor.getColumnIndex("place")));
                    courseInfoList.add(courseEntity);
                }
            }
            cursor.close();
        }catch (Exception e0){

        }
    }

    public void setIsLocal(boolean isLocal){
        this.isLocal = isLocal;
    }

    public boolean getIsLocation(){
        return isLocal;
    }

    public void setCurrentWeek(int currentWeek){
        this.currentWeek = currentWeek;
    }

    public int getCurrentWeek(){
        return this.currentWeek;
    }

    public static BaseApplication getInstance(){
        if(instance == null){
            instance = new BaseApplication();
        }
        return instance;
    }

    public void initOthers(){
        if(others == null){
            others = new ArrayList<>();
        }
    }

    public List<CourseEntity> getOthers(){
        if(others == null){
            initOthers();
        }
        return this.others;
    }

    public LinkedList<CourseEntity> getCourseInfoList() {
        if(courseInfoList == null){
            courseInfoList = new LinkedList<>();
        }
        return courseInfoList;
    }

    public void setCourseInfoList(LinkedList<CourseEntity> courseInfoList) {
        this.courseInfoList = courseInfoList;
    }

    public int getUniversity() {
        return university;
    }

    public void setUniversity(int university) {
        this.university = university;
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void logout(){
        if(activities != null){
            int i = 1;
            for(Activity activity : activities){
                if(i < activities.size() - 1){
                    activity.finish();
                    i++;
                }
            }
        }
    }

    public static void exit() {
        // 关闭所有Activity
        if (activities != null) {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            activities.clear();
            activities = new ArrayList<>();
        }
        // 彻底退出
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    /**
     * session失效
     */
    public static void removeActivies(){
        // 关闭所有Activity
        if (activities != null) {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            activities.clear();
            activities = new ArrayList<>();
        }
    }

}
