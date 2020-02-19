package com.ltyc.sms.piper;

import com.ltyc.sms.common.utils.SpringUtil;
import com.ltyc.sms.connect.manager.EndpointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistry;

/**
 * @author guht
 * @version 1.0
 * @Description 显示的注册一个消息监听者
 * @create 2020/2/19
 */
public enum JmsListenerManager {
    LSR;
    private static final Logger logger = LoggerFactory.getLogger(JmsListenerManager.class);

    private JmsListenerEndpointRegistry jmsListenerEndpointRegistry = new JmsListenerEndpointRegistry();
    private JmsListenerContainerFactory defaultFactory = (JmsListenerContainerFactory) SpringUtil.getBean("jmsActiveMQQueueListener");

    public synchronized void registerListenerContainer(JmsListenerEndpoint endpoint, JmsListenerContainerFactory factory,boolean startImmediately){
        jmsListenerEndpointRegistry.registerListenerContainer(endpoint,factory,startImmediately);
    }
    public synchronized void registerListenerContainer(JmsListenerEndpoint endpoint, JmsListenerContainerFactory factory){
        jmsListenerEndpointRegistry.registerListenerContainer(endpoint,factory,false);
    }
    public synchronized void registerListenerContainer(JmsListenerEndpoint endpoint){
        jmsListenerEndpointRegistry.registerListenerContainer(endpoint,defaultFactory,false);
    }
    public synchronized void registerListenerContainer(JmsListenerEndpoint endpoint,boolean startImmediately){
        jmsListenerEndpointRegistry.registerListenerContainer(endpoint,defaultFactory,startImmediately);
    }
    public synchronized void startAll(){
        jmsListenerEndpointRegistry.start();
    }
    public synchronized void stopAll(){
        jmsListenerEndpointRegistry.stop();
    }
    public synchronized void stop(String endpointId){
        jmsListenerEndpointRegistry.getListenerContainer(endpointId).stop();
    }

}
