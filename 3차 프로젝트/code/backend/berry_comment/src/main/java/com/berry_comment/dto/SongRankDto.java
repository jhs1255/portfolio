package com.berry_comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class SongRankDto {
    //랭크
    private int rank;

    //곡 아이디
    private int id;

    //곡 이름
    private String track;

    //아티스트 이름
    private String artist;

    //이미지 경로
    private String image;

    //재생시간
    private int playTime;

    //앨범이름
    private String album;
}
