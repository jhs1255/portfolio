import styled from "styled-components";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  LiaHeart,
  LiaHeartSolid,
  LiaPlusSolid,
  LiaPlaySolid,
} from "react-icons/lia";
import { useLikedSongs } from '../LikedSongsContext';
import AddPopup from "../Popup/AddPopup";
export const Actions = styled.div`
  flex: 1;
  display: flex;
  justify-content: space-around;
  gap: 5px;
`;
const Button = styled.button`
  font-size: 1.5rem;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #717171;
  background-color: #f9f9f9;
  border-radius: 50%;
  cursor: pointer;
  color: #717171;

  &:hover,
  &:active {
    color: #e41111;
    border-color: #e41111;
  }
  &:first-child:hover,
  &:first-child:active {
    color: #e41111;
    border-color: #e41111;
  }
  ${(props) =>
    props.$liked
      ? `
    color: #e41111;
    border-color: #e41111;
    `
      : `
    color: #717171;
    border-color: #717171;
    `}
`;
const LikePopup = styled.div`
  position: fixed;
  bottom: 100px;
  left: 50%;
  transform: translateX(-50%);
  background-color: black;
  color: white;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 1rem;
  opacity: ${(props) => (props.$show  ? "1" : "0")};
  transition: opacity 0.5s ease-in-out;
  visibility: ${(props) => (props.$show ? "visible" : "hidden")};
`;
function ActionButtons({ songId, song, type, onPlay }) {
  const { likedSongs, setLikedSongs } = useLikedSongs();
  const navigate = useNavigate(); // 페이지 이동 함수
  const [showLikePopup, setShowLikePopup] = useState(false);
  const [popupPosition, setPopupPosition] = useState(null);

  // const liked = Array.isArray(likedSongs) && likedSongs.some((likedSong) => likedSong?.id === songId);

  // const liked = Array.isArray(likedSongs) && likedSongs.filter(Boolean).some((likedSong) => likedSong?.id === songId);

  const [liked, setLiked] = useState(false);

  // ✅ `likedSongs`가 업데이트될 때 `liked` 상태 반영
  useEffect(() => {
    setLiked(likedSongs.some((likedSong) => likedSong.id === songId));
  }, [likedSongs, songId]);

  const toggleLike = async () => {
    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인 후 이용할 수 있습니다.");
      navigate("/login");
      return;
    }

    try {

      if (liked) {
        alert("이미 좋아요한 곡입니다!");
        return;
      }

      const response = await fetch("http://localhost:8080/playList/normal/my-thumb", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) throw new Error("플레이리스트를 불러오는 데 실패했습니다.");

      const data = await response.json();
      const favoritePlaylist = data.dataList.find(
        (playlist) => playlist.name === "내가 좋아하는 노래"
      );

      if (!favoritePlaylist) {
        alert("‘내가 좋아하는 노래’ 플레이리스트를 찾을 수 없습니다.");
        return;
      }

      const addResponse = await fetch("http://localhost:8080/playList/normal/addSong", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          playlistIds: [favoritePlaylist.id],
          songId: songId,
        }),
      });

      if (!addResponse.ok) throw new Error("노래를 찜하는 데 실패했습니다.");

      // ✅ 상태 업데이트 및 `localStorage` 반영
      setLikedSongs((prevLikedSongs) => {
        const updatedLikedSongs = [...prevLikedSongs, { ...song, id: songId }];
        localStorage.setItem("likedSongs", JSON.stringify(updatedLikedSongs));
        return updatedLikedSongs;
      });

      setLiked(true);
      setShowLikePopup(true);
      setTimeout(() => setShowLikePopup(false), 2000);
      
    } catch (error) {
      console.error("🚨 오류 발생:", error);
      alert(error.message);
    }
  };

  const handleAddClick = (e) => {
    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인 후 곡 추가 기능을 이용할 수 있습니다.");
      navigate("/login");
      return;
    }

    const rect = e.target.getBoundingClientRect();
    const newPosition = {
      top: rect.top + window.scrollY - 200,
      left: rect.left + window.scrollX - 240,
    };
    setPopupPosition(newPosition);  // 위치 상태 업데이트
  };
  
  const closePopup = () => setPopupPosition(null);
  
  const handlePlay = async () => {
    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인 후 곡 재생 기능을 이용할 수 있습니다.");
      navigate("/login");
      return;
    }

    console.log("재생 버튼 클릭됨", songId);
    console.log("곡 정보", song);

    if (!onPlay) {
      console.error("onPlay 함수가 정의되지 않았습니다.");
      return;
    }

    onPlay(song); // Player.js의 handlePlaySong 호출
    // 여기에 실제 노래 재생 로직 추가
  };
  return (
    <div>
      {/* 팝업 위치가 설정되었을 때 AddPopup 컴포넌트 렌더링 */}
      {popupPosition && (
        <AddPopup
          position={popupPosition}
          onClose={closePopup}
          songId={songId}
          song={song}
          //playlists={/* 전달할 플레이리스트 배열 */}
        />
      )}
      <Actions>
        {/* 좋아요 버튼 */}
        {type === "like" && (
          <Button onClick={toggleLike} $liked={liked}>
            {liked ? <LiaHeartSolid /> : <LiaHeart />}
          </Button>
        )}
        {/* 추가 버튼 */}
        {type === "add" && (
          <Button onClick={handleAddClick}>
            <LiaPlusSolid />
          </Button>
        )}
        {/* 재생 버튼 */}
        {type === "play" && (
          <Button onClick={handlePlay}>
            <LiaPlaySolid />
          </Button>
        )}
      </Actions>
      {/* 좋아요 팝업 */}
      {showLikePopup && (
        <LikePopup $show={showLikePopup ? "true" : "false"}>
          내가 좋아하는 노래로 저장했어요!
        </LikePopup>
      )}
    </div>
  );
}
export default ActionButtons;
