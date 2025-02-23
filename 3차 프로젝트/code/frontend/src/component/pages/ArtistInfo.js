import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import { useParams, useNavigate, Link } from "react-router-dom";
import { RiArrowGoBackFill } from "react-icons/ri";
import { FaChevronRight, FaChevronUp } from "react-icons/fa";
import { BackBtn } from "../ui/Buttons";
import SongList from "../ui/SongList";
import { Wrapper, Container, BackWrapper, InfoDiv } from "../ui/AllDiv";
import RecMenuDiv from "../ui/MenuDiv";

const NameP = styled.p`
  font-weight: bold;
  font-size: 1.2rem;
`;
const GenreP = styled.p`
  font-size: 1rem;
  color:#0b0b0b;
  margin-top: 10px;
`;

const ControlDiv = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
  width: 100%;
`;

const Section = styled.div`
  margin-bottom: 20px;
  button.more-btn {
    background: none;
    border: none;
    cursor: pointer;
    color: #007bff;
    display: flex;
    align-items: center;
    gap: 5px;
  }
`;

const ResultDiv = styled.div`
  display: flex;
  justify-content: space-between;
  p {
    margin-left: 20px;
  }
`;

const AlbumGrid = styled.div`
  margin: 10px 40px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
`;

const AlbumItem = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
  img {
    width: 100px;
    height: 100px;
    border-radius: 8px;
    object-fit: cover;
    background-color: #ccc;
  }
  p {
    margin: 2px 0;
    text-align: left;
  }
`;

const TitleP = styled.p`
 font-size:large;
  font-weight: bold;
`;

const StyledLink = styled(Link)`
  text-decoration: none;
  color: inherit;
  
  &:hover {
    text-decoration: underline;
  }
`;

const formatPlayTime = (playTime) => {
  if (!playTime || playTime === 0) return "3:00";
  const minutes = Math.floor(playTime / 60);
  const seconds = Math.floor(playTime % 60);
  return `${minutes}:${seconds.toString().padStart(2, "0")}`;
};

