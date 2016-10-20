package com.my_swpu.mystudyhelper.util;

import android.annotation.SuppressLint;

import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.entity.CourseEntity;
import com.my_swpu.mystudyhelper.entity.PersonalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dsx on 2015/12/9 0009.
 */
public class NetUtil {

    // 个人信息数据
    private class PInfo {
        public String name;			// 学生姓名
        public Date inSchool;		// 入学时间
        public float creditGot;		// 获得学分
        public float creditTotal;	// 总学分
    }

    private class CourseProperty {
        public String tmpStringID;
        public String tmpStringName;
        public String tmpStringNum;
        public String tmpStringCredit;
        public String tmpStringAttr;
        public String tmpStringExam;
        public String tmpStringTeacher;
        public String tmpStringWeeks;
        public String tmpStringWeek;
        public String tmpStringLesson;
        public String tmpStringCamp;
        public String tmpStringBld;
        public String tmpStringPlace;
    };

    public String login(String num, String passwd) {
        try {
            Connection con = Jsoup.connect("http://202.115.47.141/loginAction.do")
                    .data("zjh", num)
                    .data("mm",passwd)
//                    .data("Accept", "text/html, application/xhtml+xml, */*")
//                    .data("User-Agent", "Mozilla/5.0")
                    .timeout(5000)
                    .method(Connection.Method.POST);
            Response response = con.execute();
            Document doc = response.parse();

            if(doc.title().equals("学分制综合教务"))
            {
                Iterator<Map.Entry<String, String>> ite = response.cookies().entrySet().iterator();
                while(ite.hasNext()){
                    Map.Entry<String, String> entry = ite.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String sessionid = "";
                    String serverid = "";
                    if (key.equals("JSESSIONID"))
                        sessionid = value;
//                    if(key.equals("SERVERID"))
//                        serverid = value;
                    return sessionid ;
//                            + ";" + serverid;

                }
                return "100";	// TODO 错误代码100：未获取到SESSION
            }

            Element prompt = doc.getElementsByAttributeValue("class", "errorTop").first();
            String strong = prompt.select("strong>font").text().trim();

            if(strong.contains("不存在"))
            {
                return "101";	// TODO 错误代码101：学号不存在
            }
            else if(strong.contains("密码不正确"))
            {
                return "102";	// TODO 错误代码102：密码不正确
            }
            else {
                return "103";	// TODO 错误代码103：网页格式错误
            }
        }
        catch (Exception e) {
            return "104";		// TODO 错误代码104：连接超时或其他异常
        }
    }

