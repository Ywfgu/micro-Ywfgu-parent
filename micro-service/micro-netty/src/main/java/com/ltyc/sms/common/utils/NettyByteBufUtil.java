package com.ltyc.sms.common.utils;

import io.netty.buffer.ByteBuf;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public final class NettyByteBufUtil {

    public static byte[] toArray(ByteBuf buf, int length){
        byte[] result = new byte[length];
        buf.readBytes(result);
        return result;
    }
}
