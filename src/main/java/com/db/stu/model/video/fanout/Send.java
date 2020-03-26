package com.db.stu.model.video.fanout;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "ps_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String msg = "hello fanout!!!";
        // 循环发布多条消息, 注意广播模式不需要routingKey, 可以写成""， 也可以随意写个名字，在消费者也随便写一个，生产者和消费者的routingKey的不一样也可以
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("send " + msg);

        channel.close();
        connection.close();

    }
}
