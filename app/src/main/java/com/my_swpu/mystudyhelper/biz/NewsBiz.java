package com.my_swpu.mystudyhelper.biz;

import java.util.Vector; 

import android.content.Intent;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.entity.NewsEntity;
import com.my_swpu.mystudyhelper.parser.NewsParse;
import com.my_swpu.mystudyhelper.util.Const;


public class NewsBiz {
	
	public static void getNewsList(final boolean isRefresh, final int page){
		String url = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
		RequestParams param = new RequestParams();
		param.addHeader("apikey", Const.API_KEY);
		param.addQueryStringParameter("page", String.valueOf(page));
		HttpUtils http = new HttpUtils(60000);
		http.send(HttpMethod.GET, url, param, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Const.ACTION_GET_NEWS);
				intent.putExtra(Const.KEY_DATA, Const.GET_NEWS_FAILURE);
				BaseApplication.getInstance().sendBroadcast(intent);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Log.i("新闻", arg0.result);
				NewsParse newsParse = new NewsParse();
				@SuppressWarnings("static-access")
				Vector<NewsEntity> newsList = newsParse.parse(arg0.result);
				Intent intent = new Intent(Const.ACTION_GET_NEWS);
				intent.putExtra(Const.KEY_STATUS, Const.GET_NEWS_SUCCESS);
				intent.putExtra("isRefresh", isRefresh);
				intent.putExtra("newsList", newsList);
				BaseApplication.getInstance().sendBroadcast(intent);
			}
		});
		
	}

}
