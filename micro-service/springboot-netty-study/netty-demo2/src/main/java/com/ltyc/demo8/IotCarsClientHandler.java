/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ltyc.demo8;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * Created by ���ַ� on 2018/8/18.
 */
public class IotCarsClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        new Thread(()->
        {
            while (true)
            {
                ByteBuf firstMessage = Unpooled.buffer(IotCarsClient.MSG_SIZE);
                for (int i = 0; i < firstMessage.capacity(); i ++) {
                    firstMessage.writeByte((byte) i);
                }
                ctx.writeAndFlush(firstMessage);
                try
                {
                    TimeUnit.MILLISECONDS.sleep(1);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}