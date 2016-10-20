package com.my_swpu.mystudyhelper.entity;

/**
 * Created by dsx on 2015/12/9 0009.
 */
public class GlobalUrl {

    private String login;		// 教务系统登录URL
    private String course;		// 教务系统课程URL
    private String grade;		// 教务系统成绩单URL

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

}
