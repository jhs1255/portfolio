package com.berry_comment.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final RedisUtils redisUtils;

    /**
     * HTML 이메일 템플릿을 불러오는 함수
     */
    private String readHtmlTemplate(String title, String content) {
        try {
            String template = new String(Files.readAllBytes(Paths.get(new ClassPathResource("templates/mail-template.html").getURI())), StandardCharsets.UTF_8);
            return template.replace("{{TITLE}}", title).replace("{{CONTENT}}", content);
        } catch (Exception e) {
            throw new RuntimeException("이메일 템플릿 로드 실패", e);
        }
    }

    /**
     * 공통 HTML 이메일 발송 함수
     */
    private void sendHtmlEmail(String email, String subject, String title, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // HTML 템플릿 적용
            String htmlContent = readHtmlTemplate(title, content);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML 적용

            mailSender.send(message);
            log.info("📧 이메일 발송 성공! (" + subject + ")");
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패: " + subject, e);
        }
    }

    /**
     * 회원가입 성공 메일
     */
    public void sendMailJoinSuccess(String email) {
        sendHtmlEmail(email, "🎉 Berrecommend 회원가입을 축하합니다!", "회원가입이 완료되었습니다.", "Berrecommend에 가입해 주셔서 감사합니다.<br>즐거운 경험 되시길 바랍니다!");
    }

    /**
     * 비밀번호 변경 메일
     */
    public void sendMailPasswordUpdate(String email, String password) {
        sendHtmlEmail(email, "비밀번호 변경 안내", "비밀번호가 변경되었습니다.",
                "새로운 비밀번호: <b>" + password + "</b><br>보안을 위해 로그인 후 즉시 변경해주세요.<br>안전한 서비스 이용을 바랍니다!");
    }

    /**
     * 이메일 인증 코드 발송
     */
    public void sendMailValidation(String email, String code) {
        sendHtmlEmail(email, "이메일 인증 코드 안내", "이메일 인증 코드",
                "인증 번호: <b>" + code + "</b><br>인증 코드를 입력하여 이메일을 확인해주세요.<br>이 코드는 5분 동안 유효합니다.");
    }

    /**
     * 이메일 인증 코드 확인
     */
    public Boolean checkEmail(@Email String email, @NotEmpty String password) {
        String validatePass = new String(redisUtils.getData(email).toString().getBytes(), StandardCharsets.UTF_8);
        if (validatePass.equals(password)) {
            redisUtils.delData(email);
            return true;
        }
        return false;
    }
}
