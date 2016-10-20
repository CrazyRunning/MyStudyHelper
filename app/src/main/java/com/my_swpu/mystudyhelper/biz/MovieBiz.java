package com.my_swpu.mystudyhelper.biz;

import java.util.Vector;

import android.content.Intent;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.entity.MovieEntity;
import com.my_swpu.mystudyhelper.parser.MovieParse;
import com.my_swpu.mystudyhelper.util.Const;

public class MovieBiz {
	
	public static void getMovieList(final boolean isRefresh){
		String url = "http://apis.baidu.com/apistore/movie/cinema";
		RequestParams params= new RequestParams();
		params.addHeader("apikey", Const.API_KEY);
		params.addQueryStringParameter("wd", "华星");
		params.addQueryStringParameter("location", "北京");
		params.addQueryStringParameter("output", "json");
		params.addQueryStringParameter("m", String.valueOf(20));
		HttpUtils http = new HttpUtils(60000);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
//				LogUtil.i("��ȡ��Ӱ", arg0.result);
				Vector<MovieEntity> movieList  = MovieParse.parse(arg0.result);
				Intent intent = new Intent(Const.ACTION_GET_MOVIE);
				intent.putExtra(Const.KEY_STATUS, Const.GET_MOVIE_SUCCESS);
				intent.putExtra("isRefresh", isRefresh);
				intent.putExtra("movieList", movieList);
				BaseApplication.getInstance().sendBroadcast(intent);
			}
		});
		
	}
}
