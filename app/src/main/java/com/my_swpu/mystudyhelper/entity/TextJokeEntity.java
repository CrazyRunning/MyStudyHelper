package com.my_swpu.mystudyhelper.entity;

import java.io.Serializable;


//content��image

/**
 * 文字笑话实体
 * 
 * @author dsx
 * 
 */
public class TextJokeEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String title;
	private String date;

	public TextJokeEntity() {
		super();
	}

	/**
	 * @param content
	 *            文本笑话
	 * @param title
	 *            
	 */
	public TextJokeEntity(String content, String title, String date) {
		super();
		this.content = content;
		this.date = date;

		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
//		date=TextUtil.getDate(date);
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
//		content=TextUtil.replaceP(content);
		this.content = content;
	}

	public String getImage() {
		return title;
	}

	public void setImage(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "JokeEntity [content=" + content + ", title=" + title + "]";
	}

}
