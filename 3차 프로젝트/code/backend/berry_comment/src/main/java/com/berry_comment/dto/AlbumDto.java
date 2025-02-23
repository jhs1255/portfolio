package com.berry_comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {
    //앨범 아이디
    private int id;

    //앨범 이름
    private String name;

    //아티스트 이름
    private String artist;

    //이미지 url
    private String url;
}
