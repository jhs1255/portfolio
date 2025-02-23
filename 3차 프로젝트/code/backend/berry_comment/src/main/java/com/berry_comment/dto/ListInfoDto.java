package com.berry_comment.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListInfoDto {
    private int size;
    private List<?> dataList;
}
