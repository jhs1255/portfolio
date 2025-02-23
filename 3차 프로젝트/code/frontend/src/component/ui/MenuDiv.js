import styled from "styled-components";
import { AllBtn, BasketBtn, ShuffleBtn } from "../ui/Buttons";
const MenuDiv = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 20px 2vw 10px 2vw;
  button {
    padding: 6px 10px;
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
    &:last-child {
      margin-left: auto;
    }
  }
`;
const ListenDiv = styled.div`
  display: flex;
  gap: 5px;
`;
function RecMenuDiv({ extraButton }) {
  return (
    <div>
      <MenuDiv>
        <ListenDiv>
          <AllBtn>전체 듣기</AllBtn>
          <ShuffleBtn>셔플 듣기</ShuffleBtn>
          {extraButton && extraButton}
        </ListenDiv>

        <BasketBtn>담기</BasketBtn>
      </MenuDiv>
    </div>
  );
}
export default RecMenuDiv;
