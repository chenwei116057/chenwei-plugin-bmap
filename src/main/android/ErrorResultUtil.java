package com.chenwei.plugin;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResultUtil {

    public static String toJsonString(String code, String msg) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        return jsonObject.toString();
    }
}
