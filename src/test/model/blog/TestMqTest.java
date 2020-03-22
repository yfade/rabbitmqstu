package model.blog;

import com.db.stu.model.blog.TestMq;
import org.junit.Test;

public class TestMqTest {

    @Test
    public void test() throws Exception {
        TestMq testMq=new TestMq();
        testMq.testBasicConsumer();
    }


}
