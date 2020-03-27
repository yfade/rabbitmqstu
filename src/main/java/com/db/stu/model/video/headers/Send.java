package com.db.stu.model.video.headers;

import com.db.stu.model.video.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Send {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> heardersMap = new HashMap<String, Object>();
        heardersMap.put("api", "login");
        heardersMap.put("version", 1.0);
        heardersMap.put("radom", UUID.randomUUID().toString());
        AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().headers(heardersMap);

        String message = "Hello RabbitMQ!";
        String EXCHANGE_NAME = "exchange.hearders";
        channel.basicPublish(EXCHANGE_NAME, "", properties.build(), message.getBytes(StandardCharsets.UTF_8));

        channel.close();
        connection.close();
    }

}
