package com.berry_comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
@Entity
@NoArgsConstructor
@Getter
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, length = 50)
    private String title;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.REMOVE)
    private List<RecommendationDetail>recommendationDetailList;

    public Recommendation(String title) {
        this.title = title;
    }
}
