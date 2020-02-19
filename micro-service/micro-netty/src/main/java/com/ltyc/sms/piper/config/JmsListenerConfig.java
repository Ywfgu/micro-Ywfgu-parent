package com.ltyc.sms.piper.config;

import com.ltyc.sms.session.cmpp.CmppSessionLoginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.MessageListenerContainer;

import javax.jms.MessageListener;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/18
 */
//@Configuration
public class JmsListenerConfig implements JmsListenerConfigurer {
    private static JmsListenerEndpointRegistry jmsListenerEndpointRegistry = new JmsListenerEndpointRegistry();
    private String destination="";
    private MessageListener messageListener=null;

    public JmsListenerConfig(String destination,MessageListener messageListener) {
        this.destination = destination;
        this.messageListener = messageListener;
    }

    private static final Logger logger = LoggerFactory.getLogger(JmsListenerConfig.class);

    private static void registerEndpoint(JmsListenerEndpointRegistrar registrar) {

    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId("ed001");
        endpoint.setDestination("test.mq.queue");
        endpoint.setMessageListener(getMessageListener());
        registrar.registerEndpoint(endpoint);
    }

    public String getDestination() {
        return destination;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }
}
