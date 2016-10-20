package com.my_swpu.mystudyhelper.parser;

import com.my_swpu.mystudyhelper.entity.MovieEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import org.json.JSONArray;
import org.json.JSONObject;

public class MovieParse {

	List<MovieEntity> movieList = new ArrayList<MovieEntity>();

	public static Vector<MovieEntity> parse(String json) {
		Vector<MovieEntity> movieList = new Vector<MovieEntity>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray result = obj.getJSONArray("result");
			// MovieEntity movie = JSON.parseObject("movies",
			// MovieEntity.class);
//			LogUtil.i("��Ӱ", result.length() + "");
			JSONObject item = result.getJSONObject(0);
			JSONArray movies = item.getJSONArray("movies");
//			LogUtil.i("��Ӱ", movies.length() + "");
			for (int i = 0; i < movies.length(); i++) {
				JSONObject Item = movies.getJSONObject(i);

				// LogUtil.i("��Ӱ���", movie.getMovie_name());
				MovieEntity movie = new MovieEntity();
				String movie_name = Item.getString("movie_name");
				String movie_picture = Item.getString("movie_picture");
				String movie_score = Item.getString("movie_score");
				String movie_nation = Item.getString("movie_nation");
				String movie_type = Item.getString("movie_type");
				String movie_director = Item.getString("movie_director");
				String movie_starring = Item.getString("movie_starring");
				String movie_length = Item.getString("movie_length");
				String movie_description = Item.getString("movie_description");
				String movie_message = Item.getString("movie_message");
				movie.setMovie_msg(movie_message);
				movie.setMovie_description(movie_description);
				movie.setMovie_length(movie_length);
				movie.setMovie_starring(movie_starring);
				movie.setMovie_nation(movie_nation);
				movie.setMovie_type(movie_type);
				movie.setMovie_director(movie_director);
				movie.setMovie_name(movie_name);
				movie.setMovie_picture(movie_picture);
				movie.setMovie_score(movie_score);
				movieList.add(movie);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}
}
