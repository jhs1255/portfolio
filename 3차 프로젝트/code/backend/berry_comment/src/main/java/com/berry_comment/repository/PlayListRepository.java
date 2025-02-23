package com.berry_comment.repository;

import com.berry_comment.entity.PlayList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    Slice<PlayList> findAllByUserId(String userId, Pageable pageable);
}
