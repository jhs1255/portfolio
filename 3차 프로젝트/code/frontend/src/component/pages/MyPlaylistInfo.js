import styled from "styled-components";
import React, { useState, useEffect } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import SongList from "../ui/SongList";
import { BackBtn } from "../ui/Buttons";
import { RiArrowGoBackFill } from "react-icons/ri";
import {
  Wrapper,
  Container,
  BackWrapper as plBackWrapper,
} from "../ui/AllDiv";
import RecMenuDiv from "../ui/MenuDiv";
import { useLikedSongs } from "../LikedSongsContext";

const BackWrapper = styled(plBackWrapper)`
  right: 10;
  top: 25px;
  @media (min-width: 769px) {
    right: 50;
  }
`;
const InfoDiv = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  margin: 20px;
`;
const PlaylistJacket = styled.img`
  width: 100px;
  height: 100px;
  background-color: #ccc;
  object-fit: cover;
`;
const ListTitle = styled.p`
  text-align: left;
  font-size: large;
  font-weight: bold;
  margin: 10px 20px;
`;
const ControlDiv = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
`;
const NoSongsMessage = styled.p`
  text-align: center;
  font-size: 1.2rem;
  color: #888;
  margin-top: 20px;
`;
function MyPlaylistInfo({ onPlay }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const { state } = useLocation();
  const [playlist, setPlaylist] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { setLikedSongs } = useLikedSongs();

  useEffect(() => {
    async function fetchPlaylist() {
      const token = sessionStorage.getItem("access_token");
      try {
        const response = await fetch(`http://localhost:8080/playList/normal/my-detail?id=${id}&size=100`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) throw new Error("Failed to fetch playlist");
        const data = await response.json();
        const firstSongImage = data.dataList && data.dataList[0] ? data.dataList[0].image : null;
        const playlistData = {
          title: state?.playlistTitle || `추천 플레이리스트 ${id}`, // 제목을 `id`를 기준으로 예시로 설정
          image: firstSongImage || "/default-thumbnail.jpg", // 기본 이미지 또는 첫 번째 앨범 이미지로 설정
          songs: data.dataList, // 노래 목록은 응답에서 받은 `dataList`로 설정
        };
        setPlaylist(playlistData);

        // ✅ likedSongs 업데이트 (ID 기준으로 병합)
        setLikedSongs((prevLikedSongs) => {
          const updatedLikedSongs = [
            ...new Map(
              [...prevLikedSongs, ...playlistData.songs.map(song => ({ ...song, liked: true }))]
              .map(song => [song.id, song])
            ).values()
          ];
          localStorage.setItem("likedSongs", JSON.stringify(updatedLikedSongs));
          return updatedLikedSongs;
        });

      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    }
    fetchPlaylist();
  }, [id, state, setLikedSongs]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!playlist) return <p>Playlist not found</p>;
  if (!playlist || !playlist.songs || playlist.songs.length === 0) return <p>No songs available in this playlist</p>;

  const formatPlayTime = (playTime) => {
    if (!playTime || playTime === 0) return "3:00"; // playTime이 0이면 기본값 3:00

    const minutes = Math.floor(playTime / 60000); // 밀리초 → 분 변환
    const seconds = Math.floor((playTime % 60000) / 1000); // 밀리초 → 초 변환
    return `${minutes}:${seconds.toString().padStart(2, "0")}`; // 2자리 초 포맷
  };
  return (
    <Wrapper>
      <Container>
       
        {playlist && (
          <>
           <BackWrapper>
          <BackBtn onClick={() => navigate(-1)}>
            <RiArrowGoBackFill /> 이전으로
          </BackBtn>
        </BackWrapper>
            <ControlDiv>
              <InfoDiv>
              <PlaylistJacket src={playlist.image || "/default-thumbnail.jpg"} alt="Playlist Cover" />
                <div style={{ width: "100%" }}>
                  <ListTitle>{playlist.title.replace(/\\/g, "")}</ListTitle>
                  <RecMenuDiv />
                </div>
              </InfoDiv>
            </ControlDiv>

            {/* 노래 목록이 없을 때 메시지 표시 */}
            {playlist.songs && playlist.songs.length > 0 ? (
              <SongList showAll={true} headerTitle="번호"
                songs={playlist.songs.map((song, index) => ({
                  ...song,
                  number: index + 1,
                  playTimeFormatted: formatPlayTime(song.playTime),
                }))}
                onPlay={onPlay}
               />
            ) : (
              <NoSongsMessage>No songs available in this playlist</NoSongsMessage>
            )}
          </>
        )}
      </Container>
    </Wrapper>
  );
}

export default MyPlaylistInfo;
