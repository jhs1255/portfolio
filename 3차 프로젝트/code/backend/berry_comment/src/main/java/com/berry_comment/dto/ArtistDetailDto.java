package com.berry_comment.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistDetailDto {
    private ArtistDto artist;

    private ListInfoDto songList;

    private ListInfoDto albumList;
}
