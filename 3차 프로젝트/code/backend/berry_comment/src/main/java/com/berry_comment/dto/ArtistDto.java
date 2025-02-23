package com.berry_comment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDto {
    private int id;
    private String artistName;
    private String imageUrl;
}
