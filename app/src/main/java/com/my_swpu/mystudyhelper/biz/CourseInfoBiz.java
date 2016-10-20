package com.my_swpu.mystudyhelper.biz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my_swpu.mystudyhelper.entity.CourseEntity;
import com.my_swpu.mystudyhelper.util.db.DBHelper;

/**
 * Created by dsx on 2016/2/19 0019.
 */
public class CourseInfoBiz {
    private DBHelper globalDBHelper;
    private SQLiteDatabase db;

    public CourseInfoBiz(Context context) {
        globalDBHelper = new DBHelper(context);
        db = globalDBHelper.getWritableDatabase();
    }

    public int insert(CourseEntity cInfo) {
        try {
            // 如果找到相同（时间地点相同）课程，更新信息
            String sql1 = "SELECT cid FROM course_info WHERE courseid=? AND num=? AND weekfrom=? AND weekto=? AND weektype=? " +
                    "AND day=? AND lesson_from=? AND lesson_to=? AND campus=? AND bld=? AND place=?";
            Cursor c = db.rawQuery(sql1, new String[]{
                    cInfo.getCourseid(),
                    cInfo.getNum(),
                    cInfo.getWeekfrom()+"",
                    cInfo.getWeekto()+"",
                    cInfo.getWeektype()+"",
                    cInfo.getDay()+"",
                    cInfo.getLessonfrom()+"",
                    cInfo.getLessonto()+"",
                    cInfo.getCampus(),
                    cInfo.getBld(),
                    cInfo.getPlace()
            });
            if (c.getCount() != 0) {
                c.moveToFirst();
                int cid = c.getInt(c.getColumnIndex("cid"));
                String sql2 = "UPDATE course_info SET name=?,credit=?,attr=?,exam=?,teacher=? WHERE cid=?";
                db.execSQL(sql2, new Object[] {
                        cInfo.getName(),
                        cInfo.getCredit(),
                        cInfo.getAttr(),
                        cInfo.getExam(),
                        cInfo.getTeacher(),
                        cid
                });
                return cid;
            }
            // 否则插入课程
            String sql = "INSERT INTO course_info (cid,weekfrom,weekto,weektype,day,lesson_from,lesson_to,courseid,num,name,credit,attr,exam,teacher,campus,bld,place) " +
                    "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[] {
                    cInfo.getWeekfrom(),
                    cInfo.getWeekto(),
                    cInfo.getWeektype(),
                    cInfo.getDay(),
                    cInfo.getLessonfrom(),
                    cInfo.getLessonto(),
                    cInfo.getCourseid(),
                    cInfo.getNum(),
                    cInfo.getName(),
                    cInfo.getCredit(),
                    cInfo.getAttr(),
                    cInfo.getExam(),
                    cInfo.getTeacher(),
                    cInfo.getCampus(),
                    cInfo.getBld(),
                    cInfo.getPlace()
            });
            Cursor c1 = db.rawQuery("SELECT last_insert_rowid()", null);
            c1.moveToFirst();
            return c1.getInt(c1.getColumnIndex("last_insert_rowid()"));
        } catch (Exception e) {
            return 0;
        }

    }

    public Cursor select(){
        Cursor cursor = db.query("course_info", null, null, null, null, null, null);
        return cursor;
    }

    public boolean delete(int cid) {
        try {
            String sql = "DELETE FROM course_info WHERE cid=?";
            db.execSQL(sql, new Object[] {cid});
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
