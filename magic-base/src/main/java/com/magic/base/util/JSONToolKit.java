package com.magic.base.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Cheng Yufei
 * @create 2019-08-10 23:16
 **/
public class JSONToolKit {

    public static JSONObject success() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "success");
        return jsonObject;
    }
    public static JSONObject success(Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("msg", "success");
        jsonObject.put("data", data);
        return jsonObject;
    }

}
