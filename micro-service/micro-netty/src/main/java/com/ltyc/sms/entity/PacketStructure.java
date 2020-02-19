package com.ltyc.sms.entity;

import com.ltyc.sms.entity.DataType;

/**
 * @author guht
 * @version 1.0
 * @date 2020/2/6
 */
public interface PacketStructure {
    public DataType getDataType();
    public boolean isFixFiledLength();
    public boolean isFixPacketLength();
    public int getLength();
    public int getBodyLength();
}
