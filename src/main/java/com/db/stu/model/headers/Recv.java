package com.db.stu.model.headers;

import com.db.stu.model.util.ConnectionUtil;
import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Recv {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        String EXCHANGE_NAME = "exchange.hearders";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);

        String queueName = channel.queueDeclare().getQueue();
        Map<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("x-match", "any");
        arguments.put("api", "login");
        arguments.put("version", 1.0);
        arguments.put("dataType", "json");


        // 队列绑定时需要指定参数,注意虽然不需要路由键但仍旧不能写成null，需要写成空字符串""
        channel.queueBind(queueName, EXCHANGE_NAME, "", arguments);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(message);
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}
