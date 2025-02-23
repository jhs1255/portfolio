import React, { useState } from "react";
import Modal, { ModalInput, ModalButton } from "./Modal";
import styled from "styled-components";

const Text = styled.p`
  font-size: 1.2rem;
  font-weight: bold;
  text-align: center;
  color: white;
  margin-bottom: 20px;
`;

const AddPL = ({ isOpen, onClose, playlistName, onSave }) => {
  const [name, setName] = useState(playlistName || "");

  const handleSave = () => {
    const trimmedName = name.trim();
    onSave(trimmedName);
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <Text>새로운 플레이리스트 이름을 작성해주세요</Text>
      <ModalInput
        type="text"
        value={name}
        placeholder="새 플레이리스트 이름"
        onChange={(e) => setName(e.target.value)}
      />
      <ModalButton onClick={handleSave}>생성</ModalButton>
    </Modal>
  );
};

export default AddPL;
