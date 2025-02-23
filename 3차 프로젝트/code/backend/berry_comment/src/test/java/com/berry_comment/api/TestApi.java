package com.berry_comment.api;

import com.berry_comment.property.LastFmProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class TestApi {
    @Autowired
    LastFmProperty lastFmProperty;
    @Test
    void test(){
        RestTemplate restTemplate = new RestTemplate();
        //1.apiKey값
        //2.아티스트
        //3.곡이름
        String apiKey = lastFmProperty.getApiKey();
        String artist = "로제 (ROSÉ), Bruno Mars";
        String songName = "APT";
        String url = String.format("https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=%s&artist=%s&track=%s&format=json", apiKey, artist, songName);
        String value = restTemplate.getForObject(url, String.class);
        System.out.println(value);
    }
}
