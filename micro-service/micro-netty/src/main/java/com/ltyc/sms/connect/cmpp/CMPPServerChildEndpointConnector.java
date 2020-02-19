package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.connect.manager.AbstractEndpointConnector;
import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.session.AbstractSessionStateManager;
import com.ltyc.sms.session.cmpp.CmppSessionStateManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public class CMPPServerChildEndpointConnector extends AbstractEndpointConnector {
    private static final Logger logger = LoggerFactory.getLogger(CMPPServerChildEndpointConnector.class);

    public CMPPServerChildEndpointConnector(CMPPEndpointEntity endpoint) {
        super(endpoint);
    }

    @Override
    public ChannelFuture open() throws Exception {
        //TODO 子端口打开，说明有客户连接上来
        return null;
    }


    @Override
    protected AbstractSessionStateManager createSessionManager(EndpointEntity entity) {
        return new CmppSessionStateManager(entity);
    }


    @Override
    protected void doBindHandler(ChannelPipeline pipe, EndpointEntity cmppentity) {
        CMPPEndpointEntity entity = (CMPPEndpointEntity)cmppentity;
        // 修改连接空闲时间,使用server.xml里配置的连接空闲时间生效
        if (entity instanceof CMPPServerChildEndpointEntity) {
            ChannelHandler handler = pipe.get(GlobalConstance.IdleCheckerHandlerName);
            if (handler != null) {
                pipe.replace(handler, GlobalConstance.IdleCheckerHandlerName, new IdleStateHandler(0, 0, entity.getIdleTimeSec(), TimeUnit.SECONDS));
            }
        }
//        //处理长短信
//        pipe.addLast( "CMPPDeliverLongMessageHandler", new CMPPDeliverLongMessageHandler(entity));
//        pipe.addLast("CMPPSubmitLongMessageHandler",  new CMPPSubmitLongMessageHandler(entity));
//
//        pipe.addLast("CmppActiveTestRequestMessageHandler", GlobalConstance.activeTestHandler);
//        pipe.addLast("CmppActiveTestResponseMessageHandler", GlobalConstance.activeTestRespHandler);
//        pipe.addLast("CmppTerminateRequestMessageHandler", GlobalConstance.terminateHandler);
//        pipe.addLast("CmppTerminateResponseMessageHandler", GlobalConstance.terminateRespHandler);
    }

    @Override
    protected SslContext createSslCtx() {
        return null;
    }

    @Override
    protected void initSslCtx(Channel ch, EndpointEntity entity) {
    }



    @Override
    protected void doinitPipeLine(ChannelPipeline pipeline) {

    }
}
