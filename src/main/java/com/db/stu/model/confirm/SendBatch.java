package com.db.stu.model.confirm;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SendBatch {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String queueName="test_confirm";
        channel.queueDeclare(queueName,false,false,false,null);

        channel.confirmSelect();
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("",queueName,null,("hello confirm batch "+i).getBytes(StandardCharsets.UTF_8));
            System.out.println("send "+i);
        }
        channel.waitForConfirms();
        System.out.println("batch confirm success");
        channel.close();
        connection.close();
    }
}
