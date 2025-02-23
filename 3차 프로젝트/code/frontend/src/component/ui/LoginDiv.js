import styled from "styled-components";
export const Header = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
  margin-right: 40px;
  margin-bottom: 35px;
  width: 50%;
  height: 60px;
  padding: 0;
  justify-content: center;
`;

// 뒤로 가기 버튼 스타일
export const BackButton = styled.button`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0);
  border: 3px solid rgba(255, 255, 255, 0.36);
  font-size: 1.5rem;
  color: rgba(255, 255, 255, 0.51);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);

  &:hover {
    background-color: rgba(255, 255, 255, 0.18);
    color: rgba(126, 85, 176, 0.68);
  }
`;

/*로고 디자인*/
export const Logo = styled.img`
  width: 250px;
  margin-bottom: 10px;
  cursor: pointer;
`;
