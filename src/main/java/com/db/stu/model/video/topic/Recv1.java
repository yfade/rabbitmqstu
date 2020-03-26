package com.db.stu.model.video.topic;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "test_topic_q1";
        channel.queueDeclare(queueName, false, false, false, null);
        String EXCHANGE_NAME = "exchange.topic.x";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Recv1 receive :" + msg);
            }
        };
        channel.basicConsume(queueName, true, consumer);

    }
}
