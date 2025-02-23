package com.berry_comment.service;

import com.berry_comment.property.LastFmProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LastFmService {
    private final LastFmProperty lastFmProperty;
    public JsonNode getSongPlayTime(String track, String artist) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String apiKey = lastFmProperty.getApiKey();
        //정규식을 이용해서 영어만 가져오기
        artist = artist.replaceAll("[^A-Za-z]", "").trim();
        String url = String.format("https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=%s&artist=%s&track=%s&format=json", apiKey, artist, track);
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response);
    }
}
