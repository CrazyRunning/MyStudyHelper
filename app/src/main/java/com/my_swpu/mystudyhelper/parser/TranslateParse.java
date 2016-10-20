package com.my_swpu.mystudyhelper.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsx on 2016/2/23 0023.
 */
public class TranslateParse {
    public static String parse(String json){
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
//            JSONObject retData = jsonObject.getJSONObject("retData");
            JSONArray trans_result = jsonObject.getJSONArray("trans_result");
            for(int i = 0; i < trans_result.length(); i++){
                JSONObject object = (JSONObject) trans_result.get(i);
                result += object.getString("dst") + "\n";
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
