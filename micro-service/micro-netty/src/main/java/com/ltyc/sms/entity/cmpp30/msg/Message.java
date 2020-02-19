package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.entity.BaseMessage;
import com.ltyc.sms.entity.PacketType;

import java.io.Serializable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public interface Message extends BaseMessage {
    public void setPacketType(PacketType packetType);
    public PacketType getPacketType();
    public void setTimestamp(long milliseconds);
    public long getTimestamp();
    public void setLifeTime(long lifeTime);
    public long getLifeTime();
    public void setHeader(Header head);
    public Header getHeader();
    public void setBodyBuffer(byte[] buffer);
    public byte[] getBodyBuffer();
    public Serializable getAttachment();
    public void setAttachment(Serializable attachment);
}
