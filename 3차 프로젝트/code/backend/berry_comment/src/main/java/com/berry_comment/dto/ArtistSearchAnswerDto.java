package com.berry_comment.dto;


import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistSearchAnswerDto {
    //아티스트 아이디
    private int id;

    //아티스트 이름
    private String name;

    //아티스트 이미지
    private String image;
    //아티스트 장르
    private List<String> genre;

}
