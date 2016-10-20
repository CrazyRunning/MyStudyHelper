package com.my_swpu.mystudyhelper.parser;

import com.my_swpu.mystudyhelper.entity.NewsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;


public class NewsParse {

	public static Vector<NewsEntity> parse(String json) {
		Vector<NewsEntity> newsList = new Vector<NewsEntity>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONObject showapi_res_body = obj.getJSONObject("showapi_res_body");
			JSONObject pagebean = showapi_res_body.getJSONObject("pagebean");			
			JSONArray array = pagebean.getJSONArray("contentlist");
			for (int i = 0; i < array.length(); i++) {
				JSONObject newsItem = array.getJSONObject(i);
				NewsEntity news = new NewsEntity();
				String title = newsItem.getString("title");
				String time = newsItem.getString("pubDate");				
				JSONArray imgArray = newsItem.getJSONArray("imageurls");
				List<String> imgUrl = new ArrayList<String>();
				if (imgArray.length() > 0) {
					news.setHasImg(true);
					for (int j = 0; j < imgArray.length(); j++) {
						JSONObject imgObj = imgArray.getJSONObject(j);
						imgUrl.add(imgObj.getString("url"));
					}
//					LogUtil.i("withImg", i + "");
				}else{
//					LogUtil.i("noImgId", i + "");
					news.setHasImg(false);
				}
				String link = newsItem.getString("link");
				String channelName = newsItem.getString("channelName");
				news.setTitle(title);
				news.setTime(time);
				news.setImageUrl(imgUrl);
				news.setLink(link);
				news.setChannelName(channelName);
				newsList.add(news);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsList;

	}
}