function ArtistInfo({ onPlay }) {
  const scrollPosition = useRef(0);
  const { artistName } = useParams();
  const navigate = useNavigate();
  const [showAllSongs, setShowAllSongs] = useState(false);
  const [showAllAlbums, setShowAllAlbums] = useState(false);
  const [artistData, setArtistData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [artistGenre, setArtistGenre] = useState([]);

  const [songs, setSongs] = useState([]);
  const [albums, setAlbums] = useState([]);
  const [totalSongs, setTotalSongs] = useState(0);
  const [totalAlbums, setTotalAlbums] = useState(0);

  // 아티스트 정보를 가져오는 함수
  const fetchArtistInfo = async () => {
    try {
      if (!artistName) {
        setError("아티스트 이름이 제공되지 않았습니다.");
        setLoading(false);
        return;
      }

      // 아티스트 ID 가져오기
      const idResponse = await fetch(
        `http://localhost:8080/search/?keyword=ARTIST&value=${encodeURIComponent(artistName)}`
      );
      if (!idResponse.ok)
        throw new Error("아티스트 ID를 가져오는데 실패했습니다.");
      const idData = await idResponse.json();
      if (!idData?.dataList?.length)
        throw new Error("해당 아티스트를 찾을 수 없습니다.");

      const artistId = idData.dataList[0].id;
      const artistGenre = idData.dataList[0].genre || [];
      setArtistGenre(artistGenre);

      // 아티스트 상세 정보 가져오기
      const infoResponse = await fetch(
        `http://localhost:8080/search/detail?keyword=ARTIST&id=${artistId}`
      );
      if (!infoResponse.ok)
        throw new Error("아티스트 정보를 가져오는데 실패했습니다.");
      const artistInfo = await infoResponse.json();

      // 아티스트 정보 저장
      setArtistData({
        id: artistId,
        name: artistInfo.artist.artistName,
        imageUrl: artistInfo.artist.imageUrl,
      });

      // 중복 제거하면서 곡 정보 저장 (track을 사용)
      const uniqueSongs = new Map();
      (artistInfo.songList?.dataList || []).forEach((song) => {
        const songTitle = song.track?.trim() || "제목 없음";
        if (!uniqueSongs.has(songTitle)) {
          uniqueSongs.set(songTitle, {
            id: song.id,
            track: songTitle,
            artist: song.artist?.trim() || artistName,
            album: song.album?.trim() || "알 수 없음",
            image: song.image || "https://via.placeholder.com/150",
            playTimeFormatted: formatPlayTime(song.playTime),
          });
        }
      });

      setSongs(Array.from(uniqueSongs.values()));
      setTotalSongs(artistInfo.songList.totalItems);

      const albumArray = (artistInfo.albumList?.dataList || []).map((album) => ({
        id: album.id,
        name: album.name || "제목 없음",
        cover: album.url || "https://via.placeholder.com/150",
      }));

      setAlbums(albumArray); // 상태 업데이트
      setTotalAlbums(artistInfo.albumList.totalItems);
    } catch (err) {
      console.error("Error fetching artist info:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchArtistInfo();
  }, [artistName]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  const handleBackClick = () => {
    navigate(-1);
  };

  const handleToggle = async (setter, type) => {
    scrollPosition.current = window.scrollY;
    setter((prev) => !prev);
  
    const nextSize = 5;
    let nextPage = Math.floor(songs.length / nextSize);
  
    if (!artistData?.id) {
      setError("아티스트 정보를 불러오는 중 오류가 발생했습니다.");
      return;
    }
  
    try {
      const response = await fetch(
        `http://localhost:8080/search/detail?keyword=ARTIST&id=${artistData.id}&page=${nextPage}&size=${nextSize}`
      );
      const data = await response.json();
  
      if (type === 'songs') {
        setSongs((prevSongs) => [
          ...prevSongs,
          ...data.songList.dataList.slice(prevSongs.length),
        ]);
      } else if (type === 'albums') {
        setAlbums((prevAlbums) => [
          ...prevAlbums,
          ...data.albumList.dataList.slice(prevAlbums.length),
        ]);
      }
    } catch (err) {
      setError("데이터를 불러오는 중 오류가 발생했습니다.");
      console.error("Error fetching data:", err);
    }
  };    

  return (
    <Wrapper>
      <Container>
        <BackWrapper>
          <BackBtn onClick={handleBackClick}>
            <RiArrowGoBackFill /> 이전으로
          </BackBtn>
        </BackWrapper>
        {/* 아티스트 정보 섹션 */}
        <ControlDiv>
          <InfoDiv>
            <img
              src={artistData?.imageUrl || "https://via.placeholder.com/150"}
              alt="artist cover"
              onError={(e) => {
                e.target.src = "https://via.placeholder.com/150";
              }}
              style={{ width: "150px", height: "150px", borderRadius: "8px" }}
            />
            <div>
              <NameP>{artistData?.name || "알 수 없음"}</NameP>
              <GenreP>{artistGenre.length > 0 ? artistGenre.join(", ") : ""}</GenreP>
            </div>
          </InfoDiv>
        </ControlDiv>
        <Section>
          <ResultDiv>
            <TitleP>발매곡</TitleP>
            <button onClick={() => handleToggle(setShowAllSongs, 'songs')} className="more-btn">
              {showAllSongs ? "접기" : "더보기"} {showAllSongs ? <FaChevronUp /> : <FaChevronRight />}
            </button>
          </ResultDiv>
          <RecMenuDiv />
          <SongList showAll={true} headerTitle="번호" songs={songs.slice(0, showAllSongs ? songs.length : 5)} onPlay={onPlay} />
        </Section>
        {/* 발매 앨범 섹션 */}
        <Section>
          <ResultDiv>
            <TitleP>발매 앨범</TitleP>
            <button onClick={() => handleToggle(setShowAllAlbums, 'albums')} className="more-btn">
              {showAllAlbums ? "접기" : "더보기"} {showAllAlbums ? <FaChevronUp /> : <FaChevronRight />}
            </button>
          </ResultDiv>
          {albums.length > 0 ? (
            <AlbumGrid>
              {(showAllAlbums ? albums : albums.slice(0, 4)).map((album) => {
                return (
                  <AlbumItem key={album.id}>
                    <img src={album.cover} alt={album.name} />
                    <div>
                      <StyledLink
                        to={`/album/${album.id}?artist=${encodeURIComponent(
                          artistData.name
                        )}`}
                      >
                        <GenreP>{album.name}</GenreP>
                      </StyledLink>
                    </div>
                  </AlbumItem>
                );
              })}
            </AlbumGrid>
          ) : (
            <p>앨범 정보가 없습니다.</p>
          )}
        </Section>
      </Container>
    </Wrapper>
  );
}

export default ArtistInfo;
