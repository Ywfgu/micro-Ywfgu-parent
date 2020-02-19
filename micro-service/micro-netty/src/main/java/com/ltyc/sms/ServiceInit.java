package com.ltyc.sms;

import com.ltyc.sms.connect.cmpp.CMPPClientEndpointEntity;
import com.ltyc.sms.connect.manager.EndpointManager;
import com.ltyc.sms.handler.api.BusinessHandlerInterface;
import com.ltyc.sms.piper.consumer.ActiveMqConsumerHandler;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/17
 */
@Component
public class ServiceInit extends Thread  {

    @Override
    public void run() {

        try {
            testCMPPClientEndpoint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void testCMPPClientEndpoint() throws Exception {
        final EndpointManager manager = EndpointManager.INS;

        CMPPClientEndpointEntity client = new CMPPClientEndpointEntity();
        client.setId("GSDT01");
        client.setHost("127.0.0.1");
        client.setPort(7890);
        client.setChartset(Charset.forName("utf-8"));
        client.setGroupName("test");
        client.setUserName("901783");
        client.setPassword("ICP001");

        client.setSpCode("1069039128");
        client.setMaxChannels((short)1);
        client.setVersion((short)0x30);
        client.setRetryWaitTimeSec((short)30);
        client.setUseSSL(false);
        client.setReSendFailMsg(false);
        client.setWriteLimit(500);
        List<BusinessHandlerInterface> clienthandlers = new ArrayList<BusinessHandlerInterface>();
//        clienthandlers.add( new CMPPSessionConnectedHandler(30000));
        clienthandlers.add( new ActiveMqConsumerHandler());
        client.setBusinessHandlerSet(clienthandlers);
        manager.addEndpointEntity(client);

        manager.openAll();
        //LockSupport.park();
        Thread.sleep(1000);
        manager.openEndpoint(client);


        System.out.println("start.....");

        LockSupport.park();
        EndpointManager.INS.close();


    }
}
