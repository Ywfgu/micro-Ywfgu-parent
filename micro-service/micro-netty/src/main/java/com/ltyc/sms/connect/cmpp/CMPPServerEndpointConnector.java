package com.ltyc.sms.connect.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.connect.manager.AbstractServerEndpointConnector;
import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.session.cmpp.CmppSessionLoginManager;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CMPPServerEndpointConnector extends AbstractServerEndpointConnector {
    private static final Logger logger = LoggerFactory.getLogger(CMPPServerEndpointConnector.class);
    public CMPPServerEndpointConnector(EndpointEntity e) {
        super(e);
    }

    @Override
    protected void doinitPipeLine(ChannelPipeline pipeline) {
        super.doinitPipeLine(pipeline);
        CMPPCodecChannelInitializer codec = null;
        if (getEndpointEntity() instanceof CMPPEndpointEntity) {
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

}
