import React, { useState } from "react";
import Modal, { ModalInput, ModalButton } from "../Popup/Modal"; // 기존 모달 스타일 활용
import styled from "styled-components";

const Text = styled.p`
  font-size: 1.2rem;
  font-weight: bold;
  text-align: center;
  color: white;
  margin-bottom: 20px;
`;

const EditPL = ({ isOpen, onClose, playlistName, onSave }) => {
  const [name, setName] = useState(playlistName || "");

  const handleSave = () => {
    onSave(name);
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <Text>플레이리스트 이름을 수정하세요</Text>
      <ModalInput
        type="text"
        value={name}
        placeholder="새 플레이리스트 이름"
        onChange={(e) => setName(e.target.value)}
      />
      <ModalButton onClick={handleSave}>저장</ModalButton>
    </Modal>
  );
};

export default EditPL;
