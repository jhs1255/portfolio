package com.berry_comment.repository;

import com.berry_comment.entity.Chart;
import com.berry_comment.entity.ChartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ChartRepository extends JpaRepository<Chart, Long> {
    //시간 기준으로 값 입력받기
    @Query(value = "SELECT * FROM chart WHERE DATE_FORMAT(dateTime, '%Y-%m-%d %H') = :localDateTime", nativeQuery = true)
    Chart findByLocalDateTime(@Param("localDateTime") String localDateTime);
}
