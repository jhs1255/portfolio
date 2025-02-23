package com.berry_comment.repository;

import com.berry_comment.entity.RecommendationDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RecommendationDetailRepository extends JpaRepository<RecommendationDetail, Integer> {
    Slice<RecommendationDetail> findByRecommendationId(Integer recommendationId, Pageable pageable);
    List<RecommendationDetail> findAllByRecommendationId(Integer recommendationId);
}
