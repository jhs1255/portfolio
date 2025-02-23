import styled from "styled-components";
export const AllBtn = styled.button``;
export const ShuffleBtn = styled.button``;
export const BasketBtn = styled.button`
  padding: 6px 26px;
  font-size: 0.9rem;
  border: 1px solid #dadada;
  background-color: #ffffff;
  cursor: pointer;
  transition: border-bottom 0.3s, color 0.2s;

  &:hover {
    background-color: #c69fda;
    color: #fafafa;
  }
  &:active {
    color: #495057;
  }
`;
export const KeepBtn = styled.button`
  border-radius: 20px;
  border: 1px solid black;
  background-color: ${(props) =>
    props.$isBookmarked ? "#68009b" : "#ffffff"} !important;
  color: ${(props) => (props.$isBookmarked ? "#fff" : "black")} !important;
  &:hover,
  &:active {
    border: 1px solid #68009b;
    background-color: #68009b;
  }
`;
export const BackBtn = styled.button`
  padding: 6px 10px;
  font-size: 0.7rem;
  border: 1px solid #dadada;
  border-radius: 20px;
  background-color: #ffffff;
  cursor: pointer;
  transition: border-bottom 0.3s, color 0.2s;
  margin-bottom: 10px;

  &:hover {
    background-color: #495057;
    color: #fafafa;
  }
  &:active {
    color: #495057;
  }
`;
