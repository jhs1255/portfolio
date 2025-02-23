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

// 로그인 박스 디자인
const LoginBox = styled.div`
  width: 380px;
  height: 200px;
  background-color: rgb(239, 224, 225);
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
`;

// 로그인 버튼 디자인
const InputGroup = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;

  &:first-child {
    margin-top: 10px;
  }
`;

// 아이디와 입력창 스타일
const Label = styled.label`
  font-size: 1rem;
  color: #333;
  text-align: center;
  width: 80px;
`;

// 입력창 스타일
const Input = styled.input`
  width: 230px;
  height: 40px;
  padding: 5px 10px;
  font-size: 1rem;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  &:focus {
    border-color: rgb(164, 131, 181);
    outline: none;
  }
`;

// 로그인 버튼 디자인
const Button = styled.button`
  width: 130px;
  height: 45px;
  font-size: 1rem;
  font-weight: bold;
  color: white;
  background-color: rgb(121, 16, 174);
  border: none;
  border-radius: 5px;
  maring-top: 10px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;

  &:hover {
    background-color: rgb(95, 31, 137);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  }
`;

// '회원가입', 'ID 찾기' & 'PW 찾기' 문구 디자인
const TextLinks = styled.div`
  margin-top: 20px;
  text-align: center;
`;

const TextLink = styled.p`
  margin-bottom: 0;
  font-size: 1.1em;
  color: rgb(57, 57, 57);

  a {
    margin-left: 5px;
    color: #68009b;
    text-decoration: none;
    font-weight: bold;
    transition: color 0.1s;
    cursor: pointer;

    &:hover {
      color: rgb(231, 224, 236);
    }
  }
`;

const StyledButton = styled.button`
  margin-left: 5px;
  color: #68009b;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.1s;
  cursor: pointer;
  background: none;
  border: none;
  font-size: inherit;
  padding: 0;

  &:hover {
    color: rgb(231, 224, 236);
  }
`;

function Login() {
  const navigate = useNavigate(); // 페이지 이동 함수
  const [id, setUsername] = useState(""); // 아이디 입력값
  const [password, setPassword] = useState(""); // 비밀번호 입력값
  const [error, setError] = useState(""); // 에러 메시지

  // 로고 클릭 시 메인 페이지로 이동
  const handleLogoClick = () => {
    navigate("/"); //
  };

  const handleLogin = async (event) => {
    event.preventDefault();
    const loginData = { id, password };

    try {
      // 로그인 요청을 fetch로 보내기
      const response = await fetch("http://localhost:8080/user/form/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginData),
      });

      // 응답이 정상일 경우
      if (response.ok) {
        const data = await response.json();
        const token = data.access_token;
        const name = data.name;

        // 토큰을 sessionStorage에 저장
        sessionStorage.setItem("access_token", token);
        sessionStorage.setItem("refresh_token", token);
        sessionStorage.setItem("name", name);

        // 토큰을 콘솔에 출력
        console.log("받은 토큰:", token);

        // 사용자 이름을 콘솔에 출력
        console.log("받은 이름:", name);
        alert("로그인 성공. 환영합니다!");
        navigate("/"); // 메인 페이지로 이동
      } else {
        // 로그인 실패 시
        const errorData = await response.json();
        setError("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        alert("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        console.error("로그인 오류:", errorData);
      }
    } catch (error) {
      // 네트워크 오류 등
      setError("로그인 실패: 네트워크 오류 발생.");
      console.error("로그인 오류:", error);
    }
  };

  return (
    <Wrapper>
      <Header>
        <BackButton onClick={() => navigate(-1)}>{"<"}</BackButton>{" "}
        {/* 뒤로 가기 버튼 */}
        <Logo src={logo} alt="Berrecommend 로고" onClick={handleLogoClick} />
      </Header>
      <form onSubmit={handleLogin}>
        <LoginBox>
          <InputGroup>
            <Label htmlFor="username">아이디</Label>
            <Input
              id="id"
              type="text"
              placeholder="아이디를 입력하세요"
              value={id}
              onChange={(e) => setUsername(e.target.value)}
            />
          </InputGroup>
          <InputGroup>
            <Label htmlFor="password">비밀번호</Label>
            <Input
              id="password"
              type="password"
              placeholder="비밀번호를 입력하세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </InputGroup>
          <Button type="submit">로그인</Button>
        </LoginBox>
        {error && <p style={{ color: "red" }}>{error}</p>}
      </form>
      <TextLinks>
        <TextLink>
          아직 회원이 아니신가요?
          <StyledButton onClick={() => navigate("/signup")}>
            회원가입하기
          </StyledButton>
        </TextLink>
        <TextLink>
          회원 정보를 잊으셨나요?
          <StyledButton onClick={() => navigate("/find-id")}>
            ID 찾기
          </StyledButton>
          {" / "}
          <StyledButton onClick={() => navigate("/find-pw")}>
            PW 찾기
          </StyledButton>
        </TextLink>
      </TextLinks>
    </Wrapper>
  );
}

export default Login;
