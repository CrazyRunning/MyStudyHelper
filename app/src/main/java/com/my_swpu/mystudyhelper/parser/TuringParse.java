package com.my_swpu.mystudyhelper.parser;

import com.my_swpu.mystudyhelper.entity.TuringMessage;
import com.my_swpu.mystudyhelper.util.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsx on 2016/2/16 0016.
 */
public class TuringParse {

    public static TuringMessage parserTuring(String json) throws JSONException {

        JSONObject jsonObject=new JSONObject(json);
        TuringMessage m=new TuringMessage();
        m.setType(Const.TURING_TEXT);
        m.setText(jsonObject.getString("text"));
        return m;
    }
}
