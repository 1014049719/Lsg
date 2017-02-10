package com.talenton.lsg.base.okhttp.builder;

import com.talenton.lsg.base.okhttp.OkHttpUtils;
import com.talenton.lsg.base.okhttp.request.OtherRequest;
import com.talenton.lsg.base.okhttp.request.RequestCall;

/**
 *
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
