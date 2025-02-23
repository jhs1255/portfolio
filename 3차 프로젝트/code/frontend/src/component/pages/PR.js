import React, { useState, useRef } from "react";
import styled from "styled-components";
import logo from "../../images/logo.png";
import ArrowImage from "../../images/Arrow.png";
import { useNavigate } from "react-router-dom";
import Subscribe2 from "../Popup/Subscribe2";

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100vh;
  min-height: 100vh;
  width: 100vw;
  overflow-y: auto;
  background-color: #c69fda;
  padding: 20px 0;
  box-sizing: border-box;
`;

const Logo = styled.img`
  width: 250px;
  margin-top: 50px;
  margin-bottom: 30px;
  cursor: pointer;
`;

const ArrowIcon = styled.img`
  width: 110px;
  height: 110px;
  margin-top: 5px;
`;

const SubscriptionBox = styled.div`
  width: 560px;
  max-width: 90%;
  background-color: rgb(239, 224, 225);
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin-bottom: 50px;
`;

const Title = styled.h2`
  font-size: 1.8rem;
  font-weight: bold;
  color: black;
  margin-bottom: 30px;
`;

const SubscriptionButton = styled.button`
  margin-top: 20px;
  margin-bottom: 15px;
  width: 200px;
  height: 45px;
  font-size: 1rem;
  font-weight: bold;
  color: white;
  background-color: rgb(121, 16, 174);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;

  &:hover {
    background-color: rgb(95, 31, 137);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
`;

const PaymentSection = styled.div`
  width: 560px;
  max-width: 90%;
  background-color: white;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
`;

const PaymentButton = styled.button`
  width: 200px;
  height: 45px;
  font-size: 1rem;
  font-weight: bold;
  color: white;
  background-color: rgb(121, 16, 174);
  border: none;
  border-radius: 5px;
  margin-top: 20px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;

  &:hover {
    background-color: rgb(95, 31, 137);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
`;

const PaymentOptions = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  row-gap: 20px;
  margin-top: 20px;
  justify-items: center;
  padding: 0 40px;
`;

/* 이용 약관 */
const TermsText = styled.p`
  font-size: 0.9rem;
  color: #555;
  text-align: center;
  margin-top: 35px;
`;

const PayWay = styled.button`
  width: 220px;
  height: 50px;
  font-size: 1rem;
  font-weight: bold;
  color: ${({ disabled }) => (disabled ? "#999" : "#333")};
  background-color: ${({ disabled }) => (disabled ? "#e0e0e0" : "#f7c600")};
  border: none;
  border-radius: 10px;
  cursor: ${({ disabled }) => (disabled ? "not-allowed" : "pointer")};
  transition: background-color 0.3s;

  &:hover {
    background-color: ${({ disabled }) => (disabled ? "#e0e0e0" : "#f1b600")};
  }
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20px;
`;

const CancelButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 15px;
`;

const CancelButton = styled.button`
  width: 150px;
  height: 45px;
  font-size: 1rem;
  color: rgb(94, 50, 110);
  background-color: rgba(122, 122, 122, 0);
  border: 1px solid rgb(94, 50, 110);
  border-radius: 5px;
  margin-top: 30px;
  margin-bottom: 50px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: rgba(252, 245, 255, 0.59);
  }
`;

function PR() {
  const navigate = useNavigate();
  const paymentRef = useRef(null);
  const [isSubscribe2Open, setIsSubscribe2Open] = useState(false);

  // 해지 팝업 열기
  const handleCancelSubscription = () => {
    setIsSubscribe2Open(true);
  };

  const scrollToPayment = () => {
    paymentRef.current.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  //로고
  const handleLogoClick = () => {
    navigate("/");
  };

  //카카오페이 결제 창
  const handlePayment = async () => {
    const token = sessionStorage.getItem("access_token"); // 토큰 가져오기

    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/payment/ready", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`, // 토큰을 Authorization 헤더에 포함
          "Content-Type": "application/json",
        },
      });
  
      if (!response.ok) {
        throw new Error("결제 요청에 실패했습니다.");
      }
  
      const data = await response.json();
      window.location.href = data.next_redirect_pc_url; // 카카오 결제 페이지로 이동
  
    } catch (error) {
      console.error("결제 요청 중 오류 발생:", error);
      alert("결제 요청을 처리하는 중 오류가 발생했습니다.");
    }

  };

  return (
    <Wrapper>
      <Logo src={logo} alt="Berrecommend 로고" onClick={handleLogoClick} />{" "}
      <Title>베리코멘드만의 특별한 혜택을 확인하세요!</Title>
      <SubscriptionBox>
        <h1>BASIC</h1>
        <h3>음악 재생 + 플레이리스트 생성 가능</h3>

        <ArrowIcon src={ArrowImage} alt="화살표" />

        <h1>PREMIUM</h1>
        <h3>BASIC 기능 + AI 추천 플레이리스트 재생</h3>
        <SubscriptionButton onClick={scrollToPayment}>
          구독하러 가기
        </SubscriptionButton>
        <p>
          <strong>월 5,900원에 제한 없이 모든 음악을 감상하세요</strong>
        </p>
      </SubscriptionBox>
      <Title>결제 정보</Title>
      <PaymentSection ref={paymentRef}>
        <h3>베리코멘드 PREMIUM 5,900원/월</h3>
        <Title>결제 수단 선택</Title>
        <PaymentOptions>
          <PayWay disabled>신용카드</PayWay>
          <PayWay disabled>체크카드</PayWay>
          <PayWay disabled>휴대폰 결제</PayWay>
          <PayWay>카카오페이</PayWay>
        </PaymentOptions>

        <TermsText>
          [결제] 버튼을 누르면 이용 약관, 개인정보 처리 방침에 동의하는 것으로
          간주됩니다.
        </TermsText>

        <ButtonWrapper>
          <PaymentButton onClick={handlePayment} >결제하기</PaymentButton>
        </ButtonWrapper>
      </PaymentSection>
      <CancelButtonWrapper>
        {/* 🔥 해지하기 버튼 클릭 시 팝업 열기 */}
        <CancelButton onClick={handleCancelSubscription}>해지하기</CancelButton>
      </CancelButtonWrapper>
      {/* 🔥 Subscribe2 팝업 추가 (isOpen 상태에 따라 표시) */}
      {isSubscribe2Open && (
        <Subscribe2
          isOpen={isSubscribe2Open}
          onClose={() => setIsSubscribe2Open(false)}
        />
      )}
    </Wrapper>
  );
}

export default PR;
