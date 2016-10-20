package com.my_swpu.mystudyhelper.biz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my_swpu.mystudyhelper.entity.CourseData;
import com.my_swpu.mystudyhelper.entity.CourseEntity;
import com.my_swpu.mystudyhelper.util.db.DBHelper;

import java.util.LinkedList;

/**
 * Created by dsx on 2016/2/19 0019.
 */
public class CourseDataBiz {
    private DBHelper globalDBHelper;
    private SQLiteDatabase db;

    public CourseDataBiz(Context context) {
        globalDBHelper = new DBHelper(context);
        db = globalDBHelper.getWritableDatabase();
    }

    public boolean insert(CourseData courseData) {
        try {
            String sql = "INSERT INTO course_data (cid, uid) VALUES (?, ?)";
            db.execSQL(sql, new Object[] {
                    courseData.getCid(),
                    courseData.getUid()
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int cid) {
        try {
            String sql = "DELETE FROM course_data WHERE cid=?";
            db.execSQL(sql, new Object[] {cid});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clear(int uid) {
        try {
            String sql = "DELETE FROM course_data WHERE uid=?";
            db.execSQL(sql, new Object[] {uid});
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // 查询某用户的所有课程
    public LinkedList<CourseEntity> query(int uid) {
        try {
            LinkedList<CourseEntity> cInfos = new LinkedList<CourseEntity>();

            String sql = "SELECT * FROM course_info WHERE cid IN (SELECT cid FROM course_data WHERE uid=?)";
            Cursor c = db.rawQuery(sql, new String[]{String.valueOf(uid)});

            while (c.moveToNext()) {
                CourseEntity cInfo = new CourseEntity();
                cInfo.setWeekfrom(c.getInt(c.getColumnIndex("weekfrom")));
                cInfo.setWeekto(c.getInt(c.getColumnIndex("weekto")));
                cInfo.setWeektype(c.getInt(c.getColumnIndex("weektype")));
                cInfo.setDay(c.getInt(c.getColumnIndex("day")));
                cInfo.setLessonfrom(c.getInt(c.getColumnIndex("lesson_from")));
                cInfo.setLessonto(c.getInt(c.getColumnIndex("lesson_to")));
                cInfo.setCourseid(c.getString(c.getColumnIndex("courseid")));
                cInfo.setNum(c.getString(c.getColumnIndex("num")));
                cInfo.setName(c.getString(c.getColumnIndex("name")));
                cInfo.setCredit(c.getString(c.getColumnIndex("credit")));
                cInfo.setAttr(c.getString(c.getColumnIndex("attr")));
                cInfo.setExam(c.getString(c.getColumnIndex("exam")));
                cInfo.setTeacher(c.getString(c.getColumnIndex("teacher")));
                cInfo.setCampus(c.getString(c.getColumnIndex("campus")));
                cInfo.setBld(c.getString(c.getColumnIndex("bld")));
                cInfo.setPlace(c.getString(c.getColumnIndex("place")));
                cInfos.add(cInfo);
            }

            return cInfos;
        } catch (Exception e) {
            return null;
        }

    }
}
