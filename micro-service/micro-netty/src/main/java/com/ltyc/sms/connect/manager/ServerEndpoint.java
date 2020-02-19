package com.ltyc.sms.connect.manager;

/**
 * @author guht
 * @version 1.0
 * @Title XXX.java
 * @Description XXX
 * @date 2020/2/5
 */
public interface ServerEndpoint {
    public void addChild(EndpointEntity entity);
    public void removeChild(EndpointEntity entity);
    public EndpointEntity getChild(String userName);
}
