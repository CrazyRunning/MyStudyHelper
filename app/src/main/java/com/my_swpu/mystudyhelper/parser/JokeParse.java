package com.my_swpu.mystudyhelper.parser;

import com.my_swpu.mystudyhelper.entity.TextJokeEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by dsx on 2016/1/20 0020.
 */
public class JokeParse {

    public static Vector<TextJokeEntity> parse(String json){
        Vector<TextJokeEntity> jokeList = new Vector<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getString("showapi_res_code").equals("0")){
                JSONObject showapi_res_body = jsonObject.getJSONObject("showapi_res_body");
                JSONArray contentlist = showapi_res_body.getJSONArray("contentlist");
                for(int i = 0; i < contentlist.length(); i++){
                    JSONObject obj = (JSONObject) contentlist.get(i);
                    String ct = obj.getString("ct");
                    String text = obj.getString("text");
                    String title = obj.getString("title");
                    TextJokeEntity textJokeEntity = new TextJokeEntity(text, title, ct);
                    jokeList.add(textJokeEntity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jokeList;
    }
}
