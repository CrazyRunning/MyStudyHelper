package com.my_swpu.mystudyhelper.service;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

public class SetQuietService extends Service {

    //保存时间，temp[day][row][time]表示第day+1个tab选项卡中的第row+1个行中第time+1个表示时间的字符串
//    String[][][] temp = new String[7][12][2];

    //取得数据库，并定义保存每张表数据的cursor集合
//    DBHelper db = new DBHelper(SetQuietService.this);
//    private LinkedList<CourseEntity> courseInfoList;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //声明一个获取系统音频服务的类的对象
        final AudioManager audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        //获取手机之前设置好的铃声模式
        final int orgRingerMode = audioManager.getRingerMode();
//        courseInfoList = new LinkedList<>();
//        //每隔一分钟从数据库中取以此数据，获得一次当前的时间，并与用用户输入的上下课时间比较，如果相等，则执行相应的静音或者恢复铃声操作
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Cursor cursor = db.select();
//                if(cursor.moveToFirst()) {
//                    for (int i = 0; i < cursor.getCount(); i++) {
//                        cursor.move(i);
////                        Log.i("本地课表", cursor.getString(cursor.getColumnIndex("name")) + "\n");
//                        CourseEntity courseEntity = new CourseEntity();
//                        courseEntity.setWeekfrom(cursor.getInt(cursor.getColumnIndex("weekfrom")));
//                        courseEntity.setWeekto(cursor.getInt(cursor.getColumnIndex("weekto")));
//                        courseEntity.setWeektype(cursor.getInt(cursor.getColumnIndex("weektype")));
//                        courseEntity.setDay(cursor.getInt(cursor.getColumnIndex("day")));
//                        courseEntity.setLessonfrom(cursor.getInt(cursor.getColumnIndex("lesson_from")));
//                        courseEntity.setLessonto(cursor.getInt(cursor.getColumnIndex("lesson_to")));
//                        courseEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        courseEntity.setCredit(cursor.getString(cursor.getColumnIndex("credit")));
//                        courseEntity.setAttr(cursor.getString(cursor.getColumnIndex("attr")));
//                        courseEntity.setExam(cursor.getString(cursor.getColumnIndex("exam")));
//                        courseEntity.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
//                        courseEntity.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
//                        courseEntity.setBld(cursor.getString(cursor.getColumnIndex("bld")));
//                        courseEntity.setPlace(cursor.getString(cursor.getColumnIndex("place")));
//                        courseInfoList.add(courseEntity);
//                    }
//                }
//                int currentDay = 0;
//                for(int i = 0; i < courseInfoList.size(); i++){
//                    //获取当前的是星期几
////                    if(courseInfoList.get(i).get)
//                }
//
//
//                for (int j = 0; j < 12; j++) {
//                    //获取手机当前的铃声模式
//                    int currentRingerMode = audioManager.getRingerMode();
//
//                }
//
//            }
//        }, 0, 60000);
        int currentRingerMode = audioManager.getRingerMode();
        if (currentRingerMode != AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
        if (currentRingerMode == AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setRingerMode(orgRingerMode);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
