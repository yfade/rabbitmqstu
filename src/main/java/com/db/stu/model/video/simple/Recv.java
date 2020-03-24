package com.db.stu.model.video.simple;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Recv {
    private final static String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //定义消费者
        Consumer consumer = new DefaultConsumer(channel) {
            //当队列中有新的消息时会触发此方法，获取消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("simple receive " + message);
            }
        };
        //订阅消息 监听队列，阻塞在这
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
