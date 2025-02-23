package com.berry_comment.dto;

import lombok.*;

import java.util.Map;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlInfoDto {
    //곡 아이디
    private Long songId;

    //곡 이름
    private String song;

    //아티스트
    private List<ArtistDto> artists;

    //앨범
    private String album;

    //이미지 url
    private String url;

    //랭킹
    private int rank;

    //장르
    private String genre;
}
