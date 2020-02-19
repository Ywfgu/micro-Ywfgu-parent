package com.ltyc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2019/11/13
 */
public class HeaderCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println(">>>>>>>>>>encode");
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println(">>>>>>>>>>decode");
        ByteBuf buf = byteBuf.copy();
        CommonBody commonBody = new CommonBody();
        commonBody.setTotalLength(byteBuf.readUnsignedInt());
        commonBody.setCommandId(byteBuf.readUnsignedInt());
        commonBody.setSequenceId(byteBuf.readUnsignedInt());
        commonBody.setHeaderBuffer(new byte[12]);
        buf.readBytes(commonBody.getHeaderBuffer());
        commonBody.setBodyBuffer(new byte[(int)commonBody.getTotalLength()]);
        buf.readBytes(commonBody.getBodyBuffer());

        list.add(commonBody);
    }
}
