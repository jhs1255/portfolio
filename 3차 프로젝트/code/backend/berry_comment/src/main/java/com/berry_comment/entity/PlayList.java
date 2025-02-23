package com.berry_comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@NoArgsConstructor
public class PlayList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //플레이리스트 아이디
    private Long id;

    //플레이리스트 이름
    @Column(nullable = false, length = 20)
    private String playListName;


    @OneToMany(mappedBy = "playList", cascade = CascadeType.REMOVE)
    private List<PlayListDetail> playListSongInfoList;
    @ManyToOne
    @JoinColumn
    private UserEntity user;

    public PlayList(String playListName, UserEntity user) {
        this.playListName = playListName;
        this.user = user;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
}
