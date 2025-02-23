import React from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import logo from "../../images/logo.png";
import { Header, BackButton, Logo } from "../ui/LoginDiv";
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background-color: #c69fda;
`;

/*로그인 박스 디자인*/
const LoginBox = styled.div`
  background-color: rgb(239, 224, 225);
  padding: 20px;
  border-radius: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 380px;
  height: 200px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
`;

/*로그인 버튼 디자인*/
const Button = styled.button`
  width: 300px;
  height: 50px;
  padding: 10px 0;
  margin: 0px auto;
  font-size: 1rem;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s;

  /*Google 로그인 버튼 디자인*/
  &:nth-child(1) {
    background-color: #fff;
    color: #757575;
    border: 1px solid #ddd;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  &:nth-child(1):hover {
    background-color: rgb(232, 232, 232);
  }

  /*회원 ID 로그인 버튼 디자인*/
  &:nth-child(2) {
    background-color: #fff;
    color: #757575;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }

  &:nth-child(2):hover {
    background-color: rgb(232, 232, 232);
  }
`;

// '회원가입', 'ID 찾기' & 'PW 찾기' 문구 디자인

const TextLinks = styled.div`
  margin-top: 5px;
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

//구글 로그인 서버 연결
const onGoogleLogin = () => {
  // window.location.href = "http://localhost:8080/oauth2/authorization/google";
  window.location.href = "http://localhost:8080/user/oauth/login";
}

window.onload = function() {
  // URL에서 access_token과 refresh_token을 추출
  const urlParams = new URLSearchParams(window.location.search);
  const accessToken = urlParams.get('access_token');
  const refreshToken = urlParams.get('refresh_token');
  
  if (accessToken && refreshToken) {
      // sessionStorage에 토큰 저장
      sessionStorage.setItem('access_token', accessToken);
      sessionStorage.setItem('refresh_token', refreshToken);
  }
};

/*로그인 메뉴 컴포넌트*/
function LoginMenu() {
  const navigate = useNavigate(); // 페이지 이동 함수

  const handleIdLogin = () => {
    navigate("/login-id"); // 회원 ID 로그인 페이지로 이동
  };

  const handleLogoClick = () => {
    navigate("/"); // 로고 클릭 시 메인 페이지로 이동
  };

  return (
    <Wrapper>
      <Header>
        <BackButton onClick={() => navigate(-1)}>{"<"}</BackButton>
        {/* 뒤로 가기 버튼 */}
        <Logo src={logo} alt="Berrecommend 로고" onClick={handleLogoClick} />
      </Header>

      <LoginBox>
      <Button onClick={onGoogleLogin}>Google로 로그인하기</Button>
        <Button onClick={handleIdLogin}>회원 ID로 로그인하기</Button>
      </LoginBox>
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

export default LoginMenu;
