package com.berry_comment.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@ConfigurationProperties("jwt")
@Component
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private String issuer;
}
