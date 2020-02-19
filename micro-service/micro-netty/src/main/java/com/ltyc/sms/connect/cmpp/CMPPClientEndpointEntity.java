package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.connect.manager.ClientEndpoint;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/17
 */
public class CMPPClientEndpointEntity extends CMPPEndpointEntity implements ClientEndpoint {

    @Override
    public CMPPClientEndpointConnector buildConnector() {
        return new CMPPClientEndpointConnector(this);
    }
}
