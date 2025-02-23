import React, { useState } from "react";
import Modal, { ModalInput, ModalButton } from "../Popup/Modal";
import MPEdit2 from "./MPEdit2";

const MPEdit1 = ({ isOpen, onClose }) => {
  const [password, setPassword] = useState("");
  const [isMPEdit2Open, setIsMPEdit2Open] = useState(false);

  // 모달이 닫힐 때 호출되는 핸들러
  const handleClose = () => {
    setPassword(""); // 입력값 초기화
    onClose(); // 부모 onClose 호출
  };

  const handleConfirm = async () => {
    if (!password) {
      alert("비밀번호를 입력해주세요.");
      return;
    }

    try {
      console.log("API 요청 URL:", "http://localhost:8080/profile/check");
      const token = sessionStorage.getItem("access_token");
      console.log("보내는 accessToken:", token);

      const response = await fetch("http://localhost:8080/profile/check", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`, // JWT 인증 토큰 추가
        },
        body: JSON.stringify({ password }),
      });

      const data = await response.json();
      console.log("서버 응답:", data);

      if (response.ok) {
        console.log("비밀번호 확인 성공:", data);
        setIsMPEdit2Open(true); // 다음 팝업 열기
      } else {
        console.log("비밀번호 확인 실패:", data);
        alert("잘못된 비밀번호 입니다. 다시 입력해주세요.");
      }
    } catch (error) {
      console.error("API 요청 오류:", error);
      alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleConfirm();
    }
  };

  return (
    <>
      <Modal isOpen={isOpen} onClose={handleClose}>
        <p style={{ fontSize: "1.2rem", marginBottom: "15px" }}>
          회원님의 비밀번호를 입력해주세요
        </p>
        <ModalInput
          type="password"
          placeholder="비밀번호 입력"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          onKeyDown={handleKeyDown} // 엔터키 이벤트 추가
        />
        <ModalButton onClick={handleConfirm}>확인</ModalButton>
      </Modal>

      <MPEdit2
        isOpen={isMPEdit2Open}
        onClose={() => {
          setIsMPEdit2Open(false);
          setPassword(""); // 첫번째 팝업의 입력값(비밀번호) 초기화
          onClose(); // 부모에게 전체 팝업 종료 알림 (필요하다면)
        }}
      />
    </>
  );
};

export default MPEdit1;
