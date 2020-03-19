package model;

import com.db.stu.model.TestMq;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestMqTest {

    @Test
    public void test() throws Exception {
        TestMq testMq=new TestMq();
        testMq.testBasicConsumer();
    }


}
