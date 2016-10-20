package com.my_swpu.mystudyhelper.biz;

import android.content.Intent;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.entity.TextJokeEntity;
import com.my_swpu.mystudyhelper.parser.JokeParse;
import com.my_swpu.mystudyhelper.util.Const;

import java.util.Vector;

/**
 * Created by dsx on 2016/1/20 0020.
 */
public class JokeBiz {

    public static void getJokeList(final boolean isRefresh, final int page){
        String url = "http://apis.baidu.com/showapi_open_bus/showapi_joke/joke_text";
        RequestParams params= new RequestParams();
        params.addHeader("apikey", Const.API_KEY);
        params.addQueryStringParameter("page", String.valueOf(page));
        HttpUtils http = new HttpUtils(10000);
        http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("笑话", responseInfo.result);
                Vector<TextJokeEntity> jokeList = JokeParse.parse(responseInfo.result);
                Intent intent = new Intent(Const.ACTION_GET_JOKE);
                intent.putExtra(Const.KEY_STATUS, Const.GET_JOKE_SUCCESS);
                intent.putExtra("isRefresh", isRefresh);
                intent.putExtra("jokeList", jokeList);
                BaseApplication.getInstance().sendBroadcast(intent);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("笑话获取失败", e.getMessage() + "");
            }
        });
    }
}
