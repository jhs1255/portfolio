import React, { useState } from "react";
import styled from "styled-components";
import { Link, useNavigate } from "react-router-dom";
import ActionButtons from "./ActionButtons";

const Wrapper = styled.div`

`;
const Container = styled.table`
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  max-height: 80vh;
`;
const TableHeader = styled.thead`
  background-color: #eee;
  font-weight: bold;
  font-size: 0.8rem;
  border-bottom: 2px solid #ddd;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;
const TableRow = styled.tr`
  border-bottom: 1px solid #f9f9f9;
`;
const TableData = styled.td`
  padding: 8px;
  text-align: center;
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
 @media (max-width: 900px) {
    font-size: 0.8rem;
  }

  @media (max-width: 600px) {
    font-size: 0.7rem;
  }
`;
const CheckboxColumn = styled(TableData)`
  width: 5%;
  min-width: 40px;
`;

const NumberColumn = styled(TableData)`
  width: 8%;
  min-width: 50px;
`;

const SongInfoColumn = styled(TableData)`
  width: 30%;
  min-width: 150px;
`;

const AlbumColumn = styled(TableData)`
  width: 15%;
  min-width: 100px;
`;

const PlayTimeColumn = styled(TableData)`
  width: 10%;
  min-width: 80px;
`;

const LikeColumn = styled(TableData)`
  width: 10%;
  min-width: 60px;
`;

const ActionColumn = styled(TableData)`
  width: 11%;
  min-width: 80px;
`;

const PlayColumn = styled(TableData)`
  width: 11%;
  min-width: 80px;
`;
const SongLink = styled(Link)`
  text-decoration: none;
  color: black;

  &:hover {
    text-decoration: underline;
  }
`;
const SongInfoContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
`;
const SongCover = styled.div`
  width: 50px;
  height: 50px;
  background-color: #ccc;
  border-radius: 5px;
`;
const SongInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  min-width:100px;
`;

function SongList({ showAll, headerTitle, songs = [], onPlay }) {
  const navigate = useNavigate();
  const [selectedSongs, setSelectedSongs] = useState([]);
  const [allSelected, setAllSelected] = useState(false);
  const [likedSongs, setLikedSongs] = useState({});
  const [albumIds, setAlbumIds] = useState({});

  const fetchAlbumId = async (albumName) => {
    try {
      // 앨범명으로 앨범 목록을 검색
      const response = await fetch(`http://localhost:8080/search/?keyword=ALBUM&value=${encodeURIComponent(albumName)}`);
      const data = await response.json();
      
      if (data.dataList && data.dataList.length > 0) {
        return data.dataList[0].id;
      } else {
        throw new Error("앨범을 찾을 수 없습니다.");
      }
    } catch (error) {
      console.error("앨범 ID를 가져오는 중 에러가 발생했습니다:", error);
      return null;
    }
  };

  const handleAlbumClick = async (e, songId, albumName) => {
    if (albumIds[songId]) return;  // 앨범 ID가 이미 있을 경우 fetch 하지 않음
    e.preventDefault();  // 페이지 이동을 잠시 막음

    const albumId = await fetchAlbumId(albumName);
    if (albumId) {
      setAlbumIds((prev) => ({
        ...prev,
        [songId]: albumId,
      }));
      navigate(`/album/${albumId}`);
    }
  };

  const handleCheckboxChange = (id) => {
    setSelectedSongs((prev) => {
      const newSelected = prev.includes(id)
        ? prev.filter((songId) => songId !== id)
        : [...prev, id];
  
      setAllSelected(newSelected.length === songs.length);
      return newSelected;
    });
  };
  
  const handleSelectAll = () => {
    setSelectedSongs(allSelected ? [] : songs.map((song) => song.id));
    setAllSelected(!allSelected);
  };
  
  const displayedSongs = showAll === true ? songs : songs.slice(0, showAll);

  const handleToggleLike = (songId) => {
    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인 후 이용할 수 있습니다.");
      navigate("/login");
      return; // 로그인 안 된 상태에서는 함수 실행을 멈춤
    }

    console.log(`찜하기 버튼 클릭된 노래: ${songId}`);
    setLikedSongs((prev) => {
      const isLiked = prev[songId] ?? false; // 기존 값이 undefined이면 false 처리
      return {
        ...prev,
        [songId]: !isLiked, // 반대 값으로 업데이트
      };
    });
  };

  return (
    <Wrapper>
      <Container>
      <TableHeader>
  <TableRow>
    <CheckboxColumn>
      <input type="checkbox" checked={allSelected} onChange={handleSelectAll} />
    </CheckboxColumn>
    <NumberColumn>{headerTitle}</NumberColumn>
    <SongInfoColumn>곡 정보</SongInfoColumn>
    <AlbumColumn>앨범</AlbumColumn>
    <PlayTimeColumn>재생시간</PlayTimeColumn>
    <LikeColumn>찜하기</LikeColumn>
    <ActionColumn>추가</ActionColumn>
    <PlayColumn>재생</PlayColumn>
  </TableRow>
</TableHeader>

<tbody>
  {displayedSongs.map((song, index) => {
    const songId = song.id || index + 1;
    const displayNumber = headerTitle === "순위" ? song.rank : song.number || index + 1;

    const convertToMinSec = (milliseconds) => {
      const totalSeconds = Math.floor(milliseconds / 1000); // 밀리초를 초로 변환
      const minutes = Math.floor(totalSeconds / 60); // 분 계산
      const remainingSeconds = totalSeconds % 60; // 초 계산
      return `${String(minutes).padStart(2, "0")}:${String(remainingSeconds).padStart(2, "0")}`;
    };
    
    const playTime = song.playTime ? convertToMinSec(song.playTime) : "03:00";

    return (
      <TableRow key={songId}>
        <CheckboxColumn>
        <input
          type="checkbox"
          checked={selectedSongs.includes(songId)}
          onChange={() => handleCheckboxChange(songId)}
        />
        </CheckboxColumn>
        <NumberColumn>{displayNumber}</NumberColumn>
        <SongInfoColumn>
          <SongInfoContainer>
            <SongCover>
              <img
                src={song.image}
                alt={song.track}
                width="50"
                height="50"
                style={{ borderRadius: "5px" }}
              />
            </SongCover>
            <SongInfo>
              <SongLink to={`/song/${songId}`}>{song.track}</SongLink>
              <SongLink to={`/artist/${song.artist}`}>{song.artist}</SongLink>
            </SongInfo>
          </SongInfoContainer>
        </SongInfoColumn>
        <AlbumColumn>
          <SongLink
            to={albumIds[songId] ? `/album/${albumIds[songId]}` : "#"} // 앨범 ID가 있을 때만 링크를 사용하고, 없으면 빈 링크 처리
            onClick={(e) => handleAlbumClick(e, songId, song.album)}
          >
            {song.album}
          </SongLink>
        </AlbumColumn>
        <PlayTimeColumn>{playTime}</PlayTimeColumn>
        <LikeColumn>
          <ActionButtons
            songId={songId}
            type="like"
            liked={!!likedSongs[songId]}
            onToggleLike={handleToggleLike}
          />
        </LikeColumn>
        <ActionColumn>
        <ActionButtons 
          songId={songId} 
          type="add"
        />
        </ActionColumn>
        <PlayColumn>
          <ActionButtons songId={songId} song={song} type="play" onPlay={onPlay} />
        </PlayColumn>
      </TableRow>
    );
  })}
</tbody>

      </Container>
    </Wrapper>
  );
}

export default SongList;
