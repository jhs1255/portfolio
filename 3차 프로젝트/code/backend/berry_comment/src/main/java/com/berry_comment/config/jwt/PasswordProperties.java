package com.berry_comment.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("password")
@Component
@Getter
@Setter
public class PasswordProperties {
    private String secretKey;
}
