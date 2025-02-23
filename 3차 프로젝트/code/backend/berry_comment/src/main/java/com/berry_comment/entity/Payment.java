package com.berry_comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    결제 정보 관련 엔티티
*/
@Entity
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    private String id;

    @ManyToOne
    UserEntity user;

    //결제 금액
    @Column(nullable = false)
    private int amount;

    //결제 시간
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Payment(String id, UserEntity user, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
