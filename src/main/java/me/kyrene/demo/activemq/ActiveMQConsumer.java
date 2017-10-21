package me.kyrene.demo.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 消费者
 * Created by wanglin on 2017/10/19.
 */
public class ActiveMQConsumer {
    //默认连接用户
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKE_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    @Test
    public  void test1() {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection = null;
        //会话 接受或者发送消息的线程
        Session session;
        //消息的目的地
        Destination destination;
        //消息的消费者
        MessageConsumer messageConsumer;
        //实例化连接该工厂
        connectionFactory=new ActiveMQConnectionFactory(ActiveMQConsumer.USERNAME,ActiveMQConsumer.PASSWORD,ActiveMQConsumer.BROKE_URL);
        try {
            //通过实例化工厂获取连接
            connection=connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            //创建一个消息队列
            destination=session.createQueue("HELLO WORLD");
            //创建消费者
            messageConsumer=session.createConsumer(destination);

            while(true){
                TextMessage receive = (TextMessage) messageConsumer.receive(10000);
                if(receive!=null){
                    System.out.println("消费者接受到的消息："+receive.getText());
                }else{
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 本机运行的话需要在ActiveMQ 官网上下载 http://activemq.apache.org/activemq-5151-release.html 。
     * 然后选择你对应的多少位的系统。在点击.bat文件即可
     */
}
