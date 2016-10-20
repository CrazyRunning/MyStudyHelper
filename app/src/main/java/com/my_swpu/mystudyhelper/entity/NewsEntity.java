package com.my_swpu.mystudyhelper.entity;

import java.io.Serializable;
import java.util.List;

/**
 *@param title 新闻标题
 *@param image 图片链接
 *@param conatent 新闻内容摘要
 *@param time 发布完整时间
 * */
@SuppressWarnings("serial")
public class NewsEntity implements Serializable{
	private String title;
	private List<String> imageUrl;
	private String link;
	private String time;
	private String channelName;
	private boolean hasImg;

	public boolean isHasImg() {
		return hasImg;
	}

	public void setHasImg(boolean hasImg) {
		this.hasImg = hasImg;
	}

	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	@Override
	public String toString() {
		return "NewsEntity [title=" + title + ", image=" + imageUrl + ", content="
				+ link + ", time=" + time + "]";
	}
	public NewsEntity() {
		super();
	}
	public NewsEntity(String title, List<String> imageUrl, String link, String time) {
		super();
		this.title = title;
		this.imageUrl = imageUrl;
		this.link = link;
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
