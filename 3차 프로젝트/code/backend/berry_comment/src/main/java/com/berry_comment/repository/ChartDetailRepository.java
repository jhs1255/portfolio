package com.berry_comment.repository;

import com.berry_comment.entity.ChartDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartDetailRepository extends JpaRepository<ChartDetail, Long> {
    Slice<ChartDetail> findByChartId(Long chartId, Pageable pageable);
}
