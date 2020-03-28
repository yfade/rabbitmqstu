package com.db.stu.model.simple;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    private final static String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取一个链接
        Connection connection = ConnectionUtil.getConnection();
        //创建一个信道(一个轻量级的连接)
        Channel channel = connection.createChannel();
        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //要发送的消息
        String msg = "hello simple queue!";
        //发送消息 注意：exchange如果不需要写成空字符串，routingKey和队列名称保持一致
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("simple queue send " + msg);
        channel.close();
        connection.close();
    }
}
