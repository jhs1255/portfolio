package com.berry_comment.controller;

import com.berry_comment.dto.PasswordDto;
import com.berry_comment.dto.UserDto;
import com.berry_comment.oauth.PrincipalDetails;
import com.berry_comment.service.UserService;
import com.berry_comment.type.EditType;
import com.berry_comment.type.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody PasswordDto passwordDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUser().getId();
        System.out.println(userId);
        UserDto userDto = userService.getUserInfo(userId, passwordDto.getPassword());
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> edit(
            @RequestParam EditType editType,
            @RequestBody Map<String, String> valueMap,
            Authentication authentication
    ) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUser().getEmail();
        String value = valueMap.get("value");
        Map<String, String> map = new HashMap<>();
        switch (editType) {
            case PASSWORD:
                String userPassword = userService.updatePassword(userId, value);
                map.put("password", userPassword);
                return ResponseEntity.ok(map);

            case NICKNAME:
                String nickname = userService.updateNickName(userId, value);
                map.put("nickname", nickname);
                return ResponseEntity.ok(map);

            case EMAIL:
                String updatedEmail = userService.updateEmail(userId, value);
                map.put("email", updatedEmail);
                return ResponseEntity.ok(map);

                default:
                    return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rank")
    public ResponseEntity<?> getRank(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUser().getId();
        RoleUser rank = userService.getRank(userId);
        Map<String, String> userMap = new HashMap<>();
        userMap.put("rank", rank.getRoleName().replace("ROLE_", ""));
        return ResponseEntity.ok(userMap);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        String nickname = principalDetails.getUser().getNickname(); // 닉네임 추가

        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        response.put("nickname", nickname); // 닉네임도 응답에 포함

        return ResponseEntity.ok(response);
    }



}
