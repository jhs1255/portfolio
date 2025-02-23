import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { useNavigate, Link, useParams } from "react-router-dom";
import { BackBtn } from "../ui/Buttons";
import { FaChevronRight } from "react-icons/fa";
import { RiArrowGoBackFill } from "react-icons/ri";
import {
  Wrapper,
  Container,
  BackWrapper,
  InfoDiv as songinfo,
} from "../ui/AllDiv";
import ActionButtons from "../ui/ActionButtons";

const SongInfoDiv = styled(songinfo)`
  padding-bottom: 30px;
  border-bottom: 1px solid #ccc;
  p {
    margin: 5px;
  }
`;

const ControlDiv = styled.div`
  display: flex;
  width: calc(100% - 200px);
  flex-direction: row;
  justify-content: space-between;
`;

const LiricDiv = styled.div`
  text-align: left;
  margin: 20px 10px;
  p {
    margin: 5px 0;
  }
  .paragraph {
    margin: 20px 0;
  }
`;

const TitleP = styled.p`
  font-weight: bold;
`;

function SongInfo() {
  const { songId } = useParams();
  const navigate = useNavigate();
  const [songData, setSongData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSongInfo = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/search/detail?keyword=SONG&id=${songId}`
        );
        if (!response.ok) throw new Error("데이터 불러오기 실패");

        const data = await response.json();
        setSongData(data);
      } catch (error) {
        console.error("노래 정보를 불러오는 중 오류 발생:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchSongInfo();
  }, [songId]);

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
    e.preventDefault();

    const albumId = await fetchAlbumId(albumName);
    if (albumId) {
      navigate(`/album/${albumId}`);
    }
  };

  if (loading) {
    return <p>로딩 중...</p>;
  }

  if (!songData || !songData.artistName) {
    return <p>노래 정보를 불러올 수 없습니다.</p>;
  }

  // API에서 가져온 데이터 매핑
  const songTitle = songData?.songName || "제목 없음";
  const artistName = songData?.artistName || "아티스트 정보 없음";
  const albumName = songData?.albumName || "앨범 정보 없음";
  const lyrics = songData?.songLyrics || "가사 정보 없음";
  const albumImage = songData?.imageUrl || "/default-album.png";

  const handleBackClick = () => {
    navigate(-1);
  };

  return (
    <Wrapper>
      <Container>
        <SongInfoDiv>
          <BackWrapper>
            <BackBtn onClick={handleBackClick}>
              <RiArrowGoBackFill /> 이전으로
            </BackBtn>
          </BackWrapper>
          <img src={albumImage} alt="앨범 이미지" />
          <ControlDiv>
            <div>
              <TitleP>
                <a
                  href={`/album/${albumName}`}
                  style={{ textDecoration: "none", color: "black" }}
                  onClick={(e) => handleAlbumClick(e, albumName)}
                >
                  {albumName} <FaChevronRight />
                </a>
              </TitleP>
              <p>{songTitle}</p>
              <p>
                <Link
                  to={`/artist/${encodeURIComponent(artistName)}`}
                  style={{ textDecoration: "none", color: "black" }}
                >
                  {artistName} <FaChevronRight />
                </Link>
              </p>
              <ActionButtons />
            </div>
          </ControlDiv>
        </SongInfoDiv>

        <LiricDiv>
          <TitleP>가사</TitleP>
          <p dangerouslySetInnerHTML={{ __html: lyrics }} />
        </LiricDiv>
      </Container>
    </Wrapper>
  );
}

export default SongInfo;
