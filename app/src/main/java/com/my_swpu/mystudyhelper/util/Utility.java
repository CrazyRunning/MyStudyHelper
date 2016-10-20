package com.my_swpu.mystudyhelper.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utility {
	
	static public int time() {
		return (int)(System.currentTimeMillis()/1000);
	}
	
	/**
	 * 计算当前教学周
	 * @param termBegin 开学的日期
	 * @return 
	 */
	@SuppressLint("SimpleDateFormat")
	static public int getWeeks(String termBegin) {
		try {
			Date currentTime = new Date();
			
			SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dFormat.parse(termBegin);
			
			Calendar calendar = new GregorianCalendar();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);  		//将星期一作为一个星期的开始
			calendar.setTime(date);
			
			int weeks2 = calendar.get(Calendar.WEEK_OF_YEAR);	// 开学星期数
			calendar.setTime(currentTime);
			int weeks1 = calendar.get(Calendar.WEEK_OF_YEAR);	// 当前星期数
			
			if (date.after(currentTime)) {
				return 0;
			}
			else {
				int n = (weeks1-weeks2>=0)?(weeks1-weeks2+1):(weeks1-weeks2+53);
				return n;
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	static public float credit(float grade) {
    	if (grade < 60) {
			return 0;
		}
    	else if (grade < 65) {
    		return (float) 1.0;
		}
    	else if (grade < 70) {
    		return (float) 1.7;
		}
    	else if (grade < 75) {
    		return (float) 2.2;
		}
    	else if (grade < 80) {
    		return (float) 2.7;
		}
    	else if (grade < 85) {
    		return (float) 3.2;
		}
    	else if (grade < 90) {
    		return (float) 3.6;
		}
    	else if (grade < 95) {
    		return (float) 3.8;
		}
    	else if (grade <= 100) {
    		return (float) 4.0;
		}
    	else {
    		return 0;
		}
    }
	
	/**
	 * 根据节次和校区返回课程开始或结束的时间
	 * @param campus 校区
	 * @param lesson 节次
	 * @param isFrom 是否是课开始时间
	 * @return 课程时间
	 */
	static public String campusTime(String campus, int lesson, Boolean isFrom) {
		String timeString = new String();
		if (campus==null) {
			return "";
		}
		if (campus.equals("成都校区")||campus.equals("")) {
			if (isFrom) {
				switch (lesson) {
				case 1:
					timeString = "08:15";
					break;
				case 2:
					timeString = "09:05";
					break;
				case 3:
					timeString = "10:10";
					break;
				case 4:
					timeString = "11:00";
					break;
				case 5:
					timeString = "13:55";
					break;
				case 6:
					timeString = "14:45";
					break;
				case 7:
					timeString = "15:50";
					break;
				case 8:
					timeString = "16:40";
					break;
				case 9:
					timeString = "17:30";
					break;
				case 10:
					timeString = "19:20";
					break;
				case 11:
					timeString = "20:10";
					break;
				case 12:
					timeString = "21:00";
					break;
				default:
					timeString = "";
					break;
				}
			}
			else {
				switch (lesson) {
				case 1:
					timeString = "09:00";
					break;
				case 2:
					timeString = "09:50";
					break;
				case 3:
					timeString = "10:55";
					break;
				case 4:
					timeString = "11:45";
					break;
				case 5:
					timeString = "14:40";
					break;
				case 6:
					timeString = "15:30";
					break;
				case 7:
					timeString = "16:35";
					break;
				case 8:
					timeString = "17:25";
					break;
				case 9:
					timeString = "18:15";
					break;
				case 10:
					timeString = "20:05";
					break;
				case 11:
					timeString = "20:55";
					break;
				case 12:
					timeString = "21:45";
					break;
				default:
					timeString = "";
					break;
				}
			}
		}
		else {
			if (isFrom) {
				switch (lesson) {
				case 1:
					timeString = "08:00";
					break;
				case 2:
					timeString = "08:50";
					break;
				case 3:
					timeString = "10:00";
					break;
				case 4:
					timeString = "10:50";
					break;
				case 5:
					timeString = "14:00";
					break;
				case 6:
					timeString = "14:50";
					break;
				case 7:
					timeString = "16:00";
					break;
				case 8:
					timeString = "16:50";
					break;
				case 9:
					timeString = "17:40";
					break;
				case 10:
					timeString = "19:30";
					break;
				case 11:
					timeString = "20:20";
					break;
				case 12:
					timeString = "21:10";
					break;
				default:
					timeString = "";
					break;
				}
			}
			else {
				switch (lesson) {
				case 1:
					timeString = "08:45";
					break;
				case 2:
					timeString = "09:35";
					break;
				case 3:
					timeString = "10:45";
					break;
				case 4:
					timeString = "11:35";
					break;
				case 5:
					timeString = "14:45";
					break;
				case 6:
					timeString = "15:35";
					break;
				case 7:
					timeString = "16:45";
					break;
				case 8:
					timeString = "17:35";
					break;
				case 9:
					timeString = "18:25";
					break;
				case 10:
					timeString = "20:15";
					break;
				case 11:
					timeString = "21:05";
					break;
				case 12:
					timeString = "21:55";
					break;
				default:
					timeString = "";
						break;
					}
				}
			}
			return timeString;
		}
}
