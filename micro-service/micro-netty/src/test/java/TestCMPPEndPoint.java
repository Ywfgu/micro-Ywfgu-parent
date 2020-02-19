import com.ltyc.sms.connect.cmpp.CMPPClientEndpointEntity;
import com.ltyc.sms.connect.cmpp.CMPPServerChildEndpointEntity;
import com.ltyc.sms.connect.cmpp.CMPPServerEndpointEntity;
import com.ltyc.sms.connect.manager.EndpointManager;
import com.ltyc.sms.handler.api.BusinessHandlerInterface;
import com.ltyc.sms.piper.JmsListenerManager;
import com.ltyc.sms.piper.consumer.ActiveMqConsumerHandler;
import io.netty.util.ResourceLeakDetector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.util.ResourceLeakDetector.Level;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public class TestCMPPEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(TestCMPPEndPoint.class);

    @Test
    public void testCMPPServerEndpoint() throws Exception {
        ResourceLeakDetector.setLevel(Level.ADVANCED);

        final EndpointManager manager = EndpointManager.INS;

        CMPPServerEndpointEntity server = new CMPPServerEndpointEntity();
        server.setId("server");
        server.setHost("127.0.0.1");
        server.setPort(7890);
        server.setValid(true);
        //使用ssl加密数据流
        server.setUseSSL(false);
        server.setAcceptThread(1);
        server.setWorkThread(10);

        CMPPServerChildEndpointEntity child = new CMPPServerChildEndpointEntity();
        child.setId("child");
        child.setChartset(Charset.forName("utf-8"));
        child.setGroupName("test");
        child.setUserName("901783");
        child.setPassword("ICP001");

        child.setValid(true);
        child.setVersion((short)0x30);

        child.setMaxChannels((short)1);
        child.setRetryWaitTimeSec((short)30);
        child.setMaxRetryCnt((short)3);
        child.setReSendFailMsg(true);
        child.setWriteLimit(200);
        child.setReadLimit(200);
        List<BusinessHandlerInterface> serverhandlers = new ArrayList<BusinessHandlerInterface>();
        serverhandlers.add(new CMPPMessageReceiveHandler());
        child.setBusinessHandlerSet(serverhandlers);
        server.addChild(child);

        manager.addEndpointEntity(server);

        manager.openEndpoint(server);


        System.out.println("start.....");

        LockSupport.park();
        EndpointManager.INS.close();
    }

    @Test
    public void testCMPPClientEndpoint() throws Exception {
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
        clienthandlers.add( new CMPPSessionConnectedHandler(30000));
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
