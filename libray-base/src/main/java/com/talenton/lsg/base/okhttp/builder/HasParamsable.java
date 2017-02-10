package com.talenton.lsg.base.okhttp.builder;

import java.util.Map;

/**
 *
 */
public interface HasParamsable
{
    OkHttpRequestBuilder params(Map<String, String> params);
    OkHttpRequestBuilder addParams(String key, String val);
}
