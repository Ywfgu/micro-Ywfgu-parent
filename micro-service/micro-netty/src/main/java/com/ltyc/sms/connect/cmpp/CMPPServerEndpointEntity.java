package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.connect.manager.ServerEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public class CMPPServerEndpointEntity extends EndpointEntity implements ServerEndpoint {

    private Map<String, CMPPServerChildEndpointEntity> childrenEndpoint = new ConcurrentHashMap<String, CMPPServerChildEndpointEntity>();


    @Override
    public void addChild(EndpointEntity entity) {
        childrenEndpoint.put(((CMPPServerChildEndpointEntity) entity).getUserName().trim(), (CMPPServerChildEndpointEntity) entity);
    }

    @Override
    public void removeChild(EndpointEntity entity) {
        childrenEndpoint.remove(((CMPPServerChildEndpointEntity) entity).getUserName());
    }

    @Override
    public EndpointEntity getChild(String userName) {
        return childrenEndpoint.get(userName);
    }

    @Override
    public CMPPServerEndpointConnector buildConnector() {
        return new CMPPServerEndpointConnector(this);
    }

}
