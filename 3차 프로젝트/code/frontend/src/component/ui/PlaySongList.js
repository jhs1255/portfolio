import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { FaPlay } from "react-icons/fa";

const SongListDiv = styled.div`
  flex: 3;
  overflow-y: auto;
  padding: 10px;
  @media (max-width: 768px) {
    display: none;
  }
`;

const TabMenu = styled.ul`
  display: flex;
  list-style: none;
  padding: 0;
  margin: 0;
  border-bottom: 2px solid #ccc;
  li {
    font-size: 0.8rem;
    flex: 1;
    text-align: center;
    padding: 10px;
    cursor: pointer;
    background-color: #f9f9f9;
    border-right: 1px solid #ccc;
    &:last-child {
      border-right: none;
    }
    &:hover {
      border-bottom: 2px solid #c69fda;
    }
    &.active {
      background-color: #fff;
      font-weight: bold;
      border-bottom: 2px solid #68009b;
    }
  }
`;

const TabContent = styled.div`
  max-height: 350px;
  overflow-y: auto;
  overflow-x: hidden;

  ul {
    padding: 0;
    margin: 0;
    list-style: none;
  }

  li {
    padding: 5px 10px;
    margin: 0;
    border: none;
    border-bottom: 1px solid #ccc;
  }
`;
const SongItem = styled.li`
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
`;

const AlbumCover = styled.div`
  width: 45px;
  height: 45px;
  background-color: #ddd;
  margin-right: 10px;
`;

const SongInfo = styled.div`
  display: flex;
  flex-direction: column;
  cursor: pointer;
`;

const SongTitle = styled.span`
  font-weight: bold;
  font-size: 0.8rem;
  text-align: left;
`;

const ArtistName = styled.span`
  font-size: 0.6rem;
  text-align: left;
  color: #555;
`;

const PlaylistItem = styled.li`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
`;

const PlaylistCover = styled.div`
  width: 45px;
  height: 45px;
  background-color: #ddd;
  margin-right: 10px;
`;

const PlaylistInfo = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
`;

const PlaylistTitle = styled.span`
  font-weight: bold;
  font-size: 0.8rem;
  text-align: left;
`;

const PlayButton = styled.button`
  font-size: 0.9rem;
  background-color: #f9f9f9;
  border: none;
  border-radius: 3px;
  cursor: pointer;
`;
function PlaySongList({ playlist, setCurrentSong, setCurrentIndex, onPlay }) {
  const [activeTab, setActiveTab] = useState("playlist");
  const [mylistData, setMylistData] = useState([]);

  useEffect(() => {
    const fetchPlaylists = async () => {
      try {
        const response = await fetch("http://localhost:8080/playList/normal/my-thumb", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${sessionStorage.getItem("access_token")}`,
          },
        });

        if (!response.ok) throw new Error(`플레이리스트 불러오기 실패: ${response.status}`);

        const data = await response.json();
        console.log("API 응답 데이터:", data);
        const fetchedPlaylists = data.dataList || [];

        // 로컬스토리지에서 추가된 추천 플레이리스트 가져오기
        const storedPlaylists = JSON.parse(localStorage.getItem("myPlaylists") || "[]");
        const storedIds = storedPlaylists.map(pl => pl.id);

        // 중복되지 않도록 병합
        const combinedPlaylists = [...fetchedPlaylists, ...storedPlaylists].filter(
          (v, i, a) => a.findIndex((t) => t.id === v.id) === i
        );

        // 개별 플레이리스트의 첫 번째 곡 이미지를 가져오기
        const playlistsWithImages = await Promise.all(
          combinedPlaylists.map(async (pl) => {
            const firstSongImage = await fetchPlaylistImage(pl.id);
            return { ...pl, image: firstSongImage };
          })
        );

        setMylistData(playlistsWithImages);
      } catch (error) {
        console.error("플레이리스트 요청 실패:", error);
      }
    };

    fetchPlaylists();
  }, []);

  // 개별 플레이리스트의 첫 번째 곡 이미지 가져오기
  const fetchPlaylistImage = async (playlistId) => {
    try {
      const token = sessionStorage.getItem("access_token");
      const response = await fetch(`http://localhost:8080/playList/normal/my-detail?id=${playlistId}&size=1`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error("첫 번째 곡 이미지 가져오기 실패");

      const data = await response.json();
      console.log("image 응답 데이터:", data);
      return data.dataList?.[0]?.image || "/default-thumbnail.jpg"; // 첫 번째 곡의 이미지 반환, 없으면 기본 이미지
    } catch (error) {
      console.error(`플레이리스트 ID ${playlistId}의 첫 번째 곡 이미지 가져오기 실패:`, error);
      return "/default-thumbnail.jpg"; // 에러 발생 시 기본 이미지 반환
    }
  };

  // ✅ 플레이리스트의 모든 곡 정보를 가져와 `onPlay`로 전달하는 함수
  const handlePlayPlaylist = async (playlistId) => {
    try {
      const token = sessionStorage.getItem("access_token");
      const response = await fetch(`http://localhost:8080/playList/normal/my-detail?id=${playlistId}&size=100`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error("플레이리스트 곡 목록 불러오기 실패");

      const data = await response.json();
      const songs = data.dataList || [];

      if (songs.length === 0) {
        alert("이 플레이리스트에는 곡이 없습니다.");
        return;
      }

      console.log(`🎵 플레이리스트 ID ${playlistId}의 곡 목록:`, songs);

      // 첫 번째 곡을 현재 재생 곡으로 설정
      setCurrentSong(songs[0]);
      setCurrentIndex(0);

      // `Player.js`로 전체 플레이리스트 전송
      onPlay(songs);
    } catch (error) {
      console.error(`🚨 플레이리스트 ID ${playlistId}의 곡 불러오기 실패:`, error);
      alert("플레이리스트 곡을 불러오는 중 오류가 발생했습니다.");
    }
  };

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  return (
    <SongListDiv>
      <TabMenu>
        <li
          className={activeTab === "playlist" ? "active" : ""}
          onClick={() => handleTabClick("playlist")}
        >
          재생목록
        </li>
        <li
          className={activeTab === "mylist" ? "active" : ""}
          onClick={() => handleTabClick("mylist")}
        >
          내 플레이리스트
        </li>
      </TabMenu>
      <TabContent>
        {activeTab === "playlist" && (
          <ul>
            {playlist.map((song, index) => (
              <SongItem
                key={index}
                onClick={() => {
                  setCurrentSong(song);
                  setCurrentIndex(index);
                }}
              >
                <AlbumCover>
                <img
                  src={song.image}
                  alt={song.track}
                  width="100%"
                  height="100%"
                />
                </AlbumCover>
                <SongInfo>
                  <SongTitle>{song.track}</SongTitle>
                  <ArtistName>{song.artist}</ArtistName>
                </SongInfo>
              </SongItem>
            ))}
          </ul>
        )}
        {activeTab === "mylist" && (
          <ul>
            {mylistData.map((playlist, index) => (
              <PlaylistItem key={index}>
                <PlaylistCover>
                  <img src={playlist.image} alt={playlist.name} width="100%" />
                </PlaylistCover>
                <PlaylistInfo>
                  <PlaylistTitle>{playlist.name}</PlaylistTitle>
                </PlaylistInfo>
                <PlayButton onClick={() => handlePlayPlaylist(playlist.id)}>
                  <FaPlay />
                </PlayButton>
              </PlaylistItem>
            ))}
          </ul>
        )}
      </TabContent>
    </SongListDiv>
  );
}

export default PlaySongList;
