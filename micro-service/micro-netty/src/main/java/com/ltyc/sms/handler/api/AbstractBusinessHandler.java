package com.ltyc.sms.handler.api;

import com.ltyc.sms.connect.manager.EndpointEntity;
import io.netty.channel.ChannelDuplexHandler;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public abstract class AbstractBusinessHandler extends ChannelDuplexHandler implements BusinessHandlerInterface, Cloneable {

    private EndpointEntity entity;

    @Override
    public void setEndpointEntity(EndpointEntity entity) {
        this.entity = entity;
    }

    @Override
    public EndpointEntity getEndpointEntity() {
        return entity;
    }

    @Override
    public abstract String name();

    @Override
    public AbstractBusinessHandler clone() throws CloneNotSupportedException {
        return (AbstractBusinessHandler) super.clone();
    }
}
