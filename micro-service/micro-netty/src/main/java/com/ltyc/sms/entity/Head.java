package com.ltyc.sms.entity;

import com.ltyc.sms.entity.DataType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public interface Head {
    public DataType getDataType();
    public int getLength();
    public int getHeadLength();
}
