package com.my_swpu.mystudyhelper.biz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.my_swpu.mystudyhelper.util.db.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RollInfoBiz {
	private DBHelper dbHelper;
	private SQLiteDatabase db;

	public RollInfoBiz(Context context) {
		dbHelper = new DBHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public boolean insert(ArrayList<Map<String, String>> rollList, int uid) {
		if (uid == 0) {
			return false;
		}
		try {
			String sql = "INSERT INTO personal_roll (uid, key, value) " +
					"VALUES (?, ?, ?)";
			for (Map<String, String> map : rollList) {
				db.execSQL(sql, new Object[] {uid, map.get("key"), map.get("value")});
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean delete(int uid) {
		if (uid == 0) {
			return false;
		}
		try {
			String sql = "DELETE FROM personal_roll WHERE uid =" + uid;
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean update(ArrayList<Map<String, String>> rollList, int uid) {
		if (uid == 0) {
			return false;
		}
		try {
			if (query(uid) == null) {
				return insert(rollList, uid);
			}
			else {
				if (delete(uid)) {
					return insert(rollList, uid);
				}
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public ArrayList<Map<String, String>> query(int uid) {
		if (uid == 0) {
			return null;
		}
		try {
			ArrayList<Map<String, String>> rollList = new ArrayList<Map<String, String>>();
			String sql = "SELECT * FROM personal_roll WHERE uid="+uid;
			Cursor c = db.rawQuery(sql, null);
			if (c.getCount() == 0) {
				return null;
			}
			else {
				c.moveToFirst();
				while (!c.isLast()) {
					Map<String, String> tmpMap = new HashMap<String, String>();
					tmpMap.put("key", c.getString(c.getColumnIndex("key")));
					tmpMap.put("value", c.getString(c.getColumnIndex("value")));
					rollList.add(tmpMap);
					c.moveToNext();
				}
			}
			return rollList;
		} catch (Exception e) {
			return null;
		}
	}
}
