package com.berry_comment.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class SongChartDto {
    private int size;
    private ArrayList<SongRankDto> song;
}
