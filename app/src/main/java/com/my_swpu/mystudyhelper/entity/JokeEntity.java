package com.my_swpu.mystudyhelper.entity;
//content��image
/**
 * 
 * @author 刘子健
 *
 */
public class JokeEntity {
	private String content;
	private String image;
	
	public JokeEntity() {
		super();
	}
/**
 * @param content 文本笑话
 * @param image   趣图
 */
	public JokeEntity(String content, String image) {
		super();
		this.content = content;
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "JokeEntity [content=" + content + ", image=" + image + "]";
	}
	
	
}
