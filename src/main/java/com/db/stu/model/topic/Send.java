package com.db.stu.model.topic;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String EXCHANGE_NAME = "exchange.topic.x";
        String[] routingKeys = {"quick.orange.rabbit", "lazy.orange.elephant", "mq.erlang.rabbit", "lazy.brown.fox", "lazy."};
        for (String routingKey : routingKeys) {
            String msg = "hello topic -" + routingKey;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("send msg:"+msg);
        }

        channel.close();
        connection.close();
    }
}
