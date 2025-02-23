import React, {useState,useEffect} from "react";
import styled from "styled-components";
import { CiCirclePlus, CiCircleRemove } from "react-icons/ci";
import AddPL from "./AddPL";

const PopupWrapper = styled.div`
  position: absolute;
  top: ${(props) => props.$position.top}px;
  left: ${(props) => props.$position.left}px;
  z-index: 10;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 10px;
  width: 200px;
`;
const CreateDiv = styled.div`
  display: flex;
  flex-direction: row;
  height: 50px;
  align-items: center;
  justify-content: space-between;
  gap: 0px;
  border-bottom: 1px solid #ccc;
  padding-right: 3px;
  cursor: pointer;
  font-weight: bold;
  p {
    margin: 0;
    font-size: 0.8rem;
  }
  &:hover {
    color: #717171;
  }
`;
const PlusBtn = styled.button`
  width: 20px;
  height: 20px;
  background-color: #fff;
  border: none;
  font-size: 1.5em;
  margin-left:10px;
  cursor: pointer;
`;
const CloseBtn = styled.button`
  width: 20px;
  height: 20px;
  padding: 0;
  background-color: #fff;
  border: none;
  font-size: 1.5em;
  cursor: pointer;
  margin-top: -30px;
  &:hover {
    color: #68009b;
  }
`;
const PlaylistOption = styled.div`
  display: flex;
  align-items: center;
  text-align: left;
  justify-content: flex-start;
  margin: 5px 0;
  padding-bottom: 5px;
  border-bottom: 1px solid #ccc;
  cursor:pointer;
  div {
    width: 50px;
    height: 50px;
    margin-right:10px;
    overflow: hidden;
    background-color: #ccc;
  }
  div img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  p {
    margin: 0;
    font-size: 0.8rem;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    min-width: 100px; 
  }
  input[type="checkbox"] {
    margin-left: 15px;
  }
`;
const SaveBtn = styled.button`
  float: right;
  padding: 5px 10px;
  background-color: #68009b;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
`;
const AddPLWrapper = styled.div`
  position: absolute;
  top: -120px;
  left: -20px;
  z-index: 20;
`;

function AddPopup({ position, onClose, songId }) {
  const [playlists, setPlaylists] = useState([]);
  const [showAddPL, setShowAddPL] = useState(false);
  
  useEffect(() => {
    const fetchPlaylists = async () => {
      try {
        const token = sessionStorage.getItem("access_token");
        const response = await fetch("http://localhost:8080/playList/normal/my-thumb",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        if (!response.ok) throw new Error("Failed to fetch playlists");
        const data = await response.json();
        setPlaylists(data.dataList || []); // 서버에서 받은 데이터로 상태 설정
      } catch (error) {
        console.error("Error fetching playlists:", error);
      }
    };

    fetchPlaylists();
  }, []);

  // 새 플레이리스트를 서버에 생성하는 함수
  const handleCreatePlaylist = async (playlistName) => {
    if (!playlistName) return;

    const cleanedName = playlistName.trim().replace(/^"|"$/g, '').replace(/\\"/g, '');
    if (!cleanedName) {
      alert("플레이리스트 이름을 입력해주세요.");
      return;
    }

    try {
      const token = sessionStorage.getItem("access_token");
      const response = await fetch("http://localhost:8080/playList/normal/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(cleanedName),
      });

      const data = await response.json();
      if (response.ok) {
        setPlaylists([...playlists, { name: cleanedName }]); // 새로운 플레이리스트 추가
      } else {
        console.error("Failed to create playlist:", data);
      }
    } catch (error) {
      console.error("Error creating playlist:", error);
    }
  };

  const handleCheckboxToggle = (index) => {
    const updatedPlaylists = [...playlists];
    updatedPlaylists[index].checked = !updatedPlaylists[index].checked;
    setPlaylists(updatedPlaylists);
  };

  const handleSave = async () => {
    const token = sessionStorage.getItem("access_token");
    const selectedPlaylists = playlists.filter(playlist => playlist.checked); // 체크된 플레이리스트 필터링

    console.log("선택된 플레이리스트 목록:", selectedPlaylists);

    const playlistIds = selectedPlaylists.map(playlist => playlist.id);
    const requestBody = {
      playlistIds: playlistIds,
      songId: songId,
    };
    console.log("전송할 데이터:", requestBody); 
      try {
        const response = await fetch("http://localhost:8080/playList/normal/addSong", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(requestBody),
        });

        if (!response.ok) {
          console.error("Failed to add song to playlist : ", playlistIds);
          if (response.status === 400 || 500) {
            const errorData = await response.json();
            alert(errorData.message || "중복된 노래가 이미 존재합니다.");
          } else {
            console.error("Failed to add song to playlist:", response);
            alert("노래 추가에 실패했습니다.");
          }
        } else {
          console.log(`Song added to playlist: ${selectedPlaylists}`);
          alert("노래가 플레이리스트에 추가되었습니다!");
        }
      } catch (error) {
        console.error("Error adding song to playlist:", error);
      }
    onClose();
  };

  return (
    <PopupWrapper $position={position}>
      <div>
        <CreateDiv style={{ marginBottom: "10px" }} onClick={()=>setShowAddPL(true)}>
          <PlusBtn>
            <CiCirclePlus />
          </PlusBtn>
          <p>새 플레이리스트 생성</p>
          <CloseBtn onClick={onClose}>
            <CiCircleRemove />
          </CloseBtn>
        </CreateDiv>

        {/* 기존 플레이리스트 목록 */}
        {playlists.map((playlist, index) => (
          <PlaylistOption key={index} onClick={() => handleCheckboxToggle(index)}>
            <div>
              <img src={"" || "https://via.placeholder.com/100"} alt={playlist.name} />
            </div>
            <p>{playlist.name.replace(/\\"/g, '"')}</p>
            <input type="checkbox" checked={playlist.checked || false}
              onChange={() => handleCheckboxToggle(index)}
            />
          </PlaylistOption>
        ))}
      </div>
      <SaveBtn onClick={handleSave}>저장</SaveBtn>
      {showAddPL && (
        <AddPLWrapper>
          <AddPL isOpen={showAddPL} onClose={() => setShowAddPL(false)} onSave={handleCreatePlaylist} />
        </AddPLWrapper>
      )}
    </PopupWrapper>
  );
}

export default AddPopup;
