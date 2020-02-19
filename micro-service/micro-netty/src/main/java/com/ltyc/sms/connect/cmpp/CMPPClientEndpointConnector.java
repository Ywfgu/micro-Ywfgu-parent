package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.connect.manager.AbstractClientEndpointConnector;
import com.ltyc.sms.connect.manager.ClientEndpoint;
import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.session.AbstractSessionStateManager;
import com.ltyc.sms.session.cmpp.CmppSessionLoginManager;
import com.ltyc.sms.session.cmpp.CmppSessionStateManager;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/17
 */
public class CMPPClientEndpointConnector extends AbstractClientEndpointConnector {
    private static final Logger logger = LoggerFactory.getLogger(CMPPClientEndpointConnector.class);



    public CMPPClientEndpointConnector(CMPPClientEndpointEntity e)
    {
        super(e);

    }

    @Override
    protected void doBindHandler(ChannelPipeline pipe, EndpointEntity cmppentity) {
        CMPPEndpointEntity entity = (CMPPEndpointEntity)cmppentity;

//        if (entity instanceof ClientEndpoint) {
//            pipe.addAfter(GlobalConstance.codecName, "reWriteSubmitMsgSrcHandler", new ReWriteSubmitMsgSrcHandler(entity) );
//        }
        //处理长短信
//        pipe.addLast( "CMPPDeliverLongMessageHandler", new CMPPDeliverLongMessageHandler(entity));
//        pipe.addLast("CMPPSubmitLongMessageHandler",  new CMPPSubmitLongMessageHandler(entity));
//
//        pipe.addLast("CmppActiveTestRequestMessageHandler", GlobalConstance.activeTestHandler);
//        pipe.addLast("CmppActiveTestResponseMessageHandler", GlobalConstance.activeTestRespHandler);
//        pipe.addLast("CmppTerminateRequestMessageHandler", GlobalConstance.terminateHandler);
//        pipe.addLast("CmppTerminateResponseMessageHandler", GlobalConstance.terminateRespHandler);

    }

    @Override
    protected void doinitPipeLine(ChannelPipeline pipeline) {
        CMPPCodecChannelInitializer codec = null;
        EndpointEntity entity = getEndpointEntity();
        if (entity instanceof CMPPEndpointEntity) {
            pipeline.addLast(GlobalConstance.IdleCheckerHandlerName,
                    new IdleStateHandler(0, 0, ((CMPPEndpointEntity) getEndpointEntity()).getIdleTimeSec(), TimeUnit.SECONDS));

            codec = new CMPPCodecChannelInitializer(((CMPPEndpointEntity) getEndpointEntity()).getVersion());

        } else {
            pipeline.addLast(GlobalConstance.IdleCheckerHandlerName, new IdleStateHandler(0, 0, 30, TimeUnit.SECONDS));

            codec = new CMPPCodecChannelInitializer();
        }
        pipeline.addLast("CmppServerIdleStateHandler", GlobalConstance.idleHandler);
        pipeline.addLast(codec.getPipeName(), codec);
        pipeline.addLast("sessionLoginManager", new CmppSessionLoginManager(getEndpointEntity()));
    }



    @Override
    protected AbstractSessionStateManager createSessionManager(EndpointEntity entity) {
        return new CmppSessionStateManager(entity);
    }

}
