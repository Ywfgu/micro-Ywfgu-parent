import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.util.List;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/29
 */
public class ActiveMqTest {

    @Test
    public void Producer() throws JMSException, InterruptedException {
        String url = "tcp://10.8.132.230:61616";
        url = "tcp://127.0.0.1:61616";
//        url ="failover:(tcp://10.8.132.61:62616,tcp://10.8.132.63:62616,tcp://10.8.132.65:62616)";
        String topicName = "TEST.NETTY.CONSUMER";

        //1. 创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",url);
        //2. 创建Connection
        Connection connection = connectionFactory.createConnection();
//        connection.setClientID("Producer-test");
        //3. 启动连接
        connection.start();
        //4. 创建会话
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //5. 创建消息队列
        Destination destination=session.createQueue(topicName);
//        Topic destination=session.createTopic(topicName);
//        Queue selector1 = session.createQueue("selector1");
        //6. 创建消息生产者
        MessageProducer messageProducer=session.createProducer(destination);
//        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);//使用持久模式
        //7. 发送消息
        for (int i=0;i<100000;i++) {
            TextMessage message = session.createTextMessage("topic"+i);
//            message.setStringProperty("agentId","81AFF737D67746C99A9B9D4905D9724"+i);
//            message.setStringProperty("agentName","local"+i);
            messageProducer.send(message);
//            messageProducer.send(session.createTextMessage("topic"+i));
//            System.out.println("send topic"+i);
//            Thread.sleep(100L);
            if(i % 2000 ==0){
                session.commit();
                System.out.println("commit count="+i);
                Thread.sleep(1000L);
            }
        }
        //8. 提交
        session.commit();
        connection.close();
    }

    @Test
    public void createQueueConsumer() throws JMSException {
        String url = "tcp://10.8.132.230:61616";
        String topicName = "TEST.NETTY.CONSUMER";

        //1. 创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",url);
        //2. 创建Connection
        Connection connection = connectionFactory.createConnection();
//        connection.setClientID("Producer-test1");//持久订阅需要设置这个
        //3. 启动连接
        connection.start();
        //4. 创建会话
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5. 创建一个目标
        Queue destination = session.createQueue(topicName);
//        Topic destination = session.createTopic(topicName);
        //6. 创建一个消费者
        MessageConsumer consumer = session.createConsumer(destination);
//        MessageConsumer consumer = session.createConsumer(destination,"agentName = 'local3'");
//        TopicSubscriber consumer = session.createDurableSubscriber(destination,"sub-Producer-test");
        //7. 创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message)  {
                try {
                    message.acknowledge();
                    System.out.println("Queue Consumer接收消息  = [" + ((TextMessage) message).getText() + "]");
//                    System.out.println("Queue Consumer接收消息  = [" + ((MapMessage) message).getString("body") + "]");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List list = null;
                    list.size();
                } catch (JMSException e) {
                    e.printStackTrace();
                    try {
                        session.recover();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        //8.关闭连接
        //connection.close();
    }

    public static void main(String[] args) throws JMSException {
        createConsumer2();
    }
    public static void createConsumer2() throws JMSException {
        String url = "tcp://10.8.132.230:61616";
        url = "tcp://127.0.0.1:61616";
        url ="failover:(tcp://10.8.132.61:62616,tcp://10.8.132.63:62616,tcp://10.8.132.65:62616)";
        String topicName = "TEST.NETTY.CONSUMER";

        //1. 创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", url);
        //2. 创建Connection
        Connection connection = connectionFactory.createConnection();
//        connection.setClientID("Producer-test");//持久订阅需要设置这个
        //3. 启动连接
        connection.start();
        //4. 创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5. 创建一个目标
        Queue destination = session.createQueue(topicName);
        //6. 创建一个消费者
        MessageConsumer consumer = session.createConsumer(destination);
//        TopicSubscriber consumer = session.createDurableSubscriber(destination,"sub-Producer-test");
        //7. 创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            int cnt=0;
            public void onMessage(Message message) {
                try {
                    if(cnt == 0){
                        System.out.println("startTime:"+System.currentTimeMillis());
                    }
                    cnt++;
//                    if(cnt % 1000== 0){
//                        System.out.println("cnt:"+cnt);
                        System.out.println("接收消息  = [" + ((TextMessage) message).getText() + "]");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("endTime:"+System.currentTimeMillis());

        //8.关闭连接
        //connection.close();
    }
}
