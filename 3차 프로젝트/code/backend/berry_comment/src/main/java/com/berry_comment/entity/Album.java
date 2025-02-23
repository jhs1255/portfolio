package com.berry_comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@ToString
@Getter
public class Album {
    @Id
    private Long id;

    //대표 이미지
    @Column(nullable = false)
    private String imageUrl;

    //앨범 이름
    @Column(nullable = false)
    private String name;

    //음악
    @OneToMany(mappedBy = "album",fetch = FetchType.LAZY)
    private List<Song> songList;


    public Album(Long id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }
}
