package com.berry_comment.config.oauth;

import com.berry_comment.config.jwt.PasswordProperties;
import com.berry_comment.config.jwt.TokenProvider;
import com.berry_comment.entity.RefreshTokenEntity;
import com.berry_comment.entity.UserEntity;
import com.berry_comment.oauth.OAuth2UserInfo;
import com.berry_comment.oauth.PrincipalDetails;
import com.berry_comment.oauth.provider.GoogleUserInfo;
import com.berry_comment.repository.RefreshTokenRepository;
import com.berry_comment.repository.UserRepository;
import com.berry_comment.service.MailService;
import com.berry_comment.service.PlayListService;
import com.berry_comment.type.RoleUser;
import com.berry_comment.type.TypeUser;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrincipalOauthUserService extends DefaultOAuth2UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PasswordProperties passwordProperties;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PlayListService playListService;
    //리프레시 토큰 유효시간
    public static final Duration REFRESH_TOKEN_TIMEOUT = ConstantValue.REFRESH_TOKEN_EXPIRE;
    private final MailService mailService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo;
        //회원가입을 하면 일반 유저임..
        RoleUser role = RoleUser.NORMAL;

        //유저 무슨 유저인가
        TypeUser typeUser;

        //Oauth2로그인 종류 확인
        //구글일때
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            typeUser = TypeUser.GOOGLE_USER;
        }else {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT));
        }

        //provider --> oauth2가 구글인지, 네이버인지, 카카인지 구분
        String provider = oAuth2UserInfo.getProvider();

        //사용자 식별자
        String providerId = oAuth2UserInfo.getProviderId();

        //사용자 이름
        String name = oAuth2UserInfo.getName();

        //사용자 이메일
        String email = oAuth2UserInfo.getEmail();

        //사용자 패스워드
        //sub를 암호화하여 비밀번호로 변경
        String id = provider + "_" + providerId;

        //패스워크 인코딩
        //application.yml의 비밀키 값으로 인코딩
        String password = passwordEncoder.encode(passwordProperties.getSecretKey());
        Optional<UserEntity> user = userRepository.findByEmail(email);
        //만약 없는 유저라면 회원가입 진행
        if(user.isEmpty()){
            UserEntity newUser = UserEntity
                    .builder()
                    .id(id)
                    .name(name)
                    .email(email)
                    .password(password)
                    .roleUser(role)
                    .nickname("Anonymous")
                    //닉네임 추가하는 로직 작성
                    .typeUser(typeUser)
                    .build();
            userRepository.save(newUser);

            //refreshToken 생성 및 저장하기
            String refreshToken = tokenProvider.generateToken(newUser, REFRESH_TOKEN_TIMEOUT);
            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken, newUser);
            refreshTokenRepository.save(refreshTokenEntity);

            //내가 좋아하는 플레이리스트 만들기
            System.out.println("유저저장 완료");
            playListService.createMyFavouriteSongList(newUser);
            mailService.sendMailJoinSuccess(email);
            return new PrincipalDetails(newUser, oAuth2User.getAttributes());
        }
        else {
            return new PrincipalDetails(user.get(), oAuth2User.getAttributes());
        }
    }
}
