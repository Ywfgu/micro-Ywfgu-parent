package com.ltyc.sms.piper.config;

import com.ltyc.sms.common.utils.SpringUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.DeliveryMode;
import javax.jms.MessageListener;
import javax.jms.Session;
import java.util.Objects;


/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/17
 */
@EnableJms
@Configuration
public class JmsConfig{

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次   这里设置为10次
        redeliveryPolicy.setMaximumRedeliveries(10);
        //重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    public PooledConnectionFactory activeMQConnectionFactory(@Value("${inner.activemq.url}") String url,
                                                             @Value("${inner.activemq.username}") String username,
                                                             @Value("${inner.activemq.password}") String password,
                                                             @Value("${inner.activemq.maxConnection}") String maxConnection,
                                                             @Value("${inner.activemq.maximumActiveSessionPerConnection}") String maximumActiveSessionPerConnection,
                                                             @Value("${inner.activemq.TimeBetweenExpirationCheckMillis}") String TimeBetweenExpirationCheckMillis,
                                                             RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(
                        username,
                        password,
                        url);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        activeMQConnectionFactory.getPrefetchPolicy().setQueuePrefetch(1);
        activeMQConnectionFactory.setUseAsyncSend(true);//异步发送，可以提高吞吐量，但是会有数据丢失
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(
                activeMQConnectionFactory);
        pooledConnectionFactory.setMaximumActiveSessionPerConnection(Integer.valueOf(Objects.toString(maximumActiveSessionPerConnection,"100")));
        pooledConnectionFactory.setMaxConnections(Integer.valueOf(Objects.toString(maxConnection,"50")));
        pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(Long.valueOf(Objects.toString(TimeBetweenExpirationCheckMillis,"3000")));
        return pooledConnectionFactory;
    }

    @Bean(name = "jmsActiveMQQueueListener")
    public JmsListenerContainerFactory<?> jmsActiveMQQueueListener(PooledConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory);
        //设置连接数
        factory.setConcurrency("1-1");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setPubSubDomain(false);
        return factory;
    }


//    @Override
//    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
//        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
//        endpoint.setId(getDestination());
//        endpoint.setDestination(getDestination());
//        endpoint.setMessageListener(getMessageListener());
//        registrar.registerEndpoint(endpoint);
//        registrar.setContainerFactory((JmsListenerContainerFactory)SpringUtil.getBean("jmsActiveMQQueueListener"));
//        this.setRegistrar(registrar);
//    }
//    private JmsListenerEndpointRegistrar registrar=null;
//    private String destination="";
//    private MessageListener messageListener=null;
//
//    public void setDestination(String destination) {
//        this.destination = destination;
//    }
//
//    public void setMessageListener(MessageListener messageListener) {
//        this.messageListener = messageListener;
//    }
//
//    public String getDestination() {
//        return destination;
//    }
//
//    public MessageListener getMessageListener() {
//        return messageListener;
//    }
//
//    public JmsListenerEndpointRegistrar getRegistrar() {
//        return registrar;
//    }
//
//    public void setRegistrar(JmsListenerEndpointRegistrar registrar) {
//        this.registrar = registrar;
//    }
}