    /**
     * 获取用户在教务系统的SESSION值
     * @param uData 用户数据
     * @param force 是否强制更新SESSION
     * @return SESSION
     */
    private String getSession(UserEntity uData, boolean force) {
        int lastLogin = uData.getLastlogin();
        if (force || (Utility.time() - lastLogin > 1800)) {
            String newSession = login(uData.getNum(), uData.getPasswd());
            return newSession;
        }
        else {
            return uData.getSession();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public PersonalInfo getPersonalInfo(UserEntity uData) {
        try {
            String session = getSession(uData, false);
            if (session.length() <= 3) {
                session = getSession(uData, true);
            }

            PInfo info = new PInfo();
            PersonalInfo pInfo = new PersonalInfo();
            List<Map<String, String>> data = new ArrayList<Map<String,String>>();

            Document doc = Jsoup.connect("http://202.115.47.141/reportFiles/student/cj_zwcjd_all.jsp")
                    .cookie("JSESSIONID", session)
                    .timeout(10000)
                    .get();

            // 读取表格
            Element report = doc.getElementById("report1");
            Elements tblView_tr = report.select("tr");

            // 解析表格个人信息，把数据记录到info中
            info.name = tblView_tr.get(1).select("td").eq(2).text().trim();
            String dateIn = tblView_tr.get(3).select("td").eq(4).text().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            info.inSchool = sdf.parse(dateIn);

            // 解析表格成绩，把数据添加到data中
            String tmpStringAttr= new String();
            String tmpStringSubject= new String();
            String tmpStringCredit= new String();
            String tmpStringGrade= new String();
            String tmpStringTime = new String();
            Map<String, String> tmpMap = new HashMap<String, String>();

            // 第一次循环，读取左列
            for (int j = 7; j < tblView_tr.size(); j++) {
                Element tblView_line = tblView_tr.get(j);
                String height = tblView_line.attr("height").trim();
                if (height.equals("14")) {
                    String creditGot = tblView_line.select("td").eq(2).text().trim();
                    String creditTotal = tblView_line.select("td").eq(4).text().trim();
                    info.creditGot = Float.parseFloat(creditGot);
                    info.creditTotal = Float.parseFloat(creditTotal);
                    break;
                }

                tmpStringAttr = tblView_line.select("td").eq(4).text().trim();
                if (tmpStringAttr.equals("必修"))
                    tmpStringAttr = "0";
                else {
                    tmpStringAttr = "1";
                }

                tmpStringSubject = tblView_line.select("td").eq(1).text().trim();
                tmpStringCredit = tblView_line.select("td").eq(2).text().trim();
                tmpStringGrade = tblView_line.select("td").eq(3).text().trim();
                tmpStringTime = tblView_line.select("td").eq(5).text().trim();

                if (tmpStringSubject.length() != 0) {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("attr", tmpStringAttr);
                    tmpMap.put("subject", tmpStringSubject);
                    tmpMap.put("credit", tmpStringCredit);
                    tmpMap.put("grade", tmpStringGrade);
                    tmpMap.put("time", tmpStringTime);
                    data.add(tmpMap);
                }
            }

            // 第二次循环，读取右列
            for (int j = 7; j < tblView_tr.size(); j++) {
                Element tblView_line = tblView_tr.get(j);
                String height = tblView_line.attr("height").trim();
                if (!height.equals("12")) {
                    break;
                }

                tmpStringAttr = tblView_line.select("td").eq(9).text().trim();
                if (tmpStringAttr.equals("必修"))
                    tmpStringAttr = "0";
                else {
                    tmpStringAttr = "1";
                }

                tmpStringSubject = tblView_line.select("td").eq(6).text().trim();
                tmpStringCredit = tblView_line.select("td").eq(7).text().trim();
                tmpStringGrade = tblView_line.select("td").eq(8).text().trim();
                tmpStringTime = tblView_line.select("td").eq(10).text().trim();

                if (tmpStringSubject.length() != 0) {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("attr", tmpStringAttr);
                    tmpMap.put("subject", tmpStringSubject);
                    tmpMap.put("credit", tmpStringCredit);
                    tmpMap.put("grade", tmpStringGrade);
                    tmpMap.put("time", tmpStringTime);
                    data.add(tmpMap);
                }
            }

            // 计算结果、更新数据
            float total_cr1 = 0; 	//必修课总学分
            float total_gr1 = 0;	//必修课总分

            float total_cr2 = 0; 	//所有科目总学分
            float total_gr2 = 0; 	//所有科目总学分绩点

            float tmp_cr = 0;		//学分临时变量
            float tmp_gr = 0;		//分数临时变量

            for (Map<String, String> i : data) {

                tmp_cr = Float.parseFloat(i.get("credit"));
                tmp_gr = Float.parseFloat(i.get("grade"));

                // 课程为选修
                if (i.get("attr").equals("1")) {
                    if (tmp_gr >= 60) {
                        total_cr2 += tmp_cr;
                        total_gr2 += Utility.credit(tmp_gr)*tmp_cr;
                    }
                }
                // 课程为必修
                else {
                    total_cr1 += tmp_cr;
                    total_gr1 += tmp_gr*tmp_cr;
                    total_cr2 += tmp_cr;
                    total_gr2 += Utility.credit(tmp_gr)*tmp_cr;
                }
            }

            // 必修课平均分
            float avarage = total_gr1/total_cr1;

            // 所有科目平均绩点
            float GPA = total_gr2/total_cr2;

            // 大学已过去天数
            Date now = new Date();
            int days = (int)((now.getTime() - info.inSchool.getTime()) / (86400000));

            // 方案完成百分比
            float percent = info.creditGot/info.creditTotal * 100;

            pInfo.setUid(uData.getUid());
            // TODO 学院ID
            pInfo.setSid(0);
            pInfo.setName(info.name);
            pInfo.setDays(days);
            pInfo.setPercent((percent));
            pInfo.setAvarage(avarage);
            pInfo.setGpa(GPA);
            return pInfo;

        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Map<String, String>> getRoll(UserEntity uData) {
        try {
            String session = getSession(uData, false);
            if (session.length() <= 3) {
                session = getSession(uData, true);
            }

            ArrayList<Map<String, String>> rollList = new ArrayList<Map<String, String>>();

            Document doc = Jsoup.connect("http://202.115.47.141/xjInfoAction.do?oper=xjxx")
                    .cookie("JSESSIONID", session)
                    .timeout(10000)
                    .get();
            Element tblView = doc.getElementById("tblView");
            Elements tblView_tr = tblView.select("tr");

            String tmpStringKey = new String();
            String tmpStringValue = new String();
            Map<String, String> tmpMap = new HashMap<String, String>();

            for (Element tblView_line : tblView_tr)
            {
                tmpStringKey = tblView_line.select("td").eq(0).text().trim();
                tmpStringValue = tblView_line.select("td").eq(1).text().trim();
                if (tmpStringKey.length() != 0) {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("key", tmpStringKey);
                    tmpMap.put("value", tmpStringValue);
                    rollList.add(tmpMap);
                }
                tmpStringKey = tblView_line.select("td").eq(2).text().trim();
                tmpStringValue = tblView_line.select("td").eq(3).text().trim();
                if (tmpStringKey.length() != 0) {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("key", tmpStringKey);
                    tmpMap.put("value", tmpStringValue);
                    rollList.add(tmpMap);
                }
            }

            return rollList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 修改密码
     * @param oldPass 原始密码
     * @param newPass1 新设密码
     * @param newPass2 再次输入的新设密码
     * @return 错误代码
     */
    public String changePassword(UserEntity uData,String oldPass, String newPass1,String newPass2) {
        try {
            String session = getSession(uData, false);
            if (session.length() <= 3) {
                session = getSession(uData, true);
            }

            Connection con = Jsoup.connect("http://202.115.47.141/modifyPassWordAction.do?oper=xgmm")
                    .cookie("JSESSIONID", session)
                    .data("yhlbdm", "01")
                    .data("zjh", uData.getNum())
                    .data("oldPass", oldPass)
                    .data("newPass1",newPass1)
                    .data("newPass2",newPass2)
                    .timeout(10000)
                    .method(Connection.Method.POST);
            Response response = con.execute();
            Document doc = response.parse();

            Elements prompt = doc.getElementsContainingText("!");
            String strong = prompt.text().trim();

            if(strong.contains("输入的当前密码"))
            {
                return "101";	// TODO 错误代码101：原始密码输入
            }
            else if(strong.contains("密码不正确"))
            {
                return "102";	// TODO 错误代码102：密码不正确
            } else
            {
                return session; // TODO 修改成功
            }
        } catch (Exception e)
        {
            return "104"; // TODO 错误代码104：连接超时或其他异常
        }
    }

    private CourseEntity processCourse(CourseProperty cp) {

        if (cp.tmpStringWeeks.equals("") || cp.tmpStringWeek.equals("") || cp.tmpStringLesson.equals("")) {
            try {
                if(BaseApplication.getInstance().getOthers().size() > 0) {
                    for(int i = 0; i < BaseApplication.getInstance().getOthers().size(); i++){
                        if(!cp.tmpStringName.equals(BaseApplication.getInstance().getOthers().get(i).getName())){
                            CourseEntity other = new CourseEntity();
                            other.setName(cp.tmpStringName);
                            other.setTeacher(cp.tmpStringTeacher);
                            BaseApplication.getInstance().getOthers().add(other);
                        }
                    }
                }else{
                    CourseEntity other = new CourseEntity();
                    other.setName(cp.tmpStringName);
                    other.setTeacher(cp.tmpStringTeacher);
                    BaseApplication.getInstance().getOthers().add(other);
                }
            }catch (Exception e0){

            }
            return null;
        }

        try {
            CourseEntity cInfo = new CourseEntity();

            cInfo.setCourseid(cp.tmpStringID);
            cInfo.setName(cp.tmpStringName);
            cInfo.setNum(cp.tmpStringNum);
            cInfo.setCredit(cp.tmpStringCredit);
            cInfo.setAttr(cp.tmpStringAttr);
            cInfo.setExam(cp.tmpStringExam);
            cInfo.setTeacher(cp.tmpStringTeacher);
            cInfo.setCampus(cp.tmpStringCamp);
            cInfo.setBld(cp.tmpStringBld);
            cInfo.setPlace(cp.tmpStringPlace);

            int lessonStart = 0;
            int lessonEnd = 0;
            if (cp.tmpStringLesson.contains("~")) {
                String lessonArray[] = cp.tmpStringLesson.split("~");
                lessonStart = Integer.parseInt(lessonArray[0]);
                lessonEnd = Integer.parseInt(lessonArray[1]);
            }
            else {
                lessonStart = lessonEnd = Integer.parseInt(cp.tmpStringLesson);
            }
            cInfo.setLessonfrom(lessonStart);
            cInfo.setLessonto(lessonEnd);
            cInfo.setDay(Integer.parseInt(cp.tmpStringWeek));

            if (cp.tmpStringWeeks.equals("1-17周")) {
                cInfo.setWeekfrom(1);
                cInfo.setWeekto(17);
                cInfo.setWeektype(1);
            }
            else if (cp.tmpStringWeeks.equals("双周")) {
                cInfo.setWeekfrom(1);
                cInfo.setWeekto(17);
                cInfo.setWeektype(3);
            }
            else if (cp.tmpStringWeeks.equals("单周")) {
                cInfo.setWeekfrom(1);
                cInfo.setWeekto(17);
                cInfo.setWeektype(2);
            }
            // ?-?周上
            else if (cp.tmpStringWeeks.contains("-")) {
                Pattern pattern = Pattern.compile("(\\d+)-(\\d+)\\w*");
                Matcher matcher = pattern.matcher(cp.tmpStringWeeks);
                matcher.find();
                int weekStart = Integer.parseInt(matcher.group(1));
                int weekEnd = Integer.parseInt(matcher.group(2));
                cInfo.setWeekfrom(weekStart);
                cInfo.setWeekto(weekEnd);
                cInfo.setWeektype(1);
            }
            // 2,4,6,8...或1,3,5,7...的情况
            else if (cp.tmpStringWeeks.contains(",")) {
                Pattern pattern = Pattern.compile("(\\d+)\\w*");
                Matcher matcher = pattern.matcher(cp.tmpStringWeeks);
                matcher.find();
                int week = Integer.parseInt(matcher.group(1));
                if (week == 1) {
                    cInfo.setWeekfrom(1);
                    cInfo.setWeekto(17);
                    cInfo.setWeektype(2);
                }
                else {
                    cInfo.setWeekfrom(1);
                    cInfo.setWeekto(17);
                    cInfo.setWeektype(3);
                }
            }
            // ?周上
            else {
                Pattern pattern = Pattern.compile("(\\d+)\\w*");
                Matcher matcher = pattern.matcher(cp.tmpStringWeeks);
                matcher.find();
                int week = Integer.parseInt(matcher.group(1));
                cInfo.setWeekfrom(week);
                cInfo.setWeekto(week);
                cInfo.setWeektype(1);
            }
            return cInfo;
        } catch (Exception e) {
            return null;
        }
    }

    public LinkedList<CourseEntity> getCourse(UserEntity uData) {
        try {
            String session = getSession(uData, false);
            if (session.length() <= 3) {
                session = getSession(uData, true);
            }

            Document doc = Jsoup.connect("http://202.115.47.141/xkAction.do?actionType=6")
                    .cookie("JSESSIONID", session)
                    .timeout(10000)
                    .get();

            Element table = doc.select(".displayTag").get(1);
            Elements tableTr = table.select("tbody tr");

            LinkedList<CourseEntity> cList = new LinkedList<CourseEntity>();

            for (int i = 0; i < tableTr.size(); ) {
                Elements tableTds = tableTr.get(i).select("td");

                // 某一行的<td>数大于6即是一个新的课程
                if ( tableTds.size() > 6 ) {

                    String rowspan = tableTds.get(0).attr("rowspan");
                    int rowpanInt = 0;
                    if (rowspan.equals("")) {
                        rowpanInt = 1;
                    }
                    else {
                        rowpanInt = Integer.parseInt(rowspan);
                    }

                    CourseProperty courseP = new CourseProperty();
                    courseP.tmpStringID = tableTds.get(1).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringName = tableTds.get(2).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringNum = tableTds.get(3).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringCredit = tableTds.get(4).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringAttr = tableTds.get(5).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringExam = tableTds.get(6).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringTeacher = tableTds.get(7).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringWeeks = tableTds.get(11).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringWeek = tableTds.get(12).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringLesson = tableTds.get(13).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringCamp = tableTds.get(14).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringBld = tableTds.get(15).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                    courseP.tmpStringPlace = tableTds.get(16).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");

                    CourseEntity cInfo = processCourse(courseP);
                    if (cInfo != null) {
                        cList.add(cInfo);
                    }

                    for (int j = 1; j < rowpanInt; j++) {
                        tableTds = tableTr.get(i+j).select("td");

                        courseP.tmpStringWeeks = tableTds.get(0).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                        courseP.tmpStringWeek = tableTds.get(1).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                        courseP.tmpStringLesson = tableTds.get(2).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                        courseP.tmpStringCamp = tableTds.get(3).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                        courseP.tmpStringBld = tableTds.get(4).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");
                        courseP.tmpStringPlace = tableTds.get(5).text().trim().replace(" &nbsp;", "").replaceAll("\\s*","");

                        CourseEntity cInfoNew = processCourse(courseP);
                        if (cInfoNew != null) {
                            cList.add(cInfoNew);
                        }
                    }
                    i += rowpanInt;
                }
            }
            return cList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取考试信息
     *
     * @param uData 用户数据
     * @return SESSION
     */
    public ArrayList<Map<String, String>> examInfo(UserEntity uData)
    {
        ArrayList <Map<String, String>> data = new ArrayList<Map<String, String>>();;
        try
        {
            String session = getSession(uData, false);
            if (session.length() <= 3) {
                session = getSession(uData, true);
            }

            /** 联网，连接中包含Cookie信息 **/
            Document doc = Jsoup.connect("http://202.115.47.141/ksApCxAction.do?oper=getKsapXx")
                    .cookie("JSESSIONID", session)
                    .timeout(10000)
                    .get();
            Elements displayTag = doc.select(".displayTag").eq(1).select("tbody");
            Elements tblView_tr = displayTag.select("tr");

            /** 解析表格，把文本添加到与列表关联的data中 **/
            String tmpStringSubject = new String();
            String tmpStringTime = new String();
            String tmpStringPlace = new String();
            Map<String, String> tmpMap = new HashMap<String, String>();

            for (Element tblView_line : tblView_tr)
            {
                tmpStringSubject = tblView_line.select("td").eq(4).text().trim();
                tmpStringTime = "时间：" + tblView_line.select("td").eq(5).text().trim()
                        + "周 周" + tblView_line.select("td").eq(6).text().trim() + "  "
                        + tblView_line.select("td").eq(7).text().trim();
                tmpStringPlace = "地点：" + tblView_line.select("td").eq(1).text().trim()
                        + tblView_line.select("td").eq(2).text().trim()
                        + tblView_line.select("td").eq(3).text().trim();

                if (tmpStringSubject.length() != 0)
                {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("subject", tmpStringSubject);
                    tmpMap.put("time", tmpStringTime);
                    tmpMap.put("place", tmpStringPlace);
                    data.add(tmpMap);
                }
            }
        } catch (Exception e)
        {
            return null; // TODO 错误代码104：连接超时或其他异常
        }
        return data;
    }

    public List<Map<String, String>> getGrades(UserEntity uData) {
        try {

            String session = getSession(uData, false);
            if (session.length() <= 3) {
                session = getSession(uData, true);
            }

            Document doc = Jsoup.connect("http://202.115.47.141/reportFiles/student/cj_zwcjd_all.jsp")
                    .cookie("JSESSIONID", session)
                    .timeout(10000)
                    .get();

            // 读取表格
            Element report = doc.getElementById("report1");
            Elements tblView_tr = report.select("tr");

            List <Map<String, String>> data = new ArrayList<Map<String,String>>();

            // 解析表格成绩，把数据添加到data中
            String tmpStringAttr= new String();
            String tmpStringSubject= new String();
            String tmpStringCredit= new String();
            String tmpStringGrade= new String();
            String tmpStringTime = new String();
            Map<String, String> tmpMap = new HashMap<String, String>();

            // 第一次循环，读取左列
            for (int j = 7; j < tblView_tr.size(); j++) {
                Element tblView_line = tblView_tr.get(j);
                String height = tblView_line.attr("height").trim();
                if (!height.equals("12")) {
                    break;
                }

                tmpStringAttr = tblView_line.select("td").eq(4).text().trim();
                tmpStringSubject = tblView_line.select("td").eq(1).text().trim();
                tmpStringCredit = tblView_line.select("td").eq(2).text().trim();
                tmpStringGrade = tblView_line.select("td").eq(3).text().trim();
                tmpStringTime = tblView_line.select("td").eq(5).text().trim();

                if (tmpStringSubject.length() != 0) {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("attr", tmpStringAttr);
                    tmpMap.put("subject", tmpStringSubject);
                    tmpMap.put("credit", tmpStringCredit);
                    tmpMap.put("grade", tmpStringGrade);
                    tmpMap.put("time", tmpStringTime);
                    data.add(tmpMap);
                }
            }

            // 第二次循环，读取右列
            for (int j = 7; j < tblView_tr.size(); j++) {
                Element tblView_line = tblView_tr.get(j);
                String height = tblView_line.attr("height").trim();
                if (!height.equals("12")) {
                    break;
                }

                tmpStringAttr = tblView_line.select("td").eq(9).text().trim();
                tmpStringSubject = tblView_line.select("td").eq(6).text().trim();
                tmpStringCredit = tblView_line.select("td").eq(7).text().trim();
                tmpStringGrade = tblView_line.select("td").eq(8).text().trim();
                tmpStringTime = tblView_line.select("td").eq(10).text().trim();

                if (tmpStringSubject.length() != 0) {
                    tmpMap = new HashMap<String, String>();
                    tmpMap.put("attr", tmpStringAttr);
                    tmpMap.put("subject", tmpStringSubject);
                    tmpMap.put("credit", tmpStringCredit);
                    tmpMap.put("grade", tmpStringGrade);
                    tmpMap.put("time", tmpStringTime);
                    data.add(tmpMap);
                }
            }
            return data;

        } catch (Exception e) {
            return null;
        }
    }

    public float[] getAvaGpa(List<Map<String, String>> gradeList) {
        float AvaGpa[] = new float[2];

        // 计算结果、更新数据
        float total_cr1 = 0; 	//必修课总学分
        float total_gr1 = 0;	//必修课总分

        float total_cr2 = 0; 	//所有科目总学分
        float total_gr2 = 0; 	//所有科目总学分绩点

        float tmp_cr = 0;		//学分临时变量
        float tmp_gr = 0;		//分数临时变量

        for (Map<String, String> i : gradeList) {

            tmp_cr = Float.parseFloat(i.get("credit"));
            tmp_gr = Float.parseFloat(i.get("grade"));

            // 课程为选修
            if (!i.get("attr").equals("必修")) {
                if (tmp_gr >= 60) {
                    total_cr2 += tmp_cr;
                    total_gr2 += Utility.credit(tmp_gr)*tmp_cr;
                }
            }
            // 课程为必修
            else {
                total_cr1 += tmp_cr;
                total_gr1 += tmp_gr*tmp_cr;
                total_cr2 += tmp_cr;
                total_gr2 += Utility.credit(tmp_gr)*tmp_cr;
            }
        }

        // 必修课平均分
        float avarage = total_gr1/total_cr1;

        // 所有科目平均绩点
        float GPA = total_gr2/total_cr2;

        AvaGpa[0] = avarage;
        AvaGpa[1] = GPA;
        return AvaGpa;
    }
}
