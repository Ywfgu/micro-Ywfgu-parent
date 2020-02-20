package com.ltyc.demo18;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ���ַ� on 2018/9/6.
 */
public class HttpSessions {

    public static Map<String, NioSocketChannel> channelMap = new ConcurrentHashMap<>();
}
