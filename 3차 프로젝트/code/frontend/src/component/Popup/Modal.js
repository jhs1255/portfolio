import React from "react";
import styled from "styled-components";

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const ModalContainer = styled.div`
  background: #b37bae;
  color: white;
  padding: 20px 10px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  width: 450px;
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  color: white;

  &:hover {
    color: #ddd;
  }
`;

export const ModalInput = styled.input`
  width: 85%;
  padding: 12px;
  margin-top: 15px;
  margin-left: 10px;
  border: 2px solid #ddd;
  border-radius: 6px;
  text-align: center;
  font-size: 0.9rem;
  background-color: white;
  color: black;
  outline: none;
  transition: all 0.3s;

  &:hover {
    border-color: #9b59b6;
  }

  &:focus {
    border-color: #8e44ad;
    box-shadow: 0 0 5px rgba(142, 68, 173, 0.5);
  }
`;

export const ModalButton = styled.button`
  margin-top: 20px;
  width: 110px;
  height: 42px;
  font-size: 1rem;
  font-weight: bold;
  color: white;
  background-color: rgb(103, 53, 128);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;

  &:hover {
    background-color: #380d66;
    transform: scale(1.05);
  }

  &:active {
    transform: scale(0.98);
  }
`;

const Modal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <ModalOverlay>
      <ModalContainer>
        <CloseButton onClick={onClose}>✖</CloseButton>
        {children}
      </ModalContainer>
    </ModalOverlay>
  );
};

export default Modal;
