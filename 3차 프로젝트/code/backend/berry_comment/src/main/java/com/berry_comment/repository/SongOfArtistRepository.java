package com.berry_comment.repository;

import com.berry_comment.entity.SongOfArtist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongOfArtistRepository extends JpaRepository<SongOfArtist, Long> {
    Page<SongOfArtist> findByArtistId(Long artistId, Pageable pageable);
}
