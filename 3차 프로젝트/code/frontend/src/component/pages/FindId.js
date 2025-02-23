import React, { useState } from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import logo from "../../images/logo.png";
import { Header, BackButton, Logo } from "../ui/LoginDiv";
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100vw;
  background-color: #c69fda;
`;

// 아이디 찾기 박스 디자인
const FindBox = styled.div`
  width: 400px;
  background-color: rgb(239, 224, 225);
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
`;

// 설명
const Description = styled.p`
  font-size: 1.1rem;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
`;

// 입력창과 버튼 스타일
const InputGroup = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;

  &:first-child {
    margin-top: 10px; /* 첫 번째 필드 위쪽 공간 추가 */
  }
`;

// 아이디와 입력창 스타일
const Label = styled.label`
  font-size: 1rem;
  color: #333;
  text-align: right;
  width: 60px;
`;

// 입력창 스타일
const Input = styled.input`
  width: 230px;
  height: 40px;
  padding: 5px 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  &:focus {
    border-color: rgb(164, 131, 181);
    outline: none;
  }
`;

// 버튼 스타일
const ButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20px;
`;

const Button = styled.button`
  width: 150px;
  height: 45px;
  font-size: 1rem;
  font-weight: bold;
  color: white;
  background-color: rgb(121, 16, 174);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-bottom: 10px;

  &:hover {
    background-color: rgb(95, 31, 137);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
`;

// 팝업 스타일
const Modal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
`;

// 팝업 내용 스타일
const ModalContent = styled.div`
  background: rgb(241, 216, 255);
  padding: 20px 40px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  width: 300px; /* 팝업 너비 고정 */
  height: 180px; /* 팝업 높이 고정 */
  display: flex; /* 플렉스 박스 활성화 */
  flex-direction: column; /* 세로 정렬 */
  justify-content: center; /* 세로 가운데 정렬 */
  align-items: center; /* 가로 가운데 정렬 */
`;

// 팝업 버튼 스타일
const ModalButton = styled.button`
  margin-top: 20px;
  width: 100px;
  height: 40px;
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

function FindId() {
  const navigate = useNavigate(); 
  const [isModalOpen, setIsModalOpen] = useState(false); // 팝업 상태 저장
  const [modalType, setModalType] = useState(""); // 성공 또는 오류 상태 저장
  const [name, setName] = useState(""); // 이름 저장
  const [email, setEmail] = useState(""); // 이메일 저장

  const [foundId, setFoundId] = useState(""); // 찾은 아이디 저장

  // 로고 클릭 시 메인 페이지로 이동
  const handleLogoClick = () => {
    navigate("/");
  };

  // 확인 버튼 클릭 시 실행
  const handleButtonClick = async () => {
    if (!name || !email) {
      alert("이름과 이메일을 모두 입력해주세요.");
      return;
    }
  
    try {
      const response = await fetch("http://localhost:8080/user/get-id", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, name }),
      });
  
      if (response.ok) {
        const data = await response.json();
        setIsModalOpen(true);
        setModalType("success");
        setFoundId(data.id); // 서버에서 받은 아이디 저장
      } else {
        alert("일치하는 회원 정보가 없습니다. 다시 한 번 확인해주세요.");
      }
    } catch (error) {
      console.error("아이디 찾기 요청 중 오류 발생:", error);
      alert("서버와의 연결이 원활하지 않습니다. 다시 시도해주세요.");
    }
  };

  // Enter 키 입력 시 확인 버튼 실행
  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleButtonClick();
    }
  };

  // 팝업 닫기
  const closeModal = () => {
    setIsModalOpen(false);
    setModalType("");
    navigate("/login-id"); // 로그인 페이지로 이동
  };

  return (
    <Wrapper>
      <Header>
        <BackButton onClick={() => navigate(-1)}>{"<"}</BackButton>{" "}
        {/* 뒤로 가기 버튼 */}
        <Logo src={logo} alt="Berrecommend 로고" onClick={handleLogoClick} />
      </Header>

      <FindBox>
        <Description>
          아이디 찾기를 위해 회원가입 시 입력한 <br />
          이름과 이메일을 입력해주세요.
        </Description>

        {/* 이름과 이메일 입력창 */}
        <InputGroup>
          <Label htmlFor="name">이름</Label>
          <Input
            id="name"
            type="text"
            placeholder="이름"
            value={name}
            onChange={(e) => setName(e.target.value)}
            onKeyPress={handleKeyPress} // 🔹 엔터 키 입력 감지
          />
        </InputGroup>

        {/* 이메일 입력창 */}
        <InputGroup>
          <Label htmlFor="email">이메일</Label>
          <Input
            id="email"
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            onKeyPress={handleKeyPress} // 🔹 엔터 키 입력 감지
          />
        </InputGroup>

        {/* 확인 버튼 */}
        <ButtonWrapper>
          <Button onClick={handleButtonClick}>확인</Button>
        </ButtonWrapper>

        {/* 팝업 */}
      </FindBox>
      {/* 성공 팝업 */}
      {isModalOpen && modalType === "success" && (
        <Modal>
          <ModalContent>
            <p>
              {name} 회원님의 아이디는
              <br />
              <b>{foundId}</b> 입니다.
            </p>
            <ModalButton onClick={closeModal}>확인</ModalButton>
          </ModalContent>
        </Modal>
      )}
    </Wrapper>
  );
}

export default FindId;
