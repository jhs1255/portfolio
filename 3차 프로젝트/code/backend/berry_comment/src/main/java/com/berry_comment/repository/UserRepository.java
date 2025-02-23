package com.berry_comment.repository;

import com.berry_comment.entity.Payment;
import com.berry_comment.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    UserEntity findById(String id);
    @Query("SELECT p FROM Payment p WHERE p.user = :user ORDER BY p.createdAt DESC")
    Optional<Payment> findLatestPaymentByUser(@Param("user") UserEntity user);

    @Query("SELECT p.user from Payment  p WHERE p.createdAt < :localDateTime")
    Page<UserEntity> findLatestPayments(@Param("localDateTime") LocalDateTime localDateTime, Pageable pageable);
}
