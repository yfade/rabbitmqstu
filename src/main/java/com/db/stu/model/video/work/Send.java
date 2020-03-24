package com.db.stu.model.video.work;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //每个消费者发送确认消息之前，消息队列不发下一个消息到消费者，一次只处理一个消息
        channel.basicQos(1);
        for (int i = 0; i < 20; i++) {
            String msg = "hello " + i;
            channel.basicPublish("", QUEUE_NAME, false, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("send " + msg);
        }
        channel.close();
        connection.close();
    }
}
