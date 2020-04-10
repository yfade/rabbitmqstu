package com.db.stu.model.direct;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "routing_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        String[] routingKeys = {"debug", "info", "warning", "error"};

        //测试统一队列绑定多个路由键
        for (int i = 0; i < 20; i++) {
            int index = (int) (Math.random() * (4));
            String routingKey = routingKeys[index];
            String message = "Hello RabbitMQ - " + routingKey + " - " + i;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("send " + message);
        }

        //测试同一路由键绑定多个队列
//        String msg = "test multiple bindings";
//        channel.basicPublish(EXCHANGE_NAME, "log", null, msg.getBytes(StandardCharsets.UTF_8));
        channel.close();
        connection.close();

    }
}
