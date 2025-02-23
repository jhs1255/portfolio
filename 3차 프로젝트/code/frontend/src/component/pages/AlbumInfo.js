import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { BackBtn } from "../ui/Buttons";
import SongList from "../ui/SongList";
import { RiArrowGoBackFill } from "react-icons/ri";
import { FaChevronRight } from "react-icons/fa";
import { Wrapper, Container, BackWrapper, InfoDiv } from "../ui/AllDiv";
import RecMenuDiv from "../ui/MenuDiv";

const ControlDiv = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
  width: 100%;
`;

const TitleP = styled.p`
  font-weight: bold;
`;

function AlbumInfo({ onPlay }) {
  const navigate = useNavigate();
  const { albumId } = useParams();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const artistName = queryParams.get("artist");

  const [albumData, setAlbumData] = useState({});
  const [songs, setSongs] = useState([]);
  const [likedSongs, setLikedSongs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAlbumInfo = async () => {
      try {
        if (!albumId) {
          setError("앨범 ID가 존재하지 않습니다.");
          return;
        }

        const apiUrl = `http://localhost:8080/search/detail?keyword=ALBUM&id=${albumId}`;
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error(`API 요청 실패: ${response.status}`);

        const albumDetail = await response.json();

        if (!albumDetail?.listInfo?.dataList) {
          throw new Error("올바른 데이터 구조가 아닙니다.");
        }

        setAlbumData({
          id: albumDetail.id || albumId,
          name: albumDetail.album || "앨범 정보 없음",
          artist: albumDetail.artist || "아티스트 정보 없음",
          cover:
            albumDetail.listInfo.dataList[0]?.image ||
            "https://via.placeholder.com/150",
        });

        setSongs(
          albumDetail.listInfo.dataList.map((song, index) => ({
            id: song.id || `song-${index}`,
            track: song.track || "곡 정보 없음",
            artist: song.artist || "아티스트 정보 없음",
            image: song.image || "https://via.placeholder.com/50",
            playTime: song.playTime || 180000,
            album: albumDetail.album || "앨범 정보 없음",
            albumId: albumDetail.id || albumId,
            number: index + 1,
          }))
        );
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (albumId) fetchAlbumInfo();
  }, [albumId]);

  const fetchArtistId = async (artistName) => {
    try {
      const encodedArtistName = encodeURIComponent(artistName);
      const response = await fetch(
        `http://localhost:8080/search/?keyword=ARTIST&value=${encodedArtistName}`
      );
  
      if (!response.ok) {
        throw new Error(`서버 응답 실패: ${response.status}`);
      }
  
      const data = await response.json();
  
      if (!data?.dataList || data.dataList.length === 0) {
        throw new Error(`"${artistName}" 아티스트를 찾을 수 없습니다.`);
      }
  
      return data.dataList[0].id; // 첫 번째 검색 결과의 ID 반환
    } catch (error) {
      console.error("아티스트 ID를 가져오는 중 오류 발생:", error);
      return null;
    }
  };  

  const handleArtistClick = (e, artistId) => {
    e.preventDefault();
    if (artistId) {
      navigate(`/artist/${artistId}`);
    } else {
      alert("아티스트 정보를 찾을 수 없습니다.");
    }
  };  

  const handleBackClick = () => {
    navigate(-1);
  };

  if (loading) return <p>로딩 중...</p>;
  if (error)
    return (
      <p>
        페이지 로딩 중 오류가 발생했습니다.
        <br />{error}
      </p>
    );
  
  const handlePlaySong = (song) => {
    if (!onPlay) {
      console.error("🚨 onPlay 함수가 정의되지 않았음");
      return;
    }

    console.log("🎵 재생할 곡 정보:", song);
    onPlay(song);  // ✅ 부모 컴포넌트(App.js)로 곡 정보 전달
  };

  return (
    <Wrapper>
      <Container>
        <BackWrapper>
          <BackBtn onClick={handleBackClick}>
            <RiArrowGoBackFill /> 이전으로
          </BackBtn>
        </BackWrapper>

        <ControlDiv>
          <InfoDiv>
            <img
              src={albumData?.cover || "https://via.placeholder.com/150"}
              alt="album cover"
              onError={(e) =>
                (e.target.src = "https://via.placeholder.com/150")
              }
              style={{ width: "150px", height: "150px", borderRadius: "8px" }}
            />
            <div>
              <TitleP>{albumData?.name || "앨범 정보 없음"}</TitleP>
              <p>
                <a
                  href={`/artist/${albumData?.artist?.id}`}
                  style={{ textDecoration: "none", color: "black" }}
                  onClick={(e) => handleArtistClick(e, albumData.artist)}
                >
                  {albumData?.artist || "아티스트 정보 없음"} <FaChevronRight />
                </a>
              </p>
            </div>
          </InfoDiv>
        </ControlDiv>

        <RecMenuDiv />

        <SongList
          showAll={songs.length}
          headerTitle="번호"
          songs={songs}
          likedSongs={likedSongs}
          onToggleLike={(songRank) =>
            setLikedSongs((prev) =>
              prev.includes(songRank)
                ? prev.filter((rank) => rank !== songRank)
                : [...prev, songRank]
            )
          }
          onPlay={handlePlaySong}
        />
      </Container>
    </Wrapper>
  );
}

export default AlbumInfo;
