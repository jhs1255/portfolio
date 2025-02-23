package com.berry_comment.repository;

import com.berry_comment.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface SongRepository extends JpaRepository<Song,Long> {
    Song findById(long id);

    //특정 문자열이 들어간 음악을 찾습니다.
    Slice<Song> findByTrackContaining(String track, Pageable pageable);

    @Query("SELECT s from Song s WHERE s.id IN(SELECT rd.song.id FROM RecommendationDetail rd where rd.recommendation.id = :id)")
    Page<Song> findByRecommendation(@Param("id") long id, Pageable pageable);
}
