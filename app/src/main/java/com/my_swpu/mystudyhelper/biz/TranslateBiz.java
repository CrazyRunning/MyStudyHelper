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
import com.my_swpu.mystudyhelper.parser.TranslateParse;
import com.my_swpu.mystudyhelper.util.Const;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

/**
 * Created by dsx on 2016/2/23 0023.
 */
public class TranslateBiz {

    public static void translate(final String query, final String from, final String to){
        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("q", query);
        params.addQueryStringParameter("from", "auto");
        params.addQueryStringParameter("to", to);
        params.addQueryStringParameter("appid", Const.APPID);
        String salt = String.valueOf(new Random(10000));
        params.addQueryStringParameter("salt", salt);
        String sign = Const.APPID + query + salt + Const.TRANSLATE_PASSWORD;
        String md5 = new String(Hex.encodeHex(DigestUtils.md5(sign)));
        params.addQueryStringParameter("sign", md5);
        Log.i("sign", md5);
        HttpUtils httpUtils = new HttpUtils(60000);
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.i("翻译", responseInfo.result);
                String result = TranslateParse.parse(responseInfo.result);
                Intent intent = new Intent(Const.TRANSLATW_ACTION);
                intent.putExtra("result", result);
                BaseApplication.getInstance().sendBroadcast(intent);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("翻译失败", e.getMessage().toString());
            }
        });

    }
}
