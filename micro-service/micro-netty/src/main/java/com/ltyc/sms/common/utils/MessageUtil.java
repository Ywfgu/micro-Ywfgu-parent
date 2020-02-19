package com.ltyc.sms.common.utils;

import com.ltyc.sms.common.DynamicLoadPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;

import java.util.Collection;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/18
 */
public class MessageUtil {
    private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    /**
     * 动态创建消费者
     *
     * @param destinationName   消息通道名称
     * @param beanName          消费者bean名称
     * @param consumerBeanClass 消费者bean class,比如 MsgConsumer.class
     * @param fixedVarName4Dest 通道的固定名称，通过环境变量来动态设置
     * @return
     */
    public synchronized static boolean dynamicCreateMQConsumer(String destinationName, String beanName, Class<?> consumerBeanClass, String fixedVarName4Dest) {
        logger.info("start to create MsgConsumer ,destinationName=" + destinationName);
        boolean resultFlag = false;
        if(!DynamicLoadPropertySource.isContain(fixedVarName4Dest, destinationName)){
            DynamicLoadPropertySource.updateProperty(fixedVarName4Dest, destinationName);
            try {
                ApplicationContext context = CommonUtils.getApplicationContext();
                // 通过BeanDefinitionBuilder创建bean定义
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(consumerBeanClass);
                // 设置属性
                //beanDefinitionBuilder.addPropertyValue("consumerId",uuid);
                // 注册bean
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
                defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
                context.getBean(beanName, consumerBeanClass);
                resultFlag = true;
                logger.info("dynamic create MsgConsumer success!! destinationName= " + destinationName);
            } catch (Exception e) {
                logger.error("dynamic create MsgConsumer error:" + e.getMessage(), e);
            }
        }else{
            logger.info("MsgConsumer is already existed, destinationName=" + destinationName);
        }
        return resultFlag;
    }

    /**
     * 动态移除消息消费者
     *
     * @param mqDestinationName 消息通道名称
     * @param beanName          消费者bean名称
     * @param fixedVarName4Dest 通道的固定名称，通过环境变量来动态设置
     * @return
     */
    public synchronized static boolean dynamicRemoveMsgConsumer(String mqDestinationName, String beanName, String fixedVarName4Dest) {
        boolean resultFlag = false;
        logger.info("==== dynamic remove MQ Consumer ,destinationName: " + mqDestinationName + " ====");
        try {
            DynamicLoadPropertySource.removeProperty(fixedVarName4Dest, mqDestinationName);
            ApplicationContext context = CommonUtils.getApplicationContext();
//            if (msgMode.equals(MsgConstants.MESSAGE_MODE_ACTIVEMQ)) {
                JmsListenerEndpointRegistry jmsListenerEndpointRegistry = context.getBean(JmsListenerEndpointRegistry.class);
                Collection<MessageListenerContainer> containerCollections = jmsListenerEndpointRegistry.getListenerContainers();
                for (MessageListenerContainer container : containerCollections) {
                    DefaultMessageListenerContainer container1 = ((DefaultMessageListenerContainer) container);
                    if (container1.isRunning() && container1.getDestinationName().equals(mqDestinationName)) {
                        container1.setAcceptMessagesWhileStopping(true);//默认是false，false的话，consumer就会拒绝接收消息，消息会产生丢失
                        container1.shutdown();//关闭消息监听容器，消息消费者停止接收消息

                        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
                        //applicationContext中移除bean
                        defaultListableBeanFactory.removeBeanDefinition(beanName);
                        logger.info("dynamic remove Consumer success!! destinationName= " + mqDestinationName);
                        resultFlag = true;
                    } else {
                        logger.info("can't find Consumer destinationName: " + mqDestinationName);
                    }
                }
//            }
        } catch (Exception e) {
            logger.error("remove MsgConsumer error:" + e.getMessage(), e);
        }
        return resultFlag;
    }
}
