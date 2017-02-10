package com.talenton.lsg.base.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjh
 * @date 2016/4/19
 */
public class JsonObjUtil {
    private JsonObjUtil(){
        params = new HashMap<>();
    }

    private static JsonObjUtil jsonObjUtil;
    private Map<String,String> params;

    public static JsonObjUtil getInstance(){
        if (jsonObjUtil == null){
            jsonObjUtil = new JsonObjUtil();
        }
        return jsonObjUtil;
    }

    public JsonObjUtil addParam(String key,String value){
        params.put(key,value);
        return this;
    }

    public JsonObjUtil addParam(String key,int value){
        params.put(key,String.valueOf(value));
        return this;
    }

    public JsonObjUtil addParam(String key,float value){
        params.put(key,String.valueOf(value));
        return this;
    }

    public JsonObjUtil addParam(String key,double value){
        params.put(key,String.valueOf(value));
        return this;
    }

    public JsonObjUtil addParam(String key,long value){
        params.put(key,String.valueOf(value));
        return this;
    }

    /**
     * 转换成json数据并清除map
     * @return
     */
    public String toJsonString(){
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String,String> param : params.entrySet()){
            try {
                jsonObject.put(param.getKey(),param.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        params.clear();
        return jsonObject.toString();
    }
}
