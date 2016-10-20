package com.my_swpu.mystudyhelper.entity;

/**
 * 课程实体类
 * Created by dsx on 2015/12/9 0009.
 */
public class CourseEntity {

    private int cid;		// 数据记录相关的id

    private int weekfrom;	// 起始周
    private int weekto;		// 结束周
    private int weektype;	// 周类型：1普通；2单周；3双周
    private int day;		// 周几
    private int lessonfrom;	// 开始节次
    private int lessonto;	// 结束节次

    private String courseid;	// 课程号
    private String num;			// 课序号
    private String name;		// 课程名
    private String credit;		// 学分
    private String attr;		// 属性：必修；选修
    private String exam;		// 考试类型：考试；考察
    private String teacher;		// 教师
    private String campus;		// 校区
    private String bld;			// 教学楼
    private String place;		// 教室

    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public int getWeekfrom() {
        return weekfrom;
    }
    public void setWeekfrom(int weekfrom) {
        this.weekfrom = weekfrom;
    }
    public int getWeekto() {
        return weekto;
    }
    public void setWeekto(int weekto) {
        this.weekto = weekto;
    }
    public int getWeektype() {
        return weektype;
    }
    public void setWeektype(int weektype) {
        this.weektype = weektype;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getLessonfrom() {
        return lessonfrom;
    }
    public void setLessonfrom(int lessonfrom) {
        this.lessonfrom = lessonfrom;
    }
    public int getLessonto() {
        return lessonto;
    }
    public void setLessonto(int lessonto) {
        this.lessonto = lessonto;
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
    public String getCredit() {
        return credit;
    }
    public void setCredit(String credit) {
        this.credit = credit;
    }
    public String getAttr() {
        return attr;
    }
    public void setAttr(String attr) {
        this.attr = attr;
    }
    public String getExam() {
        return exam;
    }
    public void setExam(String exam) {
        this.exam = exam;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }
    public String getBld() {
        return bld;
    }
    public void setBld(String bld) {
        this.bld = bld;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

}
