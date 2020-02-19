package com.ltyc.sms.entity;

import com.ltyc.sms.entity.PacketStructure;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public interface PacketType {
    public long getCommandId();
    public PacketStructure[] getPacketStructures();
    public long getAllCommandId();
    public MessageToMessageCodec getCodec();
}
