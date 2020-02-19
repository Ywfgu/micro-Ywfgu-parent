package com.ltyc.sms.entity;

import java.io.Serializable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public interface BaseMessage extends Serializable {
    public boolean isRequest();
    public boolean isResponse();
    public boolean isTerminated();
    public void setRequest(BaseMessage message);
    public BaseMessage getRequest();
    public long getSequenceNo();
    public void setSequenceNo(long seq);
}
