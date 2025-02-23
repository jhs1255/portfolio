package com.berry_comment.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class AlbumDetailDto {
    //앨범 아이디
    private int id;

    //아티스트
    private String artist;

    //앨범 이름
    private String album;

    //곡 목록들
    private ListInfoDto listInfo;
}
