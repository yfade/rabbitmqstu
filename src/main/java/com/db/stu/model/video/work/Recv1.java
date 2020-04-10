package com.db.stu.model.video.work;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //每个消费者发送确认消息之前，消息队列不发下一个消息到消费者，一次只处理一个消息，必须是手动应答
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Recv1 " + msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //消息应答
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        boolean autoAck = false; //手动模式
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
