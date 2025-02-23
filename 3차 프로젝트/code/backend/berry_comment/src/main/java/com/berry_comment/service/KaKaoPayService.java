package com.berry_comment.service;

import com.berry_comment.dto.KaKaoReadyResponse;
import com.berry_comment.dto.KakaoApproveResponse;
import com.berry_comment.entity.Payment;
import com.berry_comment.entity.UserEntity;
import com.berry_comment.exception.BerryCommentException;
import com.berry_comment.exception.ErrorCode;
import com.berry_comment.property.KakaoPayProperty;
import com.berry_comment.repository.PaymentRepository;
import com.berry_comment.repository.UserRepository;
import com.berry_comment.type.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KaKaoPayService {
    private final KakaoPayProperty kakaoPayProperty;
    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private KaKaoReadyResponse kaKaoReadyResponse;
    private final UserRepository userRepository;
    //주문 번호
    private String partner_order_id;

    //결제할 때 쓰이는유저 아이디
    private String userpay_id;

    //일반 유저아이디
    private String userId;



    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + kakaoPayProperty.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public KaKaoReadyResponse kaKaoReady(String userId) {
        UserEntity userEntity = userService.getUserById(userId);
        //이미 결제한 유저라면
        if(userEntity.getRoleUser() == RoleUser.PREMIUM) {
            throw new BerryCommentException(ErrorCode.ALREADY_PAID, ErrorCode.ALREADY_PAID.getMessage());
        }
        Map<String, Object> params = new HashMap<>();
        this.userId = userId;
        //테스트용 가맹정 이름
        params.put("cid", kakaoPayProperty.getCid());
        //주문번호는 유저아이디 + 결제일
        LocalDateTime localDateTime = LocalDateTime.now();
        String order_id = userId + "_" + localDateTime.toString();
        partner_order_id = order_id;
        params.put("partner_order_id",partner_order_id);

        userpay_id = userId;
        //파트너 유저아이디에 유저아이디를 지정
        params.put("partner_user_id", userpay_id);

        
        //아이템 이름
        params.put("item_name",kakaoPayProperty.getItemName());
        
        //수량
        params.put("quantity", String.valueOf(kakaoPayProperty.getQuantity()));
        
        //총 금액
        params.put("total_amount", String.valueOf(kakaoPayProperty.getCost()));

        //비과세 금액
        params.put("tax_free_amount", String.valueOf(kakaoPayProperty.getTaxFreeAmount()));

        params.put("approval_url", kakaoPayProperty.getApproveUrl());
        params.put("fail_url", kakaoPayProperty.getFailUrl());
        params.put("cancel_url", kakaoPayProperty.getCancelUrl());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        kaKaoReadyResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                request,
                KaKaoReadyResponse.class
        );
        return kaKaoReadyResponse;
    }

    //결제 완료시..
    public KakaoApproveResponse approveResponse(String pgToken){
        // 카카오 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", kakaoPayProperty.getCid());
        parameters.put("tid", kaKaoReadyResponse.getTid());
        parameters.put("partner_order_id", partner_order_id);
        parameters.put("partner_user_id", userpay_id);
        parameters.put("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);
        addPaymentEntity(approveResponse);
        return approveResponse;
    }

    @Transactional
    public void addPaymentEntity(KakaoApproveResponse approveResponse) {
        UserEntity user = userService.getUserEntity(userId);
        System.out.println("유저 아이디" + user.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // 문자열을 LocalDateTime으로 변환
        LocalDateTime localDateTime = LocalDateTime.parse(approveResponse.getApproved_at(), formatter);
        Payment payment = new Payment(approveResponse.getTid(), user, approveResponse.getAmount().getTotal(), localDateTime);
        paymentRepository.save(payment);

        //결제 후 --> 프리미엄으로 전환
        user.setRoleUser(RoleUser.PREMIUM);
        userRepository.save(user);
    }

    @Transactional
    public void cancelSubscription(String tid, String userId) {
        UserEntity user = userService.getUserById(userId);

        // 해당 tid를 가진 결제 내역 찾기
        Optional<Payment> paymentOptional = paymentRepository.findById(tid);
        if (paymentOptional.isEmpty()) {
            throw new BerryCommentException(ErrorCode.NOT_FOUND, "결제 정보를 찾을 수 없습니다.");
        }

        Payment payment = paymentOptional.get();

        // 결제한 유저가 현재 유저와 동일한지 확인
        if (!payment.getUser().getId().equals(user.getId())) {
            throw new BerryCommentException(ErrorCode.FORBIDDEN, "결제 취소 권한이 없습니다.");
        }

        // 결제 정보 삭제
        paymentRepository.delete(payment);

        // 유저 권한을 NORMAL로 변경
        user.setRoleUser(RoleUser.NORMAL);
        userRepository.save(user);
    }
}
