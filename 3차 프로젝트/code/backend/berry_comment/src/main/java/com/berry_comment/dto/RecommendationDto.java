package com.berry_comment.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecommendationDto {
    private int id;
    private String title;
}
