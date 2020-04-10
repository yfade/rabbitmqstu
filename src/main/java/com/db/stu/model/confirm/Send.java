package com.db.stu.model.confirm;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        String queueName="test_confirm";
        channel.queueDeclare(queueName,false,false,false,null);

        channel.confirmSelect();
        channel.basicPublish("",queueName,null,"hello confirm".getBytes(StandardCharsets.UTF_8));

        if (channel.waitForConfirms()) {
            System.out.println("send success !!!");
        } else {
            System.out.println("send fail !!!");
        }
        channel.close();
        connection.close();
    }
}
