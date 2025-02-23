package com.berry_comment.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleUser {
    PREMIUM("ROLE_PREMIUM"),
    NORMAL("ROLE_NORMAL"),;
    private final String roleName;
}
