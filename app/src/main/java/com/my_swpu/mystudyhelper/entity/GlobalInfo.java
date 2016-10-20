package com.my_swpu.mystudyhelper.entity;

/**
 * Created by dsx on 2016/2/15 0015.
 * 全局信息
 */
public class GlobalInfo {

    private int version;
    private String versionStr;
    private String termBegin;
    private int yearFrom;
    private int yearTo;
    private int term;
//    private int isFirstUse;
    private int activeUserUid;

    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public String getVersionStr() {
        return versionStr;
    }
    public void setVersionStr(String versionStr) {
        this.versionStr = versionStr;
    }
    public String getTermBegin() {
        return termBegin;
    }
    public void setTermBegin(String termBegin) {
        this.termBegin = termBegin;
    }
    public int getYearFrom() {
        return yearFrom;
    }
    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }
    public int getYearTo() {
        return yearTo;
    }
    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }
    public int getTerm() {
        return term;
    }
    public void setTerm(int term) {
        this.term = term;
    }
//    public int isFirstUse() {
//        return isFirstUse;
//    }
//    public void setFirstUse(int isFirstUse) {
//        this.isFirstUse = isFirstUse;
//    }
    public int getActiveUserUid() {
        return activeUserUid;
    }
    public void setActiveUserUid(int activeUserUid) {
        this.activeUserUid = activeUserUid;
    }
}
