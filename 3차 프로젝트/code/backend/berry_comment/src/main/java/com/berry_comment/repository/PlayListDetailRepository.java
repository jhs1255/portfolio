package com.berry_comment.repository;

import com.berry_comment.entity.PlayList;
import com.berry_comment.entity.PlayListDetail;
import com.berry_comment.entity.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlayListDetailRepository extends JpaRepository<PlayListDetail, Integer> {
    @Query("SELECT p.song.id from PlayListDetail p WHERE p.playList.id = :id")
    Slice<Long> findSongIdByPlayListId(Long id, Pageable pageable);
    boolean existsByPlayListAndSong(PlayList playList, Song song);
}
