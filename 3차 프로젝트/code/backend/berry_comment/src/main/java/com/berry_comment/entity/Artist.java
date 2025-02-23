package com.berry_comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Entity
@NoArgsConstructor
@Getter
//테스트 배포때 지울것
@ToString
public class Artist {
    @Id
    private Long id;

    //아티스트 이름
    @Column(nullable = false, length = 40)
    private String name;

    @OneToMany(mappedBy = "artist")
    private List<SongOfArtist> Songs;

    //아티스트 대표 이미지
    @Column(nullable = false, length = 200)
    private String image;

    public Artist(Long id,String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
}
