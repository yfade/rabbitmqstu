package com.db.stu.model.rpc;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class RMClient {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        String queueName = "rpc_queue";

        String replyQueueName = channel.queueDeclare().getQueue();
        final String correlationId = UUID.randomUUID().toString();

        //预先定义响应的结果，即预先订阅响应结果的队列，先订阅响应队列，再发送消息到请求队列
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (correlationId.endsWith(properties.getCorrelationId())) {
                    String response = new String(body, StandardCharsets.UTF_8);
                    System.out.println("client receive " + response);
                }
            }
        };
        channel.basicConsume(replyQueueName, true, consumer);

        //发送消息
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().correlationId(correlationId).replyTo(replyQueueName).build();
        String msg = "test rabbitMq rpc!";
        channel.basicPublish("", queueName, properties, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("send:" + msg);

    }
}
