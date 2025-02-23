import React, { useState } from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import logo from "../../images/logo.png";
import { Header as jHeader, BackButton, Logo } from "../ui/LoginDiv";

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  min-height: 100vh;
  width: 100vw;
  background-color: #c69fda;
  padding: 50px 0;
  overflow-x: hidden;
  margin: 0;
  * {
    box-sizing: border-box;
  }
`;

const Header = styled(jHeader)`
  margin-top: 24%;
  @media (min-width: 769px) {
    margin-top: 10%;
  }
`;

// 회원가입 박스
const JoinBox = styled.div`
  width: 600px;
  background-color: rgb(239, 224, 225);
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

// 입력 필드 그룹
const InputGroup = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;

  &:first-child {
    margin-top: 10px; /* 첫 번째 필드 위쪽 공간 추가 */
  }
`;

// 라벨
const Label = styled.label`
  font-size: 1rem;
  color: #333;
  text-align: right;
  width: 150px;
`;

// 입력 필드
const Input = styled.input`
  width: 280px;
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

// 작은 버튼 (중복확인)
const SmallButton = styled.button`
  height: 35px;
  padding: 0 10px;
  font-size: 0.8rem;
  white-space: nowrap;
  color: white;
  background-color: #68009b;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #9e7bb5;
  }
`;

// 동의 체크박스 그룹
const AgreementGroup = styled.div`
  display: flex;
  align-items: flex-start; /* 위아래 정렬 */
  gap: 20px;
`;

// 동의 체크박스
const Textarea = styled.textarea`
  width: 390px;
  height: 120px;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-y: scroll; /* 스크롤 추가 */
  resize: none; /* 크기 조절 불가능하게 설정 */

  &:focus {
    border-color: rgb(164, 131, 181);
    outline: none;
  }
`;

