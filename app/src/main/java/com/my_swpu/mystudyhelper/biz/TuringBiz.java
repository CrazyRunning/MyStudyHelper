package com.my_swpu.mystudyhelper.biz;

import android.content.Intent;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.entity.TuringMessage;
import com.my_swpu.mystudyhelper.parser.TuringParse;
import com.my_swpu.mystudyhelper.util.Const;

import org.json.JSONException;

/**
 * Created by dsx on 2016/2/16 0016.
 */
public class TuringBiz {

    public static void sendMessage(String body){
//        String url = "http://www.tuling123.com/openapi/api";  //以前的接口,随时可能失效
        String url = "http://apis.baidu.com/turing/turing/turing";
        RequestParams params=new RequestParams();
        params.addHeader("apikey", Const.API_KEY);
        params.addQueryStringParameter("key", Const.KEY);
        params.addQueryStringParameter("info",body);
        params.addQueryStringParameter("userid",Const.USERID);
        HttpUtils utils=new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i("图灵", arg1);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.i("图灵", arg0.result);
                try {
                    TuringMessage m= TuringParse.parserTuring(arg0.result);
                    Intent intent=new Intent(Const.TURING_MESSAGE);
                    intent.putExtra("message", m);
                    BaseApplication.getInstance().sendBroadcast(intent);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.i("图灵", "json解析异常");
                }

            }
        });
    }
}
