package com.berry_comment.repository;

import com.berry_comment.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByName(String albumName);

    Slice<Album> findByNameContaining(String albumName, Pageable pageable);

    @Query("SELECT a FROM Album a WHERE a.id IN (SELECT soa.song.album.id FROM SongOfArtist soa WHERE soa.artist.id = :id)")
    Page<Album> findAlbumsBySongOfArtistId(@Param("id") Long id, Pageable pageable);
}
