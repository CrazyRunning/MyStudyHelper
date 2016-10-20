package com.my_swpu.mystudyhelper.biz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.util.db.DBHelper;

/**
 * Created by dsx on 2016/2/15 0015.
 */
public class UserDataBiz {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public UserDataBiz(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 插入一条用户信息
     * 如果用户学号已存在，则更新数据
     * @param uData 用户信息
     * @return 本次插入的用户的UID
     */
    public int insert(UserEntity uData) {
        try {
            UserEntity uDataNew = query(uData.getNum());
            if ( uDataNew != null) {
                uData.setUid(uDataNew.getUid());
                if(update(uData)) {
                    return uData.getUid();
                }
                else {
                    return 0;
                }
            }
            String sql = "INSERT INTO user_data (uid, num, passwd, session, lastlogin, lastlogout, savepasswd, autologin, headshot) " +
                    "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[] {
                    uData.getNum(),
                    uData.getPasswd(),
                    uData.getSession(),
                    uData.getLastlogin(),
                    uData.getLastlogout(),
                    uData.getSavepasswd(),
                    uData.getAutologin(),
                    uData.getHeadshot()
            });
            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            return c.getInt(c.getColumnIndex("last_insert_rowid()"));
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 按照UID删除用户记录
     * @param uid
     * @return 删除成功返回true
     */
    public boolean delete(int uid) {
        try {
            String sql = "DELETE FROM user_data WHERE uid = ?";
            db.execSQL(sql, new Object[] {uid});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 按照学号删除用户记录
     * @param num
     * @return 删除成功返回true
     */
    public boolean delete(String num) {
        try {
            String sql = "DELETE FROM user_data WHERE num = ?";
            db.execSQL(sql, new Object[] {num});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修改用户信息
     * @param uData
     * @return 如果参数为空或者uid为空都返回false
     */
    public boolean update(UserEntity uData) {
        if (uData == null) {
            return false;
        }
        if (uData.getUid() == 0) {
            return false;
        }
        try {
            String sql = "UPDATE user_data SET num=?, passwd=?, session=?, lastlogin=?, lastlogout=?, savepasswd=?, " +
                    "autologin=?, headshot=? WHERE uid=?";
            db.execSQL(sql, new Object[] {
                    uData.getNum(),
                    uData.getPasswd(),
                    uData.getSession(),
                    uData.getLastlogin(),
                    uData.getLastlogout(),
                    uData.getSavepasswd(),
                    uData.getAutologin(),
                    uData.getHeadshot(),
                    uData.getUid()
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过UID查找用户信息
     * @param uid
     * @return 用户信息
     */
    public UserEntity query(int uid) {
        try {
            UserEntity uData = new UserEntity();
            String sql = "SELECT * FROM user_data WHERE uid="+uid;
            Cursor c = db.rawQuery(sql, null);
            if (c.getCount() == 0) {
                return null;
            }
            else {
                c.moveToFirst();
                uData.setUid(uid);
                uData.setNum(c.getString(c.getColumnIndex("num")));
                uData.setPasswd(c.getString(c.getColumnIndex("passwd")));
                uData.setSession(c.getString(c.getColumnIndex("session")));
                uData.setLastlogin(c.getInt(c.getColumnIndex("lastlogin")));
                uData.setLastlogout(c.getInt(c.getColumnIndex("lastlogout")));
                uData.setSavepasswd(c.getInt(c.getColumnIndex("savepasswd")));
                uData.setAutologin(c.getInt(c.getColumnIndex("autologin")));
                uData.setHeadshot(c.getString(c.getColumnIndex("headshot")));
                return uData;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过学号查找用户信息
     * @param num
     * @return 用户信息
     */
    public UserEntity query(String num) {
        try {
            UserEntity uData = new UserEntity();
            String sql = "SELECT * FROM user_data WHERE num="+num;
            Cursor c = db.rawQuery(sql, null);
            if (c.getCount() == 0) {
                return null;
            }
            else {
                c.moveToFirst();
                uData.setUid(c.getInt(c.getColumnIndex("uid")));
                uData.setNum(c.getString(c.getColumnIndex("num")));
                uData.setPasswd(c.getString(c.getColumnIndex("passwd")));
                uData.setSession(c.getString(c.getColumnIndex("session")));
                uData.setLastlogin(c.getInt(c.getColumnIndex("lastlogin")));
                uData.setLastlogout(c.getInt(c.getColumnIndex("lastlogout")));
                uData.setSavepasswd(c.getInt(c.getColumnIndex("savepasswd")));
                uData.setAutologin(c.getInt(c.getColumnIndex("autologin")));
                uData.setHeadshot(c.getString(c.getColumnIndex("headshot")));
                return uData;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
