package me.kyrene.demo.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 生产者
 * Created by wanglin on 2017/10/19.
 */
public class ActiveMQProducer {
    //默认连接用户
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKE_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    //发送的消息数量
    private static final int SEND_NUM =10;


    @Test
    public  void  test1() {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection =null;
        //会话，接受或发送消息的线程
        Session session ;
        //消息的目的地
        Destination destination;
        //消息生产者
        MessageProducer messageProducer;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQProducer.USERNAME,ActiveMQProducer.PASSWORD,ActiveMQProducer.BROKE_URL);
        try{
            //通过连接工厂获取连接
            connection=connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建seesion
            session=connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            //创建一个名为hello word的消息队列
            destination= session.createQueue("HELLO WORLD");
            //创建消息生产者
            messageProducer=session.createProducer(destination);
            //发送消息
            sendMail(session,messageProducer);

            session.commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 发送消息
     * @param session
     * @param messageProducer
     */
    private static void sendMail(Session session, MessageProducer messageProducer) throws  Exception{
        for(int i = 0 ; i<ActiveMQProducer.SEND_NUM;i++){
            //创建一条文本消息
            TextMessage textMessage = session.createTextMessage("ACTIVE MQ 发送第" + i + "条消息");
            System.out.println("ACTIVE MQ 发送第" + i + "条消息");
            //通过消息生产者发送消息
            messageProducer.send(textMessage);
        }
    }
}
