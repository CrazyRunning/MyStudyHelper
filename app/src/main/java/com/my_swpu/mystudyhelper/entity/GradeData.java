package com.my_swpu.mystudyhelper.entity;

/**
 * Created by dsx on 2015/12/9 0009.
 */
public class GradeData {

    private int gid;
    private String courseid;	// 课程号
    private String num;			// 课序号
    private String name;		// 课程名
    private float credit;		// 学分
    private String attr;		// 属性：必修；选修
    private String time;		// 考试时间
    private float grade;		// 成绩
    public int getGid() {
        return gid;
    }
    public void setGid(int gid) {
        this.gid = gid;
    }
    public String getCourseid() {
        return courseid;
    }
    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getCredit() {
        return credit;
    }
    public void setCredit(float credit) {
        this.credit = credit;
    }
    public String getAttr() {
        return attr;
    }
    public void setAttr(String attr) {
        this.attr = attr;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public float getGrade() {
        return grade;
    }
    public void setGrade(float grade) {
        this.grade = grade;
    }

}
