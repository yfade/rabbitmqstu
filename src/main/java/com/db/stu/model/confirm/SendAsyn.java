package com.db.stu.model.confirm;

import com.db.stu.model.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class SendAsyn {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "test_confirm_asyn";
        channel.queueDeclare(queueName, false, false, false, null);
        //将生产者设置为confirm模式
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println(confirmSet+"  handleAck " + l + " " + b);
                if (b) {
                    confirmSet.headSet(l + 1L).clear();
                } else {
                    confirmSet.remove(l);
                }
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("handleNack " + l + " " + b);
                if (b) {
                    confirmSet.headSet(l + 1L).clear();
                } else {
                    confirmSet.remove(l);
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", queueName, null, ("hello " + i).getBytes());
            confirmSet.add(nextPublishSeqNo);
            System.out.println("nextNo " + nextPublishSeqNo);
        }
        System.out.println("send end...");

        channel.close();
        connection.close();
    }
}
