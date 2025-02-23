package com.berry_comment.oauth;

public interface OAuth2UserInfo {
    String getName();
    String getProviderId();
    String getEmail();
    String getProvider();
}
