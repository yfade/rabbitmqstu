package com.db.stu.model.rpc;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RMServer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();

        String queueName = "rpc_queue";
        channel.queueDeclare(queueName, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 服务器端接收到消息并处理消息
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("receive:" + msg);
                String response = "{'code':200,'data':" + msg + "}";

                // 将消息发布到reply_to响应队列中
                AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
                channel.basicPublish("", properties.getReplyTo(), replyProperties, response.getBytes(StandardCharsets.UTF_8));
            }
        };
        channel.basicConsume(queueName, true, consumer);
        System.out.println("RMServer waiting..");
    }
}
