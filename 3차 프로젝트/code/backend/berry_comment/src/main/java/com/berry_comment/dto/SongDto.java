package com.berry_comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
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

    private String album;
}
