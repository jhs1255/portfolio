package com.berry_comment.config.oauth;

import com.berry_comment.config.jwt.TokenProvider;
import com.berry_comment.entity.UserEntity;
import com.berry_comment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    //리프레시 토큰 유효시간
    public static final Duration REFRESH_TOKEN_TIMEOUT = ConstantValue.REFRESH_TOKEN_EXPIRE;

    //액세스 토큰 유효시간
    public static final Duration ACCESS_TOKEN_TIMEOUT = ConstantValue.ACCESS_TOKEN_EXPIRE;

    public static final String URI = "/user/login/oauth2/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("인증에 성공하셨습니다.");
        //accessToken 및 refreshToken 발급
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(oAuth2User.getAttributes().get("email").toString())
                .orElseThrow(EntityNotFoundException::new);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_TIMEOUT);
        String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_TIMEOUT);

//        // JSON 형태로 응답 설정
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("{\"access_token\": \"%s\", \"refresh_token\": \"%s\"}", accessToken, refreshToken);
        // 리디렉션 (localhost:3030으로 이동)
//        response.sendRedirect("http://localhost:3030");
        response.sendRedirect("http://localhost:3030?access_token=" + accessToken + "&refresh_token=" + refreshToken);
//        response.getWriter().write(jsonResponse);
//        response.getWriter().flush();
    }
}
