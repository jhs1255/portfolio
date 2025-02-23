package com.berry_comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SongOfArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn
    //해당 노래를 부른 아티스트들
    private Artist artist;

    @ManyToOne
    @JoinColumn
    private Song song;

    public SongOfArtist(Artist artist, Song song) {
        this.artist = artist;
        this.song = song;
    }


}
