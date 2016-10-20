package com.my_swpu.mystudyhelper.entity;

import java.io.Serializable;

/**
 * Created by dsx on 2016/2/16 0016.
 */
public class TuringMessage implements Serializable{
    private static final long serialVersionUID = 1L;
    private int type;
    private String text;
    /**
     * @param type 消息类型
     * @param text 消息内容
     */
    public TuringMessage(int type, String text) {
        this.type = type;
        this.text = text;
    }
    public TuringMessage() {
        // TODO Auto-generated constructor stub
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
