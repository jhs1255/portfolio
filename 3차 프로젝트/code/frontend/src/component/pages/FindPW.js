import React, { useState, useEffect } from "react";
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

// 비밀번호 찾기 박스 스타일
const FindBox = styled.div`
  width: 500px;
  background-color: rgb(239, 224, 225);
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
`;

// 비밀번호 찾기 설명 스타일
const Description = styled.p`
  font-size: 1.1rem;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
`;

// 아이디와 입력창 스타일 //
const IdInputGroup = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 20px;
  width: 100%;
  margin-left: 50px;
`;

// 아이디 입력창 스타일
const IdLabel = styled.label`
  font-size: 1rem;
  color: #333;
  text-align: right;
  width: 80px;
`;

const IdInput = styled.input`
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

// 나머지 입력창 스타일 //
const InputGroup = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  width: 100%;
`;

// 라벨 스타일
const Label = styled.label`
  font-size: 1rem;
  color: #333;
  text-align: right;
  width: 90px;
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

// 작은 버튼 스타일
const SmallButton = styled.button`
  height: 35px;
  width: 90px;
  padding: 0 10px;
  font-size: 0.9rem;
  color: white;
  background-color: rgb(121, 16, 174);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: rgb(95, 31, 137);
  }
`;

// 확인 버튼 스타일
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
  background-color: ${(props) =>
    props.disabled ? "rgba(172, 172, 172, 0.83)" : "rgb(121, 16, 174)"};
  border: none;
  border-radius: 5px;
  cursor: ${(props) => (props.disabled ? "not-allowed" : "pointer")};
  transition: background-color 0.3s, box-shadow 0.3s;
  margin-bottom: 10px;

  &:hover {
    background-color: ${(props) =>
      props.disabled ? "#b0b0b0" : "rgb(95, 31, 137)"};
    box-shadow: ${(props) =>
      props.disabled ? "none" : "0 4px 6px rgba(194, 194, 194, 0.2)"};
  }
`;

// 모달 스타일
const Modal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background: rgb(241, 216, 255);
  padding: 20px 40px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  width: 320px;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

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

// 타이머 스타일
const TimerText = styled.span`
  position: absolute;
  right: 10px;
  top: 50%;
  font-size: 1rem;
  transform: translateY(-50%);
  color: ${(props) => (props.timer === 0 ? "red" : "rgba(43, 43, 43, 0.61)")};
`;

// 입력창 래퍼 스타일
const InputWrapper = styled.div`
  position: relative;
  width: 250px;
`;

function FindPw() {
  const navigate = useNavigate(); // 페이지 이동 함수
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 여부
  const [email, setEmail] = useState(""); // 사용자가 입력한 이메일
  const [timer, setTimer] = useState(300); // 5분 (300초)
  const [isCounting, setIsCounting] = useState(false); // 타이머 작동 여부
  const [username, setUsername] = useState(""); // 사용자가 입력한 아이디
  const [code, setCode] = useState(""); // 사용자가 입력한 인증 코드
  const [isVerified, setIsVerified] = useState(false); // 인증 완료 여부

  //로고
  const handleLogoClick = () => {
    navigate("/");
  };

  // 인증코드 전송 함수
  const handleSendVerificationCode = async () => {
    if (!email || !username) {
      alert("아이디와 이메일을 모두 입력해주세요.");
      return;
    }
  
    try {
      const response = await fetch("http://localhost:8080/user/validate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, id: username }),
      });
  
      if (!response.ok) {
        alert("회원정보를 찾을 수 없습니다. 아이디와 이메일을 다시 입력해주세요.");
        return;
      }
  
      alert("입력한 이메일로 인증번호를 발송했습니다.\n5분 내로 번호를 입력해 주세요.");
      startTimer();
    } catch (error) {
      console.error("인증 코드 전송 오류:", error);
      alert("인증 코드 전송 중 오류가 발생했습니다.");
    }
  };
  

  // 타이머 시작 함수
  const startTimer = () => {
    setTimer(300); // 5분 초기화
    setIsCounting(true); // 타이머 작동 시작
  };

  // 타이머 감소 로직
  useEffect(() => {
    if (isCounting) {
      const interval = setInterval(() => {
        setTimer((prevTimer) => {
          if (prevTimer <= 1) {
            clearInterval(interval);
            setIsCounting(false); // 타이머 멈추기
            alert("시간이 초과되었습니다. 다시 시도해 주세요."); // 시간이 초과되면 알림
            return 0;
          }
          return prevTimer - 1;
        });
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [isCounting]);

  // "분:초" 포맷 변환 함수
  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, "0")}:${secs
      .toString()
      .padStart(2, "0")}`;
  };

  // 인증코드 확인 함수
  const handleVerifyCode = async () => {
    if (!code) {
      alert("인증 코드를 입력해 주세요.");
      return;
    }
  
    try {
      const response = await fetch("http://localhost:8080/user/send-email", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password: code }),
      });
  
      if (response.ok) {
        alert("인증이 완료되었습니다. 임시 비밀번호를 이메일에서 확인하세요.");
        setIsVerified(true);
      } else {
        alert("잘못된 인증 코드입니다. 다시 확인해 주세요.");
      }
    } catch (error) {
      console.error("이메일 인증 오류:", error);
      alert("이메일 인증 중 오류가 발생했습니다.");
    }
  };

  // 모달 열기
  const handleButtonClick = () => {
    if (isVerified) {
      setIsModalOpen(true);
    }
  };

  // 모달 닫기
  const closeModal = () => {
    setIsModalOpen(false);
    navigate("/login-id");
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
          비밀번호 찾기를 위해 회원가입 시 입력한 <br />
          아이디와 이메일을 입력해주세요.
        </Description>

        {/* 아이디 입력창 */}
        <IdInputGroup>
          <IdLabel htmlFor="username">아이디</IdLabel>
          <IdInput
            id="username"
            type="text"
            placeholder="아이디"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </IdInputGroup>

        {/* 이메일 입력창 */}
        <InputGroup>
          <Label htmlFor="email">이메일</Label>
          <Input
            id="email"
            type="email"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <SmallButton onClick={handleSendVerificationCode}>
            코드 전송
          </SmallButton>
        </InputGroup>

        {/* 이메일 인증코드 입력창 */}
        <InputGroup>
          <Label htmlFor="emailCode">이메일 인증</Label>
          <InputWrapper>
            <Input
              id="emailCode"
              type="text"
              placeholder="인증코드 입력"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              disabled={!isCounting} // 타이머가 진행 중일 때만 입력 가능
            />
            {/* 타이머 표시 */}
            {timer !== null && (
              <TimerText expired={timer === 0}>
                {timer > 0 ? formatTime(timer) : "시간 초과"}
              </TimerText>
            )}
          </InputWrapper>

          {/* 인증번호 확인 버튼 */}
          <SmallButton onClick={handleVerifyCode}>인증하기</SmallButton>
        </InputGroup>

        <ButtonWrapper>
          {/* 인증 완료되지 않으면 버튼 비활성화 (회색, 마우스 비활성) */}
          <Button onClick={handleButtonClick} disabled={!isVerified}>
            확인
          </Button>
        </ButtonWrapper>
      </FindBox>
      {/* 팝업 모달 */}
      {isModalOpen && (
        <Modal>
          <ModalContent>
            <p>
              이메일로 전송된 임시 비밀번호로 <br />
              로그인 후 마이페이지에서
              <br />
              비밀번호를 변경해 주세요.
            </p>
            <ModalButton onClick={closeModal}>확인</ModalButton>
          </ModalContent>
        </Modal>
      )}
    </Wrapper>
  );
}

export default FindPw;
