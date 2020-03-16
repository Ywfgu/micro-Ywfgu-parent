package com.ltyc.sms.piper.consumer;

import com.ltyc.sms.common.utils.ChannelUtil;
import com.ltyc.sms.common.utils.MessageUtil;
import com.ltyc.sms.common.utils.MsgId;
import com.ltyc.sms.common.utils.SpringUtil;
import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.connect.manager.ServerEndpoint;
import com.ltyc.sms.entity.BaseMessage;
import com.ltyc.sms.entity.cmpp30.msg.CmppDeliverRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.CmppSubmitRequestMessage;
import com.ltyc.sms.handler.api.AbstractBusinessHandler;
import com.ltyc.sms.piper.JmsListenerManager;
import com.ltyc.sms.piper.config.JmsConfig;
import com.ltyc.sms.session.SessionState;
import com.ltyc.sms.session.cmpp.CmppSessionLoginManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/17
 */
public class ActiveMqConsumerHandler extends AbstractBusinessHandler implements MessageListener{
    private static final Logger logger = LoggerFactory.getLogger(ActiveMqConsumerHandler.class);

    @Override
    public String name() {
        return "mqConsumer-Handler";
    }

//    @JmsListener(destination = "TEST.NETTY.CONSUMER", containerFactory = "jmsActiveMQQueueListener", concurrency = "1-5")
//    public void receive(Message message, Session session) throws JMSException {
//        ChannelFuture chfuture = null;
//        BaseMessage msg = createTestReq(UUID.randomUUID().toString());
//        chfuture = ChannelUtil.asyncWriteToEntity(getEndpointEntity().getId(), msg);
//        System.out.println("received message=" + message);
//        message.acknowledge();
//    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == SessionState.Connect) {
//            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
//            endpoint.setId("TEST.NETTY.CONSUMER");
//            endpoint.setDestination("TEST.NETTY.CONSUMER");
//            endpoint.setMessageListener(this);
//            JmsListenerManager.LSR.registerListenerContainer(endpoint,true);
//            System.out.println("===================" + getEndpointEntity().getId());
//            System.out.println("===================connect");
            JmsListenerManager.LSR.registerListenerContainer("TEST.NETTY.CONSUMER","TEST.NETTY.CONSUMER",this,true);
            //MessageUtil.dynamicCreateMQConsumer("TEST.NETTY.CONSUMER", "TEST.NETTY.CONSUMER", this.getClass(), "TEST.NETTY.CONSUMER");
        } else if (evt == SessionState.DisConnect){
            JmsListenerManager.LSR.stop(getEndpointEntity().getId());
        }
    }

    protected BaseMessage createTestReq(String content) {
        final EndpointEntity finalentity = getEndpointEntity();

        if (finalentity instanceof ServerEndpoint) {
            CmppDeliverRequestMessage msg = new CmppDeliverRequestMessage();
            msg.setDestId(String.valueOf(System.nanoTime()));
            msg.setLinkid("0000");
            msg.setMsgId(new MsgId());

            msg.setServiceid("10086");
            msg.setSrcterminalId(String.valueOf(System.nanoTime()));
            msg.setSrcterminalType((short) 1);

            return msg;
        } else {
            CmppSubmitRequestMessage msg = new CmppSubmitRequestMessage();
            msg.setDestterminalId(String.valueOf(System.nanoTime()));
            msg.setSrcId(String.valueOf(System.nanoTime()));
            msg.setLinkID("0000");
            msg.setRegisteredDelivery((short)1);
            msg.setServiceId("10086");
            return msg;
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof BytesMessage) {

        }else if(message instanceof TextMessage){
            logger.info("onMessage ..."+message);
            ChannelFuture chfuture = null;
            BaseMessage msg = createTestReq(UUID.randomUUID().toString());
            chfuture = ChannelUtil.asyncWriteToEntity(getEndpointEntity().getId(), msg);
            System.out.println("received message=" + message);
            try {
                message.acknowledge();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }


    }
}
