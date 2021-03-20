package com.sign;

import com.sign.tool.Request;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootTest
class SignApplicationTests {

    @Test
    void contextLoads() {
        Request.sign();
    }

}
