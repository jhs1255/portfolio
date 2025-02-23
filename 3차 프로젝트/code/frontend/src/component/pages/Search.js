import styled from "styled-components";
import { Link, useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import { FaChevronRight, FaChevronUp } from "react-icons/fa";
import SongList from "../ui/SongList";
import { Wrapper, Container } from "../ui/AllDiv";

const ResultP = styled.p`
  font-size: 1.5rem;
  margin-top: 30px;
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

const ListDiv = styled.div`
  margin: 0;
  padding: 0;
  width: 100%;
`;

const ListGrid = styled.div`
  margin: 10px 40px;
  display: grid;
 grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 10px;
`;

const ListItem = styled.div`
width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
  word-wrap: break-word;
  div {
    display: flex;
    flex-direction: column;
  }
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
  font-weight: bold;
  max-width: 200px; 
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const SubtitleP = styled.p`
  font-weight: 100;
  font-size: 0.9rem;
  max-width: 150px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const ArtistItem = styled(ListItem)`
  img {
    border-radius: 50%;
    object-fit: cover;
  }
`;
const NoResultsText = styled.p`
  color: #777;
  font-size: 1rem;
  text-align: center;
  margin-top: 10px;
`;

function Search({ onPlay }) {
  const scrollPosition = useRef(0);
  const location = useLocation();
  const query = new URLSearchParams(location.search).get("query") || "";
  const navigate = useNavigate();

  const [songs, setSongs] = useState([]);
  const [albums, setAlbums] = useState([]);
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [showAllSongs, setShowAllSongs] = useState(false);
  const [showAllAlbums, setShowAllAlbums] = useState(false);
  const [showAllArtists, setShowAllArtists] = useState(false);

  const fetchAlbumId = async (albumName) => {
    try {
      const response = await fetch(`http://localhost:8080/search/?keyword=ALBUM&value=${encodeURIComponent(albumName)}`);
      if (!response.ok) {
        throw new Error(`서버 응답 실패: ${response.status}`);
      }
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

  const handleAlbumClick = async (e, albumName) => {
    e.preventDefault();  // 링크 기본 동작을 막음

    const albumId = await fetchAlbumId(albumName);  // 앨범명으로 앨범 ID 가져오기
    if (albumId) {
      navigate(`/album/${albumId}`);  // 앨범 ID로 이동
    }
  };

  const handleToggle = (setter) => {
    scrollPosition.current = window.scrollY; // 현재 스크롤 위치 저장
    setter((prev) => !prev); // 상태 변경
  };

  useEffect(() => {
    window.scrollTo(0, scrollPosition.current); // 스크롤 위치 복원
  }, [showAllSongs, showAllAlbums, showAllArtists]);

  useEffect(() => {
    if (!query) return;
    setLoading(true);
    setError(null);

    const controller = new AbortController();
    const signal = controller.signal;

    const songUrl = `http://localhost:8080/search/?keyword=SONG&value=${query}&size=${showAllSongs ? 20 : 5}`;
    const albumUrl = `http://localhost:8080/search/?keyword=ALBUM&value=${query}&size=${showAllAlbums ? 20 : 4}`;
    const artistUrl = `http://localhost:8080/search/?keyword=ARTIST&value=${query}&size=${showAllArtists ? 20 : 4}`;

    Promise.all([
      fetch(songUrl, {signal}).then((res) => res.json()),
      fetch(albumUrl, {signal}).then((res) => res.json()),
      fetch(artistUrl, {signal}).then((res) => res.json()),
    ])
      .then(([songData, albumData, artistData]) => {
        if(!signal.aborted){
          setSongs(songData.dataList || []);
          setAlbums(albumData.dataList || []);
          setArtists(artistData.dataList || []);
        }
      })
      .catch((error) => {
        if (error.name === "AbortError") {
          console.log("요청이 취소되었습니다.");
        } else {
          console.error("데이터를 불러오는 중 오류 발생:", error);
          setError("데이터를 불러오는 데 실패했습니다.");
        }
      })
      .finally(() => {
        if (!signal.aborted) setLoading(false);
      });
  
    return () => {
      controller.abort(); // 컴포넌트 언마운트 시 요청 취소
    };
  }, [query, showAllSongs, showAllAlbums, showAllArtists]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  // playTime을 변환하는 함수
  const formatPlayTime = (playTime) => {
    if (!playTime || playTime === 0) return "3:00"; // playTime이 0이면 기본값 3:00

    const minutes = Math.floor(playTime / 60000); // 밀리초 → 분 변환
    const seconds = Math.floor((playTime % 60000) / 1000); // 밀리초 → 초 변환
    return `${minutes}:${seconds.toString().padStart(2, "0")}`; // 2자리 초 포맷
  };

  return (
    <Wrapper>
      <Container>
        <ResultP>
          '<span style={{ fontWeight: "bold" }}>{query}</span>'(으)로 검색한 결과
        </ResultP>

        {/* 곡 검색 결과 */}
        <Section>
          <ResultDiv>
            <TitleP>곡명으로 검색</TitleP>
            <button onClick={() => handleToggle(setShowAllSongs)} className="more-btn">
              {showAllSongs ? "접기" : "더보기"} {showAllSongs ? <FaChevronUp /> : <FaChevronRight />}
            </button>
          </ResultDiv>
          <ListDiv>
            {songs.length > 0 ? (
              <SongList
                showAll={true}
                headerTitle="번호"
                songs={songs.map((song, index) => ({
                  ...song,
                  number: index + 1,
                  playTimeFormatted: formatPlayTime(song.playTime),
                }))}
                onPlay={onPlay}
              />
            ) : (
              <NoResultsText>검색결과가 없습니다</NoResultsText>
            )}
          </ListDiv>
        </Section>

        {/* 앨범 검색 결과 */}
        <Section>
          <ResultDiv>
            <TitleP>앨범명으로 검색</TitleP>
            <button onClick={() => handleToggle(setShowAllAlbums)} className="more-btn">
              {showAllAlbums ? "접기" : "더보기"} {showAllAlbums ? <FaChevronUp /> : <FaChevronRight />}
            </button>
          </ResultDiv>

          {albums.length > 0 ? (
            <ListGrid>
              {(showAllAlbums ? albums : albums.slice(0, 4)).map((album) => (
                <ListItem key={album.id}>
                  <img src={album.url || "https://via.placeholder.com/100"} alt={album.name} />
                  <div>
                    <Link
                      to={`/album/${album.name}`}
                      style={{ textDecoration: "none", color: "black" }}
                      onClick={(e) => handleAlbumClick(e, album.name)} // 앨범 클릭 시 앨범 ID를 가져오는 함수 호출
                    >
                      <TitleP>{album.name}</TitleP>
                    </Link>
                    <Link to={`/artist/${album.artist}`} style={{ textDecoration: "none", color: "black" }}>
                      <SubtitleP>{album.artist}</SubtitleP>
                    </Link>
                  </div>
                </ListItem>
              ))}
            </ListGrid>
          ) : (
            <NoResultsText>검색결과가 없습니다</NoResultsText>
          )}
        </Section>

        {/* 아티스트 검색 결과 */}
        <Section>
          <ResultDiv>
            <TitleP>아티스트명으로 검색</TitleP>
            <button onClick={() => handleToggle(setShowAllArtists)} className="more-btn">
              {showAllArtists ? "접기" : "더보기"} {showAllArtists ? <FaChevronUp /> : <FaChevronRight />}
            </button>
          </ResultDiv>

          {artists.length > 0 ? (
            <ListGrid>
              {(showAllArtists ? artists : artists.slice(0, 4)).map((artist) => (
                <ArtistItem key={artist.id}>
                  <img src={artist.image || "https://via.placeholder.com/100"} alt={artist.name} />
                  <div>
                    <Link to={`/artist/${artist.name}`} style={{ textDecoration: "none", color: "black" }}>
                      <TitleP>{artist.name}</TitleP>
                      <SubtitleP>{artist.genre.join(", ")}</SubtitleP>
                    </Link>
                  </div>
                </ArtistItem>
              ))}
            </ListGrid>
          ) : (
            <NoResultsText>검색결과가 없습니다</NoResultsText>
          )}
        </Section>
      </Container>
    </Wrapper>
  );
}

export default Search;