// 라디오 버튼 그룹 (동의함, 동의하지 않음)
const RadioGroup = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
  margin-left: 310px;
  margin-bottom: 15px;
  label {
    font-size: 0.9rem;
    white-space: nowrap;
  }
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
`;

// 회원가입 버튼
const Button = styled.button`
  width: 200px;
  height: 45px;
  font-size: 1rem;
  font-weight: bold;
  color: white;
  background-color: rgb(121, 16, 174);
  border: none;
  border-radius: 5px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;

  &:hover {
    background-color: rgb(95, 31, 137);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
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

// 모달 내용 스타일
const ModalContent = styled.div`
  background: rgb(241, 216, 255);
  padding: 20px 40px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
`;

// 모달 버튼 스타일
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

// 비밀번호 유효성 검사 결과 스타일
const PasswordCriteria = styled.div`
  font-size: 0.9rem;
  color: ${(props) => (props.isValid ? "green" : "red")};
`;

// 비밀번호 툴팁 스타일 (유효성 검사 박스스)
const Tooltip = styled.div`
  position: absolute;
  top: 370px;
  right: 350px;
  width: 200px;
  background-color: white;
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  font-size: 0.7rem;
  color: #333;
  visibility: ${(props) => (props.visible ? "visible" : "hidden")};
  opacity: ${(props) => (props.visible ? 1 : 0)};
  transition: opacity 0.3s ease-in-out;
`;

function Join() {
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [name, setName] = useState(""); // 이름 입력값
  const [id, setUsername] = useState("");
  const [password, setPassword] = useState(""); // 비밀번호 입력값
  const [confirmPassword, setConfirmPassword] = useState(""); // 비밀번호 확인 입력값
  const [email, setEmail] = useState(""); // 이메일 입력값
  const [tooltipVisible, setTooltipVisible] = useState(false); // 비밀번호 툴팁 보이기 여부

  //로고 클릭 시 홈으로 이동
  const handleLogoClick = () => {
    navigate("/");
  };

  //가짜 데이터베이스 (이미 가입된 아이디 리스트)
  const existingUsernames = ["user123", "testUser", "helloWorld", "berry123"];

  //아이디 중복 확인 버튼
  const handleCheckUsername = () => {
    if (id.trim() === "") {
      alert("아이디를 입력하세요.");
      return;
    }

    if (existingUsernames.includes(id)) {
      alert("사용이 불가한 아이디입니다. 다시 입력해 주세요.");
    } else {
      alert("사용이 가능합니다!");
    }
  };

  //회원가입 버튼 클릭 시
  const handleJoinClick = async () => {
    if (name.trim() === "") {
      alert("이름을 입력해주세요.");
      return;
    }

    if (id.trim() === "") {
      alert("아이디를 입력해주세요.");
      return;
    }

    if (existingUsernames.includes(id)) {
      alert("사용이 불가한 아이디입니다. 다시 입력해 주세요.");
      return;
    }

    if (
      !isLengthValid ||
      (!containsUppercase && !containsLowercase) ||
      !containsNumber ||
      !containsSpecialChar
    ) {
      alert(
        "비밀번호 조건을 충족해야 합니다.\n(영문+숫자+특수문자 포함, 8자 이상)"
      );
      return;
    }

    if (confirmPassword !== password) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    // 이메일 비어있는지 확인
    if (email.trim() === "") {
      alert("이메일을 입력해주세요.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      alert("유효한 이메일 형식을 입력해주세요.");
      return;
    }

    const agreement = document.querySelector('input[name="agreement"]:checked');
    if (!agreement || agreement.value !== "동의함") {
      alert("개인정보 수집에 동의해야 회원가입이 가능합니다.");
      return;
    }
    // 서버로 보낼 회원가입 데이터
  const requestData = {
    id,
    email,
    name,
    password,
  };
  try {
    const response = await fetch("http://localhost:8080/user/join", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    if (!response.ok) {
      throw new Error("회원가입에 실패했습니다.");
    }

    const data = await response.json();
    console.log("회원가입 성공:", data);

    setIsModalOpen(true); // 모달 열기
  } catch (error) {
    console.error("Error:", error);
    alert("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
    // 모든 조건이 충족되면 회원가입 진행
    setIsModalOpen(true);
  };

  // 팝업에서 확인 버튼 클릭 시 Main.js로 이동
  const handleModalConfirm = () => {
    setIsModalOpen(false);
    navigate("/"); // 메인 페이지로 이동
  };

  //비밀번호 검증 (유효성 검사)
  const containsUppercase = /[A-Z]/.test(password);
  const containsLowercase = /[a-z]/.test(password);
  const containsNumber = /\d/.test(password);
  const containsSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
  const isLengthValid = password.length >= 8;

  // 비밀번호 확인 시, 입력값이 다르면 alert 띄우는 함수
  const handleConfirmPasswordBlur = () => {
    if (confirmPassword !== "" && confirmPassword !== password) {
      alert("비밀번호가 일치하지 않습니다.");
    }
  };

  return (
    <Wrapper>
      <Header>
        <BackButton onClick={() => navigate(-1)}>{"<"}</BackButton>{" "}
        {/* 뒤로 가기 버튼 */}
        <Logo src={logo} alt="Berrecommend 로고" onClick={handleLogoClick} />
      </Header>
      <JoinBox>
        <InputGroup>
          <Label htmlFor="name">이름</Label>
          <Input
            id="name"
            type="text"
            placeholder="이름을 입력하세요"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </InputGroup>

        {/* 아이디 입력란 */}
        <InputGroup>
          <Label htmlFor="username">아이디</Label>
          <Input
            id="username"
            type="text"
            placeholder="8~15자"
            value={id}
            onChange={(e) => setUsername(e.target.value)}
          />
          <SmallButton onClick={handleCheckUsername}>중복확인</SmallButton>
        </InputGroup>

        {/* 비밀번호 입력란 */}
        <InputGroup>
          <Label htmlFor="password">비밀번호</Label>
          <Input
            id="password"
            type="password"
            placeholder="영문+숫자+특문 8자 이상"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            onFocus={() => setTooltipVisible(true)} // 포커스 시 툴팁 표시
            onBlur={() => setTooltipVisible(false)} // 포커스 해제 시 툴팁 숨김
          />

          {/* 툴팁 추가 */}
          <Tooltip visible={tooltipVisible}>
            <PasswordCriteria isValid={containsUppercase || containsLowercase}>
              {containsUppercase || containsLowercase
                ? "✅ 영문 포함됨"
                : "❌ 영문 필요"}
            </PasswordCriteria>
            <PasswordCriteria isValid={containsNumber}>
              {containsNumber ? "✅ 숫자 포함됨" : "❌ 숫자 필요"}
            </PasswordCriteria>
            <PasswordCriteria isValid={containsSpecialChar}>
              {containsSpecialChar ? "✅ 특수문자 포함됨" : "❌ 특수문자 필요"}
            </PasswordCriteria>
            <PasswordCriteria isValid={isLengthValid}>
              {isLengthValid ? "✅ 8자 이상" : "❌ 8자 이상 필요"}
            </PasswordCriteria>
          </Tooltip>
        </InputGroup>

        {/* 비밀번호 확인 입력란 */}
        <InputGroup>
          <Label htmlFor="confirm-password">비밀번호 확인</Label>
          <Input
            id="confirm-password"
            type="password"
            placeholder="비밀번호 재입력"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            onBlur={handleConfirmPasswordBlur}
          />
        </InputGroup>
        {/* 이메일 입력란 */}
        <InputGroup>
          <Label htmlFor="email">이메일</Label>
          <Input
            id="email"
            type="email"
            placeholder="유효한 이메일 입력"
            value={email} // 이메일 입력값을 useState와 연결
            onChange={(e) => setEmail(e.target.value)} // 입력 시 상태 업데이트
          />
        </InputGroup>

        {/* 개인정보 수집 동의 */}
        <AgreementGroup>
          <Label htmlFor="agreement">개인정보 수집 동의</Label>
          <Textarea
            readOnly
            id="agreement"
            defaultValue={`개인정보 수집 동의 내용:\n\n1. 수집하는 개인정보 항목\n- 이름, 이메일, 아이디, 비밀번호 등 회원가입 시 필수 정보\n\n2. 개인정보 이용 목적\n- 회원 관리, 서비스 제공 및 개선`}
          />
        </AgreementGroup>

        {/* 동의함, 동의하지 않음 라디오 버튼 */}
        <RadioGroup>
          <label>
            <input type="radio" name="agreement" value="동의함" /> 동의함
          </label>
          <label>
            <input type="radio" name="agreement" value="동의하지 않음" />{" "}
            동의하지 않음
          </label>
        </RadioGroup>

        {/* 회원가입 버튼 */}
        <ButtonWrapper>
          <Button onClick={handleJoinClick}>회원가입</Button>
        </ButtonWrapper>
      </JoinBox>
      {/* 모달 */}
      {isModalOpen && (
        <Modal>
          <ModalContent>
            <p>
              {name}님,
              <br /> 베리코멘드의 회원이 된 것을 환영합니다!
            </p>
            <ModalButton onClick={handleModalConfirm}>확인</ModalButton>
          </ModalContent>
        </Modal>
      )}
    </Wrapper>
  );
}

export default Join;
