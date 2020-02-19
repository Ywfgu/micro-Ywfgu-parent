package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.codec.cmpp.CMPPMessageCodecAggregator;
import com.ltyc.sms.codec.cmpp.CmppHeaderCodec;
import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.NotSupportedException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CMPPCodecChannelInitializer extends ChannelInitializer<Channel> {
    private static final Logger logger = LoggerFactory.getLogger(CMPPCodecChannelInitializer.class);
    private int version;

    private final static int defaultVersion = 0x30;

    public CMPPCodecChannelInitializer() {
        this.version = defaultVersion;
    }

    public CMPPCodecChannelInitializer(int version) {
        this.version = version;
    }

    private final static String pipeName = "cmppCodec";

    public String getPipeName() {
        return pipeName;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
		/*
		 * 消息总长度(含消息头及消息体) 最大消消息长度要从配置里取 处理粘包，断包
		 */
        pipeline.addBefore(pipeName, "FrameDecoder", new LengthFieldBasedFrameDecoder(4 * 1024 , 0, 4, -4, 0, true));

        pipeline.addBefore(pipeName, "CmppHeaderCodec", new CmppHeaderCodec());

        pipeline.addBefore(pipeName, GlobalConstance.codecName, getCodecHandler(version));

    }

    public static ChannelDuplexHandler getCodecHandler(int version) throws Exception {
        if (version == 0x30L) {
            return CMPPMessageCodecAggregator.getInstance();
        } else if (version == 0x20L) {
//            return CMPP20MessageCodecAggregator.getInstance();
        } else if (version == 0x7FL) {
//            return CMPP7FMessageCodecAggregator.getInstance();
        }
        logger.error("not supported protocol version: {}", version);
        throw new NotSupportedException("not supported protocol version.");
    }

}
