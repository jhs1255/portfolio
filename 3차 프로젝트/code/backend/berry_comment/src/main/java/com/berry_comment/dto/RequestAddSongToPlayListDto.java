package com.berry_comment.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RequestAddSongToPlayListDto {
    private List<Integer> playlistIds;
    private int songId;
}
