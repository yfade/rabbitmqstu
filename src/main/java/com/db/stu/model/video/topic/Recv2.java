package com.db.stu.model.video.topic;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Recv2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "test_topic_q2";
        channel.queueDeclare(queueName, false, false, false, null);
        String EXCHANGE_NAME = "exchange.topic.x";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String[] routingKeys={"*.*.rabbit","lazy.#"};
        for (String routingKey : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        }

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Recv2 receive :" + msg);
            }
        };
        channel.basicConsume(queueName, true, consumer);

    }
}
