package com.talenton.lsg.server.bean.shop;

import java.io.Serializable;

/**
 * Created by xiaoxiang on 2016/5/25.
 */

public final class BookOrderInfo implements Serializable {
    /**
     *
     */

    private static final long serialVersionUID = -582457752806705419L;

    public String  package_label;
    public int baobao_id;
    public int num;
    public double price;
    public String name;

}
