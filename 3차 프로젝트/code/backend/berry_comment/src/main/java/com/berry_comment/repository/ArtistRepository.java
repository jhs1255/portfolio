package com.berry_comment.repository;

import com.berry_comment.entity.Artist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByName(String name);

    Slice<Artist> findByNameContaining(String name, Pageable pageable);
}
