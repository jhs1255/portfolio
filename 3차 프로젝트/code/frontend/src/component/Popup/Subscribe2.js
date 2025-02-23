import React from "react";
import Modal, { ModalButton } from "./Modal"; // 같은 폴더 내 Modal 사용
import styled from "styled-components";
import { useNavigate } from "react-router-dom"; // 네비게이션 추가

const Text = styled.p`
  font-size: 1.1em;
  text-align: center;
  color: white;
  margin-bottom: 20px;
`;

const Subscribe2 = ({ isOpen, onClose }) => {
  const navigate = useNavigate(); // useNavigate 훅 추가

  // const onConfirm = () => {
  //   alert("구독이 해지되었습니다."); // 해지 완료 alert
  //   navigate("/"); // main.js로 이동
  // };
  const onConfirm = async () => {
    const tidToken= sessionStorage.getItem("tidToken");
    const accessToken = sessionStorage.getItem("access_token");
  
    if (!tidToken) {
      alert("구독 정보가 없습니다.");
      return;
    }

    console.log("Authorization Header:", `Bearer ${accessToken}`);
    console.log("Request Body:", JSON.stringify({ tid: tidToken }));
  
    try {
      const response = await fetch("http://localhost:8080/payment/cancel-subscription", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${accessToken}`,  // 인증 토큰을 헤더에 포함
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ tid: tidToken })  // ✅ 바디에 JSON 데이터로 tid 포함
      });
  
      if (response.ok) {
        alert("구독이 성공적으로 해지되었습니다.");
        sessionStorage.removeItem("tidToken"); // sessionStorage에서 tid 삭제
        window.location.href = "/"; // 메인 페이지로 이동
      } else {
        const errorText = await response.text();
        alert(`구독 해지 실패: ${errorText}`);
      }
    } catch (error) {
      console.error("구독 해지 중 오류 발생:", error);
      alert("구독 해지 요청 중 오류가 발생했습니다.");
    }
  };
  

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <Text>
        정말로 구독을 해지하시겠습니까?
        <br />
        <strong>[추천 플레이리스트]</strong> 재생 기능을
        <br />더 이상 이용할 수 없습니다.
      </Text>
      <ModalButton onClick={onConfirm}>확인</ModalButton>
    </Modal>
  );
};

export default Subscribe2;
