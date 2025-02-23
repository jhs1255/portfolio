package com.berry_comment.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("kakao-pay")
@Component
@Getter
@Setter
public class KakaoPayProperty {
    private String cid;
    private String secretKey;
    private int cost;
    private String itemName;
    private int quantity;
    private String failUrl;
    private String taxFreeAmount;
    private String successUrl;
    private String cancelUrl;
    private String approveUrl;
}
