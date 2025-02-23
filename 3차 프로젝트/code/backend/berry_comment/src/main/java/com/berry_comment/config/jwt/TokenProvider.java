package com.berry_comment.config.jwt;

import com.berry_comment.entity.RefreshTokenEntity;
import com.berry_comment.entity.UserEntity;
import com.berry_comment.exception.BerryCommentException;
import com.berry_comment.exception.ErrorCode;
import com.berry_comment.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    //토큰 반환
    public String generateToken(UserEntity userEntity, Duration expiration) {
        Date now = new Date();
        return makeToken(new Date( now.getTime() + expiration.toMillis()), userEntity);
    }

    private String makeToken(Date date, UserEntity userEntity) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(date)
                .claim("id", userEntity.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //리프레시 토큰 만드는 함수
    public String generateRefreshToken(UserEntity user, Duration expiration) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByUser(user).orElseThrow(EntityNotFoundException::new);
        Date now = new Date();
        String refreshTokenString = refreshToken.getRefreshToken();

        //만약 리프레시 토큰이 유효하다면
        if(validate(refreshTokenString)) {
            return refreshTokenString;
        }
        //만약 리프레시 토큰이 유효하지 않다면
        else {
            refreshTokenString = makeToken(new Date( now.getTime() + expiration.toMillis() ), user);
            return refreshTokenString;
        }
    }

    public boolean validate(String token) {
        try {
            Claims claims = Jwts
                    .parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            // 만료시간(exp) 체크
            Date expiration = claims.getExpiration();
            System.out.println("만료시간" + expiration.toString());
            System.out.println("현재시간 " + new Date().toString());
            // 만약 토큰이 유효하면 true 반환
            return true;
        }catch (ExpiredJwtException e) {
            return false;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();

    }
}
