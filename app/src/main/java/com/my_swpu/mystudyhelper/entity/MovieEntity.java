package com.my_swpu.mystudyhelper.entity;

import java.io.Serializable;

/**
 * @param title
 *            褰辩墖鏍囬
 * @param image
 *            褰辩墖娴锋姤閾炬帴
 * @param actor
 *            褰辩墖婕斿憳鍒楄〃
 * @param guider
 *            褰辩墖鐨勫墽鎯呮瑕�
 * @param time
 *            褰辩墖涓婃槧鏃堕棿
 * @param runtime
 *            褰辩墖鎸佺画鏃堕棿
 * @param detail
 *            褰辩墖瀵兼紨鍒楄〃
 * @param rate
 *            褰辩墖璇勫垎
 */
@SuppressWarnings("serial")
public class MovieEntity implements Serializable {
	private String movie_name;
	private String movie_picture;
	private String movie_starring;
	private String movie_director;
	private String movie_release_date; // 上映时间
	private String movie_length; // 电影时长
	private String movie_description; // 电影简介
	private String movie_score;
	private String movie_nation;
	private String movie_type;
	private String movie_msg;

	public String getMovie_msg() {
		return movie_msg;
	}

	public void setMovie_msg(String movie_msg) {
		this.movie_msg = movie_msg;
	}

	public String getMovie_type() {
		return movie_type;
	}

	public void setMovie_type(String movie_type) {
		this.movie_type = movie_type;
	}

	@Override
	public String toString() {
		return "MovieEntity [title=" + movie_name + ", image=" + movie_picture
				+ ", actor=" + movie_director + ", guider=" + movie_director
				+ ", time=" + movie_release_date + ", runtime=" + movie_length
				+ ", detail=" + movie_description + ", rate=" + movie_score
				+ "]";
	}

	public MovieEntity() {
		super();
	}

	public String getMovie_picture() {
		return movie_picture;
	}

	public void setMovie_picture(String movie_picture) {
		this.movie_picture = movie_picture;
	}

	public String getMovie_nation() {
		return movie_nation;
	}

	public void setMovie_nation(String movie_nation) {
		this.movie_nation = movie_nation;
	}

	public String getMovie_name() {
		return movie_name;
	}

	public void setMovie_name(String title) {
		this.movie_name = title;
	}

	public String getMovie_starring() {
		return movie_starring;
	}

	public void setMovie_starring(String movie_starring) {
		this.movie_starring = movie_starring;
	}

	public String getMovie_director() {
		return movie_director;
	}

	public void setMovie_director(String movie_director) {
		this.movie_director = movie_director;
	}

	public String getMovie_release_date() {
		return movie_release_date;
	}

	public void setMovie_release_date(String movie_release_date) {
		this.movie_release_date = movie_release_date;
	}

	public String getMovie_length() {
		return movie_length;
	}

	public void setMovie_length(String movie_length) {
		this.movie_length = movie_length;
	}

	public String getMovie_description() {
		return movie_description;
	}

	public void setMovie_description(String movie_description) {
		this.movie_description = movie_description;
	}

	public String getMovie_score() {
		return movie_score;
	}

	public void setMovie_score(String movie_score) {
		this.movie_score = movie_score;
	}

}
