package com.berry_comment.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("last-fm")
@Component
@Getter
@Setter
public class LastFmProperty {
    //lastfm api관련 키값 같은 거 들고 옴
    //lastFm apiKey
    private String apiKey;
    
    //lastFm 시크릿 키
    private String apiSecret;
}
