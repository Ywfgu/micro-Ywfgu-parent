package com.ltyc.sms.handler.api;

import com.ltyc.sms.connect.manager.EndpointEntity;
import io.netty.channel.ChannelHandler;

/**
 * @author guht
 * @version 1.0
 * @Title XXX.java
 * @Description 业务处理接口
 * @date 2020/2/5
 */
public interface BusinessHandlerInterface extends ChannelHandler {
    /**
     *业务处理名称
     */
    String name();
    /**
     *设置端口对象
     */
    void setEndpointEntity(EndpointEntity entity);

    EndpointEntity getEndpointEntity();
}
