package com.my_swpu.mystudyhelper.biz;

import com.my_swpu.mystudyhelper.util.Const;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LocationBiz {
	String key = Const.BAIDU_API_KEY;
    
    public String getAddress(String latValue, String longValue){
        String location = getJsonLocation(latValue, longValue);
        location = getLocation(makeResults(location));
        return location;
    }
                                                                                                                                                                                                                 
    private String getJsonLocation(String latValue, String longValue){
        String urlStr = "http://api.map.baidu.com/geocoder?location=" + latValue + "," + longValue + "&output=json&key=" + key;
        HttpClient httpClient = new DefaultHttpClient();
           String responseData = "";
           try{
               //向指定的URL发送Http请求
               HttpResponse response = httpClient.execute(new HttpGet(urlStr));
               //取得服务器返回的响应
               HttpEntity entity = response.getEntity();
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
               String line = "";
               while((line = bufferedReader.readLine()) != null){
                   responseData = responseData + line;
               }
           }
           catch (Exception e) {
               e.printStackTrace();
           }
             return responseData;
    }
                                                                                                                                                                                                                 
    private String makeResults(String result){
        String dealResult = result.substring(0, result.indexOf("result") +8) + "[" + result.substring(result.indexOf("result") +8, result.length()-1) + "]}";
        return dealResult;
    }
                                                                                                                                                                                                                 
    private String getLocation(String str){
          JSONArray jsonObjs;
          String location = "";
        try {
             jsonObjs = new JSONObject(str).getJSONArray("result");
            //取出数组中第一个json对象(本示例数组中实际只包含一个元素)
                JSONObject jsonObj = jsonObjs.getJSONObject(0);
            //解析得formatted_address值
              String address = jsonObj.getString("formatted_address");
              String bussiness = jsonObj.getString("business");
              location = address + ":" + bussiness;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //取出数组中第一个json对象(本示例数组中实际只包含一个元素)
          return location;
    }
}
