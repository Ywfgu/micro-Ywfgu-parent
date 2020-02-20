package com.ltyc.sms.piper;

import com.ltyc.sms.common.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;

import javax.jms.MessageListener;

/**
 * @author guht
 * @version 1.0
 * @Description 动态注册一个监听者
 * @create 2020/2/20
 */
public enum JmsListenerManager {
    LSR;
    private static final Logger logger = LoggerFactory.getLogger(JmsListenerManager.class);

    private MyJmsListenerEndpointRegistry jmsListenerEndpointRegistry = new MyJmsListenerEndpointRegistry();
    private JmsListenerContainerFactory defaultFactory = (JmsListenerContainerFactory) SpringUtil.getBean("jmsActiveMQQueueListener");

    public synchronized void registerListenerContainer(JmsListenerEndpoint endpoint, JmsListenerContainerFactory factory, boolean startImmediately){
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
    public synchronized void registerListenerContainer(String endpointId, String endpointDestination, @Nullable MessageListener messageListener, boolean startImmediately){
        if(checkIsRegister(endpointId)){
            checkAndstart(endpointId);
        }else{
            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(endpointId);
            endpoint.setDestination(endpointDestination);
            endpoint.setMessageListener(messageListener);
            jmsListenerEndpointRegistry.registerListenerContainer(endpoint,defaultFactory,startImmediately);
        }
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
    public synchronized void destroy(String endpointId){
        try {
            MessageListenerContainer listenerContainer =jmsListenerEndpointRegistry.getListenerContainer(endpointId);
            if(null != listenerContainer){
                listenerContainer.stop();
                jmsListenerEndpointRegistry.removeContainer(endpointId);
            }
            if (listenerContainer instanceof DisposableBean) {
                ((DisposableBean)listenerContainer).destroy();
            }
        }
        catch (Throwable ex) {
            logger.warn("Failed to destroy message listener container", ex);
        }
    }

    /**
     * @param endpointId
     * @return
     */
    public boolean checkIsRegister(String endpointId){
        MessageListenerContainer listenerContainer =jmsListenerEndpointRegistry.getListenerContainer(endpointId);
        return null != listenerContainer;
    }

    public synchronized void checkAndstart(String endpointId){
        MessageListenerContainer listenerContainer =jmsListenerEndpointRegistry.getListenerContainer(endpointId);
        if(!listenerContainer.isRunning()){
            listenerContainer.start();
        }
    }

}
