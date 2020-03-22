package com.db.stu.model.video.util;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {
    //获取MQ链接
    public static Connection getConnection() throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("127.0.0.1");
        //AMQP
        factory.setPort(AMQP.PROTOCOL.PORT);
        //设置virtualHost
        factory.setVirtualHost("/vh01");
        //用户名
        factory.setUsername("rmAdmin");
        //密码
        factory.setPassword("123456");
        return factory.newConnection();
    }
}
