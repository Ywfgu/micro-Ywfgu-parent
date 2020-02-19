package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.connect.manager.ServerEndpoint;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public class CMPPServerChildEndpointEntity extends CMPPEndpointEntity implements ServerEndpoint {

    @Override
    public CMPPServerChildEndpointConnector buildConnector() {

        return new CMPPServerChildEndpointConnector(this);
    }

    @Override
    public void addChild(EndpointEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeChild(EndpointEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public EndpointEntity getChild(String userName) {
        // TODO Auto-generated method stub
        return null;
    }



}