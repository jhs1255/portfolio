package com.berry_comment.controller;

import com.berry_comment.dto.*;
import com.berry_comment.service.MailService;
import com.berry_comment.service.RedisUtils;
import com.berry_comment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserLoginController {
    private final MailService mailService;
    private final UserService userService;
    private final RedisUtils redisUtils;

    //Oauth 로그인시 다시 리다이렉트 되는 경로입니다....
    //구현하기 개빡세네
    //왜 되는지는 모르겠는데 일단 둡시다..
    @GetMapping("/login/oauth2/success")
    public ResponseEntity<TokenResponseDto> oauth2Success(HttpServletRequest request) {
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(request.getSession().getAttribute("access_token").toString());
        tokenResponseDto.setRefreshToken(request.getSession().getAttribute("refresh_token").toString());
        return ResponseEntity.ok().body(tokenResponseDto);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserInfo(){
        return ResponseEntity.ok("This is the user profile");
    }

    @GetMapping("/oauth/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "http://localhost:8080/oauth2/authorization/google";
        response.sendRedirect(url);
    }

    @PostMapping("/form/login")
    public ResponseEntity<?> loginForm(@RequestBody LoginFormDto loginFormDto) throws IOException {
        TokenDto tokenDto = userService.login(loginFormDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        TokenDto tokenDto = userService.joinUser(joinDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> validDateUser(@RequestBody EmailCheckDto emailCheckDto){
        Boolean checked = mailService.checkEmail(emailCheckDto.getEmail(), emailCheckDto.getPassword());
        if (checked){
            //임시 비밀번호 발급
            String password = redisUtils.createTempPass();
            String passwordUpdate = userService.updatePassword(emailCheckDto.getEmail(),password);
            mailService.sendMailPasswordUpdate(emailCheckDto.getEmail(),passwordUpdate);
            return ResponseEntity.ok("");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestBody EmailAndIdCheckDto emailAndNameCheckDto){
        userService.validateUserCheckByEmailAndId(emailAndNameCheckDto.getEmail(), emailAndNameCheckDto.getId());
        return ResponseEntity.ok("");
    }

    @PostMapping("/get-id")
    public ResponseEntity<IdDto> getUserById(@RequestBody IdRequestDto idRequestDto){
        IdDto idDto = userService.findId(idRequestDto.getEmail(), idRequestDto.getName());
        return ResponseEntity.ok(idDto);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request){
        TokenDto tokenDto = userService.refreshToken(request);
        return ResponseEntity.ok(tokenDto);
    }
}
