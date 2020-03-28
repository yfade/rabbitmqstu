package com.db.stu.model.deadqueue;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //声明一个接受被删除消息的交换机和队列
        String dead_exchange = "exchange_dead";
        String dead_queue = "queue_dead";
        String dead_routing_key = "routingKey.dead";
        channel.exchangeDeclare(dead_exchange, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(dead_queue, false, false, false, null);
        channel.queueBind(dead_queue, dead_exchange, dead_routing_key);


        String exchangeName = "test_dead_exchange";
        String queueName = "test_dead_queue";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 15000);
        arguments.put("x-max-length", 4);
        arguments.put("x-max-length-bytes", 1024);
        arguments.put("x-expires", 30000);

        //配置死信交换机和路由键
        arguments.put("x-dead-letter-exchange", "exchange_dead");
        arguments.put("x-dead-letter-routing-key", "routingKey.dead");
        channel.queueDeclare(queueName, false, false, false, arguments);
        channel.queueBind(queueName, exchangeName, "");

        String msg = "hello rabbitMq ";
        for (int i = 1; i < 6; i++) {
            channel.basicPublish(exchangeName, "", null, (msg + i).getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("send over...");
        channel.close();
        connection.close();

    }
}
