package com.db.stu.model.video.ps;

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
        channel.basicPublish(EXCHANGE_NAME, "", false, false, null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("send " + msg);

        channel.close();
        connection.close();

    }
}
