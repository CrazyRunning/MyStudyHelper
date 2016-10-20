package com.my_swpu.mystudyhelper.util.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "swpu_db.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		//CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE IF NOT EXISTS global_info" +		// 全局信息表
				"(key TEXT PRIMARY KEY, " +
				"value TEXT)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS global_option" +		// 全局选项表
				"(key TEXT PRIMARY KEY, " +
				"value TEXT)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS global_url" +		// 全局URL表
				"(key TEXT PRIMARY KEY, " +
				"value TEXT)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS user_data" +			// 用户信息表
				"(uid INTEGER PRIMARY KEY AUTOINCREMENT, " +		// 用户ID
				"num TEXT, " +										// 学号
				"passwd TEXT, " +									// 密码
				"session TEXT, " +									// SESSION信息
				"lastlogin INTEGER, " +								// 上次登录时间
				"lastlogout INTEGER, " +							// 上次注销时间
				"savepasswd INTEGER, " +							// 是否保存密码
				"autologin INTEGER, " +								// 是否自动登录
				"headshot TEXT)"									// 头像
				);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS personal_info" +		// 个人信息表
				"(uid INTEGER PRIMARY KEY, " +		// 用户ID
				"sid INTEGER, "+ 					// 学院ID
				"name TEXT, " +						// 姓名
				"days INTEGER, " +					// 大学过去天数
				"percent TEXT, " +					// 方案完成度
				"avarage TEXT, " +					// 必修平均分
				"gpa TEXT )"						// 平均绩点
				);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS personal_roll" +		// 学籍信息表
				"(uid INTEGER , " +					// 用户ID
				"key TEXT , " +						// 项目
				"value TEXT)"						// 值
				);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS course_info" +		// 课程信息表
				"(cid INTEGER PRIMARY KEY AUTOINCREMENT, " +		// 课程ID
				"weekfrom INTEGER," +		// 开始周次
				"weekto INTEGER," +		// 结束周次
				"weektype INTEGER," +		// 周次类型
				"day INTEGER," +			// 星期几
				"lesson_from INTEGER," +	// 节次开始
				"lesson_to INTEGER," +		// 节次结束
				"courseid TEXT," +			// 课程号
				"num TEXT," +				// 课序号
				"name TEXT," +				// 课程名
				"credit TEXT," +			// 学分
				"attr TEXT," +				// 课程属性
				"exam TEXT," +				// 考试类型
				"teacher TEXT," +			// 教师
				"campus TEXT," +			// 校区
				"bld TEXT," +				// 教学楼
				"place TEXT )"				// 教室	
				);
				
		db.execSQL("CREATE TABLE IF NOT EXISTS course_data" +		// 课程记录表
				"(uid INTEGER, " +			// 用户ID
				"cid INTEGER )"				// 课程ID	
				);
	}

	public Cursor select(){

		SQLiteDatabase db= getReadableDatabase();
		Cursor cursor=db.query("course_info",null,null,null,null,null,null);
		return cursor;
	}

	//如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

