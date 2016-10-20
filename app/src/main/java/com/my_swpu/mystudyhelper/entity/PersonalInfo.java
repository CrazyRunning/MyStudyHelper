package com.my_swpu.mystudyhelper.entity;

/**
 * 学生实体类
 * Created by dsx on 2015/12/9 0009.
 */
public class PersonalInfo {
	private int uid;
	private int sid;
	private String name;     //姓名
	private int days;
	private float percent;   //百分之？
	private float avarage;   //
	private float gpa;       //平均学分绩点

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public float getPercent() {
		return percent;
	}
	public void setPercent(float percent) {
		this.percent = percent;
	}
	public float getAvarage() {
		return avarage;
	}
	public void setAvarage(float avarage) {
		this.avarage = avarage;
	}
	public float getGpa() {
		return gpa;
	}
	public void setGpa(float gpa) {
		this.gpa = gpa;
	}
}
