package com.berry_comment.service;

import com.berry_comment.config.jwt.TokenProvider;
import com.berry_comment.config.oauth.ConstantValue;
import com.berry_comment.dto.*;
import com.berry_comment.entity.RefreshTokenEntity;
import com.berry_comment.entity.UserEntity;
import com.berry_comment.exception.BerryCommentException;
import com.berry_comment.exception.ErrorCode;
import com.berry_comment.oauth.PrincipalDetails;
import com.berry_comment.repository.RefreshTokenRepository;
import com.berry_comment.repository.UserRepository;
import com.berry_comment.type.RoleUser;
import com.berry_comment.type.TypeUser;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RedisUtils redisUtils;
    private final TokenProvider tokenProvider;
    private final PlayListService playListService;
    //액세스 토큰 만료시간
    private final Duration ACCESSTOKEN_DURATION = ConstantValue.ACCESS_TOKEN_EXPIRE;

    //리프레시 토큰 만료시간
    private final Duration REFRESH_TOKEN_DURATION = ConstantValue.REFRESH_TOKEN_EXPIRE;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserEntity getUserEntity(String userId) {
        return userRepository.findById(userId);
    }
    public String getUserIdByAuthentication(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return principalDetails.getUser().getId();

    }

    public String updatePassword(String userEmail, String password) {
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElse(null);
        if (userEntity == null) {
            throw new EntityNotFoundException("해당하는 사용자가 없습니다..");
        }
        String passwordEncode = passwordEncoder.encode(password);
        userEntity.setUserPassword(passwordEncode);
        userRepository.save(userEntity);
        return password;
    }


    public void validateUserCheckByEmailAndId(@Email String email, @NotNull String id) {
        System.out.println(id);
        UserEntity userEntity = userRepository.findById(id);
        if (userEntity == null) {
            throw new EntityNotFoundException("해당하는 유저가 없습니다.");
        }

        System.out.println("유저 아이디" + id);
        System.out.println(userEntity.getId());

        if(!userEntity.getEmail().equals(email)) {
            throw new EntityNotFoundException("해당하는 유저가 없습니다.");
        }
        String temp = redisUtils.setPassword(email);
        mailService.sendMailValidation(userEntity.getEmail(), temp);
    }

    public TokenDto joinUser(JoinDto joinDto) {
        UserEntity user = getUserById(joinDto.getId());
        UserEntity user2 = userRepository.findByEmail(joinDto.getEmail()).orElse(null);
        //만약 이미 회원가입을 한 아이디이거나 이메일이 있다면
        if (user != null || user2 != null) {
            throw new DuplicateKeyException("이미 가입한 유저입니다.");
        }
        String userId = joinDto.getId();
        String userEmail = joinDto.getEmail();
        String name = joinDto.getName();
        String nickName = "Anonymous";
        String password = joinDto.getPassword();
        password = passwordEncoder.encode(password);
        UserEntity userEntity = new UserEntity(userId, name, password, userEmail, nickName, RoleUser.NORMAL, TypeUser.NORMAL_USER);
        userRepository.save(userEntity);
        String accessToken = tokenProvider.generateToken(userEntity, ACCESSTOKEN_DURATION);
        String refreshToken = tokenProvider.generateToken(userEntity, REFRESH_TOKEN_DURATION);
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken, userEntity);
        refreshTokenRepository.save(refreshTokenEntity);
        //내가 좋아하는 플레이리스트 만들기
        System.out.println("유저저장 완료");
        playListService.createMyFavouriteSongList(userEntity);
        mailService.sendMailJoinSuccess(userEmail);
        return TokenDto.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
    }

    public UserEntity getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public TokenDto login(LoginFormDto loginFormDto) {
        String userId = loginFormDto.getId();
        String password = loginFormDto.getPassword();
        UserEntity userEntity = userRepository.findById(userId);
        if (userEntity == null || !passwordEncoder.matches(password, userEntity.getPassword()) || userEntity.getTypeUser() != TypeUser.NORMAL_USER) {
            throw new EntityNotFoundException("해당하는 유저가 없습니다.");
        }
        String accessToken = tokenProvider.generateToken(userEntity, ACCESSTOKEN_DURATION);
        String refreshToken = tokenProvider.generateRefreshToken(userEntity, REFRESH_TOKEN_DURATION);
        return TokenDto.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
    }

    public IdDto findId(String userEmail, String userName){
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElse(null);
        if (userEntity == null || !userEntity.getName().equals(userName) || userEntity.getTypeUser() != TypeUser.NORMAL_USER) {
            throw new EntityNotFoundException("해당하는 사용자를 찾을 수 없습니다.");
        }
        return IdDto.builder()
                .id(userEntity.getId())
                .build();
    }

    public UserDto getUserInfo(String userId, @NotBlank String password) {
        UserEntity userEntity = userRepository.findById(userId);
        if (userEntity == null || !passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new EntityNotFoundException("해당하는 유저가 없습니다.");
        }
        UserDto userDto = getUserDto(userEntity);
        userDto.setPassword(password);
        return userDto;
    }

    public UserDto getUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .build();
    }

    public String updateNickName(String userId,String value) {
        UserEntity user = userRepository.findByEmail(userId).orElse(null);
        if (user== null)
            throw new EntityNotFoundException("해당하는 유저가 없습니다.");
        user.setnickname(value);
        userRepository.save(user);
        return value;
    }

    public RoleUser getRank(String userId) {
        UserEntity userEntity = userRepository.findById(userId);
        if (userEntity == null)
            throw new EntityNotFoundException("해당하는 유저가 없습니다.");
        return userEntity.getRoleUser();
    }

    public TokenDto refreshToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.replace("Bearer ", "");
        Boolean tokenValid = tokenProvider.validate(token);
        if (tokenValid){
            Claims claims = tokenProvider.getClaims(token);
            String userId = claims.get("id").toString();
            UserEntity user = userRepository.findById(userId);
            if (user == null)
                throw new EntityNotFoundException("해당하는 유저가 없습니다.");
            String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_DURATION);
            String accessToken = tokenProvider.generateToken(user, ACCESSTOKEN_DURATION);
            return TokenDto.builder()
                    .access_token(accessToken)
                    .refresh_token(refreshToken)
                    .build();
        }
        else
            throw new BerryCommentException(ErrorCode.TOKEN_INVALID, ErrorCode.TOKEN_INVALID.getMessage());
    }

    public String updateEmail(String userId, String newEmail) {
        UserEntity user = userRepository.findByEmail(userId).orElse(null);

        if (user == null) {
            throw new EntityNotFoundException("해당하는 사용자가 없습니다.");
        }

        // 이메일 형식 검사
        if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }

        // 이메일 중복 확인
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new DuplicateKeyException("이미 존재하는 이메일입니다.");
        }

        user.setEmail(newEmail);
        userRepository.save(user);

        return user.getEmail(); // 업데이트된 이메일 반환
    }

}
