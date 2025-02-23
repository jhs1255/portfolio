package com.berry_comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@NoArgsConstructor
@Getter
public class Chart {
    //차트 정보를 나타내는 데이터베이스...?
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    //생성된 차트날짜
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "chart", cascade = CascadeType.REMOVE)
    private List<ChartDetail> chartDetailList;

    public Chart(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
