package com.berry_comment.init;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InitTest {
    @Autowired
    private Init init;

    @Test
    public void testInit() throws IOException {
        init.init();
    }
}