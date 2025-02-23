package com.berry_comment;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BerryCommentApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(BerryCommentApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
