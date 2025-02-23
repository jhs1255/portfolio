import styled from "styled-components";
export const Wrapper = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
`;
export const Container = styled.div`
  width: calc(100% - 310px);
  padding: 30px;
  @media (max-width: 768px) {
    width: calc(100% - 56px);
  }
`;
export const BackWrapper = styled.div`
  position: absolute;
  right: 20px;
  top: 20px;
  @media (min-width: 769px) {
    right: 300px;
  }
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-end;
  button {
    margin: 0;
  }
`;
export const InfoDiv = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  text-align: left;
  gap: 15px;
  img {
    width: 150px;
    height: 150px;
    border-radius: 8px;
    object-fit: cover;
    background-color: #ccc;
  }
  p {
    margin: 0 15px;
  }
`;
