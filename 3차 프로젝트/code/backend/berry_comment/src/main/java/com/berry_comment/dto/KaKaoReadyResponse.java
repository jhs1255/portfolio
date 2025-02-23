package com.berry_comment.dto;

import lombok.Data;

@Data
public class KaKaoReadyResponse {
    private String tid;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String created_at;
}
