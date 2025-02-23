package com.berry_comment.dto;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDetailDto {

    //곡아이디
    private int songId;

    //곡이름
    private String songName;

    //가사
    private String songLyrics;

    //앨범이름
    private String albumName;

    //아티스트 이름
    private String artistName;

    private String imageUrl;
}
