import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import { FaSearch } from "react-icons/fa";
import logo from "../../images/logo.png";
const Wrapper = styled.div`
  position: relative;
  border-bottom: 3px solid #dee2e6;
`;
const Container = styled.div`
  width: calc(100% - 250px);
  margin: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  @media (max-width: 768px) {
    width: 100%;
  }
`;
const LoginDiv = styled.div`
  display: flex;
  justify-content: flex-end;
  padding-right: 30px;
  button {
    padding: 5px 16px;
    font-size: 0.9rem;
    background-color: #ffffff;
    border: 1px solid #ced4da;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;

    &:hover {
      background-color: #495057;
      color: #ffffff;
    }
`;
const SearchDiv = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  position: relative;
  margin: 0 20px;
  img:hover {
    cursor: pointer;
  }
  input {
    padding: 8px 50px 8px 36px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    font-size: 1rem;
    outline: none;
    transition: border-color 0.3s;
    width: 100%;
    max-width: 400px;
    box-sizing: border-box;

    &::placeholder {
      font-size: 0.9rem;
    }
    &:focus {
      border-color: #495057;
    }
  }
  button {
    position: absolute;
    left: 200px;

    top: 50%;
    transform: translateY(-50%);
    background: none;
    border: none;
    cursor: pointer;
    svg {
      width: 16px;
      height: 16px;
      color: #495057;
    }
  }
  @media (max-width: 532px) {
    input {
      padding: 8px 50px 8px 36px;
      font-size: 0.9rem;
    }
    button {
      left: 90%;
    }
  }
  @media (max-width: 768px) {
    margin: 0 10px;
    input {
      padding: 8px 58px 8px 30px;
      font-size: 0.9rem;
    }
  }
`;
const MenuDiv = styled.div`
  position: relative;
  width: 100%;
  height: 45px;

  button {
    padding: 8px 25px;
    border: 3px solid #ffffff;
    background-color: #ffffff;
    cursor: pointer;
    transition: border-bottom 0.2s, color 0.2s;
    font-weight: bold;
    font-size: 0.9rem;

    &:hover {
      color: #68009b;
      border-bottom: 3px solid #c69fda;
    }
    &:active {
      color: #495057;
      border-bottom: 3px solid #68009b;
    }
  }
  .active {
    color: #68009b;
    border-bottom: 3px solid #68009b;
  }
`;
const MainMenuDiv = styled.div`
  position: relative;
  top: 20px;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  gap: 10px;
  justify-content: center;
  width: 100%;
`;
const MyMenuDiv = styled.div`
  position: absolute;
  top: 2px;
  right: 0;
  width: 130px;
  font-size: 0.7rem;
  display: flex;
  justify-content: flex-end;
  margin-right: 3vw;
  button {
    font-size: 0.7rem;
  }
`;
function Header({ activeMenu, onMenuClick }) {
  const [searchTerm, setSearchTerm] = useState(""); // 검색어 상태
  const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태
  const navigate = useNavigate();

  useEffect(() => {
    // 페이지 로드 시, 세션에 토큰이 있으면 로그인 상태로 설정
    const token = sessionStorage.getItem("access_token");
    setIsLoggedIn(!!token); // 토큰이 있으면 로그인 상태로 변경
  }, []);

  // 검색 실행 함수
  const handleSearch = (e) => {
    e.preventDefault();
    if (searchTerm.trim() !== "") {
      navigate(`/search?query=${encodeURIComponent(searchTerm)}`);
    }
  };

  // 로고 클릭 시 메인 페이지로 이동
  const handleLogoClick = () => {
    window.location.href = "/"; // 메인 페이지로 이동
  };

  const handleLoginClick = () => {
    navigate("/login"); // 로그인 페이지로 이동
  };

  const handleLogoutClick = () => {
    const confirmLogout = window.confirm("로그아웃 하시겠습니까?");
    if (confirmLogout) {
      // 세션에서 토큰 삭제
      sessionStorage.removeItem("access_token");
      sessionStorage.removeItem("refresh_token");
      sessionStorage.removeItem("name");
      localStorage.removeItem("myPlaylists");
      localStorage.removeItem("likedSongs"); // ✅ 좋아요 목록 삭제
      setIsLoggedIn(false); // 로그인 상태 변경
      alert("로그아웃 되었습니다."); // 로그아웃 완료 알림
      window.location.href = "/";
    }
  };

  const handleMyPageClick = () => {
    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인 후 이용할 수 있습니다.");
      navigate("/login");
      return;
    }
    onMenuClick("mypage");
    navigate("/mypage");
  };

  const handleRecommendClick = () => {
    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인 후 이용할 수 있습니다.");
      navigate("/login");
      return;
    }
    onMenuClick("recommend");
  };

  return (
    <Wrapper>
      <Container>
        <LoginDiv>
        {isLoggedIn ? (
            <button onClick={handleLogoutClick}>로그아웃</button> // 로그아웃 버튼
          ) : (
            <button onClick={handleLoginClick}>로그인</button> // 로그인 버튼
          )}
        </LoginDiv>

        <SearchDiv>
          <img src={logo} alt="logo" onClick={handleLogoClick} />
          <form onSubmit={handleSearch} style={{ display: "flex" }}>
            <input
              type="text"
              placeholder="검색어를 입력하세요!"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button type="submit">
              <FaSearch />
            </button>
          </form>
        </SearchDiv>

        <MenuDiv>
          <MainMenuDiv>
            <button
              className={activeMenu === "chart" ? "active" : ""}
              onClick={() => onMenuClick("chart")}
            >
              차트
            </button>
            <button
              className={activeMenu === "recommend" ? "active" : ""}
              onClick={handleRecommendClick}
            >
              추천
            </button>
          </MainMenuDiv>
          <MyMenuDiv>
            <button
              className={activeMenu === "mypage" ? "active" : ""}
              onClick={handleMyPageClick}
            >
              마이페이지
            </button>
          </MyMenuDiv>
        </MenuDiv>
      </Container>
    </Wrapper>
  );
}
export default Header;
