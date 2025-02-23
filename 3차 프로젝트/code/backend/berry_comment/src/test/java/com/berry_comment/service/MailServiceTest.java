package com.berry_comment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailServiceTest {
    @Autowired
    private MailService mailService;
    @Test
    public void emailTest() {
        mailService.sendMailJoinSuccess("wjdehdwn2475@naver.com");
    }
}