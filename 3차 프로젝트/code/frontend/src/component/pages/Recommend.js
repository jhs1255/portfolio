import React, { useEffect, useState } from "react";
import styled, { keyframes } from "styled-components";
import { Link } from "react-router-dom";
const StyledLink = styled(Link)`
  text-decoration: none;
  color: inherit;
`;
const Wrapper = styled.div`
  width: 100%;
  position: relative;
  overflow-x: hidden;
`;

const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  div {
    margin-left: 10px;
  }
  max-width: 100%;
`;
const colorChange = keyframes`
  0% {
    color: black;
  }
  50% {
    color: #68009b;
  }
  100% {
    color: black;
  }
`;
const TitleDiv=styled.div`
  text-align:left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  h2,
  p {
    margin: 5px 0;
    color: black;
    animation: ${colorChange} 2s ease-in-out forwards;
  }
  p {
    font-size: small;
    margin-bottom: 20px;
  }
`;
const PlaylistDiv = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  overflow-x: hidden;
  gap: 20px;
  width: 100%;
  box-sizing: border-box;
  position: relative;
`;

const PlaylistItem = styled.div`
  display: flex;
  flex-direction: row;
  text-align: left;
  padding: 15px 0;
  gap: 15px;
  align-items: center;
  box-sizing: border-box;
  overflow: hidden;
  text-overflow: ellipsis;
  p {
    font-weight: bold;
    font-size: 0.9rem;
    cursor: pointer;
    color: black;
    text-decoration: none;
    padding: 5px 8vw 5px 1px;
    &:hover {
      background-color: #c69fda;
      color:#fafafa;
      text-decoration: none;
    }
  }
`;

const PlaylistJacket = styled.img`
  width: 60px;
  height: 60px;
  background-color: yellow;
  object-fit: cover;
  cursor: pointer;
  border:none;
`;

const Divider = styled.div`
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  width: 1px;
  background-color: #ccc;
  z-index:-1;
`;
const Pagination = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 30px;
  span{
  width:40px;
  }
`;

const PageButton = styled.button`
  padding: 8px 12px;
  border: none;
  background-color: ${(props) => (props.disabled ? "#ccc" : "#68009b")};
  color: white;
  cursor: ${(props) => (props.disabled ? "not-allowed" : "pointer")};
  border-radius: 5px;
  &:hover {
    background-color: ${(props) => (props.disabled ? "#ccc" : "#4a0073")};
  }
`;
const getFirstSongImage = (songs) => {
  // 첫 번째 노래의 이미지 반환, 없으면 랜덤 색상 생성
  if (songs && songs[0] && songs[0].image) {
    console.log("First song image:", songs[0].image);  // 디버깅용 로그
    return songs[0].image;
  }
  // 랜덤 색상 생성 (RGB값으로)
  const randomColor = `rgb(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)})`;
  return randomColor;
};

function Recommend() {
  const [playlists, setPlaylists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
    async function fetchPlaylists() {
      setLoading(true);
      try {
        const token = sessionStorage.getItem("access_token");
        const response = await fetch(
          `http://localhost:8080/playList/recommend/thumb?page=${page}&size=10`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
  
        if (!response.ok) throw new Error("Failed to fetch playlists");
  
        const data = await response.json();
        setPlaylists(data.dataList || []);
        const totalItems = 20;
        setTotalPages(Math.ceil(totalItems/10));
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    }
    fetchPlaylists();
  }, [page]);

  // ✅ 플레이리스트가 비어있을 경우 PR.js 창을 자동으로 실행하는 useEffect
  useEffect(() => {
    if (playlists.length === 0 && !loading) {
      window.location.href = "/pr";
    }
  }, [playlists, loading]);

  
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (playlists.length === 0) return <p>No playlists available</p>;

  return (
    <Wrapper>
      <Container>
        <TitleDiv>
          <h2>Mood Recommend</h2>
          <p>베리코멘드에서 추천하는 맞춤 음악</p>
        </TitleDiv>
        <PlaylistDiv>
          <Divider />
          {playlists.map((playlist, index) => (
            <StyledLink
             key={playlist.id || `playlist-${index}`}
              to={`/playlist/${playlist.id}`}
              state={{playlistTitle: playlist.title}}
              >
              <PlaylistItem>
                <PlaylistJacket
                  src={getFirstSongImage(playlist.id)}
                  alt=""
                  aria-hidden="true"
                  style={{ backgroundColor: getFirstSongImage(playlist.songs)}}
                />
                <p>{playlist.title.replace(/\\/g, "")}</p>
              </PlaylistItem>
            </StyledLink>
          ))}
        </PlaylistDiv>
        {/* 페이지네이션 버튼 */}
        <Pagination>
          <PageButton disabled={page === 0} onClick={() => setPage((prev) => prev - 1)}>
            ◀
          </PageButton>
          <span>
            {page + 1} / {totalPages}
          </span>
          <PageButton disabled={page >= totalPages - 1} onClick={() => setPage((prev) => prev + 1)}>
            ▶
          </PageButton>
        </Pagination>
      </Container>
    </Wrapper>
  );

}

export default Recommend;
