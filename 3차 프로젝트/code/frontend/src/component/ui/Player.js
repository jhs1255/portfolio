import styled from "styled-components";
import React, { useState, useEffect, useRef } from "react";
import { FaShuffle, FaRepeat } from "react-icons/fa6";
import { CgPlayTrackPrevO, CgPlayTrackNextO } from "react-icons/cg";
import { FaRegPlayCircle, FaRegPauseCircle } from "react-icons/fa";
import PlaySongList from "./PlaySongList";

const Wrapper = styled.div`
  position: fixed;
  right: 0;
  display: flex;
  flex-direction: column;
  width: 250px;
  height: 100vh;
  border-left: 3px solid #ccc;
  background-color: #fff;

  @media (max-width: 768px) {
    width: 100%;
    height: 100vh;
    display: none;
  }
`;

const Container = styled.div`
  justify-content: flex-end;
`;

const SongPlayDiv = styled.div`
  flex: 1;
  margin: 20px 15px;
  border-bottom: 2px solid #ccc;
`;

const AlbumJacket = styled.div`
  width: 180px;
  height: 180px;
  background-color: #ccc;
  margin: 0 auto;
`;

const SongTitleP = styled.p`
  font-size: 1.2rem;
  font-weight: bold;
  margin: 10px;
`;

const ArtistP = styled.p`
  margin: 0;
`;

const PlaySettingDiv = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 0 10px;
`;

const PlaySettingBtn = styled.button`
  border: none;
  background-color: #fff;
  cursor: pointer;
`;
const ProgressDiv = styled.div`
  margin: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
  position: relative;
  span {
    font-size: 0.8rem;
  }
  .progress-container {
    flex: 1;
    position: relative;
    height: 6px;
    background-color: #eee;
    border-radius: 5px;
    cursor: pointer;
  }

  .progress-bar {
    height: 100%;
    background-color: #c69fda;
    border-radius: 5px;
    width: ${(props) => props.$progress}%;
  }

  .progress-circle {
    position: absolute;
    top: 50%;
    left: ${(props) => props.$progress}%;
    transform: translate(-50%, -50%);
    width: 12px;
    height: 12px;
    background-color: ${(props) => (props.$isDragging ? "#822fa0" : "#c69fda")};
    border-radius: 50%;
    transition: background-color 0.2s;
  }
`;
const PlayDiv = styled.div`
  margin: 10px;
  button {
    margin: 0 5px;
    font-size: 30px;
  }
`;

const CompactPlayerWrapper = styled.div`
  display: ${({ $isExpanded }) => ($isExpanded ? "none" : "flex")};
  justify-content: center;
  background-color: #fff;
  border-top: 2px solid #ccc;
  position: fixed;
  bottom: 70px;
  width: 100%;
  z-index: 9999;

  @media (min-width: 769px) {
    display: none !important;
  }
`;
const CompactPlayer = styled.div`
  flex-direction: row;
  padding: 10px;
  background-color: #fff;
  cursor: pointer;
  text-align: center;
  border-top: 2px solid #ccc;

  p {
    margin: 0;
    text-align: left;
  }
  button {
    font-size: 3rem;
    margin-left: auto;
    margin-right: 15px;
  }
  @media (max-width: 768px) {
    position: fixed;
    width: 100%;
    display: flex !important;
    flex-direction: row;
  }
`;
const CompactAlbumJacket = styled.div`
  width: 50px;
  height: 50px;
  background-color: #ccc;
  margin: 0 10px;
`;
const CompactSongInfo = styled.div`
  display: flex;
  flex-direction: column;
  flex: 2;
`;
const CompactPlayDiv = styled.div`
  display: flex;
  gap: 0;
`;
function Player({ playlist: propPlaylist, setPlaylist }) {
  const [isPlaying, setIsPlaying] = useState(false);
  const [isExpanded, setIsExpanded] = useState(true);
  const [currentSong, setCurrentSong] = useState({});
  const [currentIndex, setCurrentIndex] = useState(0);
  const [progress, setProgress] = useState(0);
  const [currentTime, setCurrentTime] = useState(0);
  const [totalDuration, setTotalDuration] = useState(180);
  const [isDragging, setIsDragging] = useState(false);
  const [previousPressedOnce, setPreviousPressedOnce] = useState(false);
  const progressRef = useRef(null);
  const [playlist, setLocalPlaylist] = useState(propPlaylist); // 내부 상태 관리
  const audioRef = useRef(new Audio());

  // ✅ propPlaylist가 변경될 때, Player 내부 상태 업데이트
  useEffect(() => {
    setLocalPlaylist(propPlaylist);
  }, [propPlaylist]);

  useEffect(() => {
    const handleUserInteraction = () => {
      audioRef.current.muted = false; // 🔥 자동 재생 차단 해결
      document.removeEventListener("click", handleUserInteraction);
    };

    document.addEventListener("click", handleUserInteraction);

    return () => {
      document.removeEventListener("click", handleUserInteraction);
    };
  }, []);

  // ✅ 최신 곡이 자동으로 재생되도록 설정
  useEffect(() => {
    if (playlist.length > 0) {
      setCurrentSong(playlist[playlist.length - 1]); // 최신 곡을 재생
    }
    console.log("🎶 Player 업데이트된 playlist:", playlist); // 확인용
  }, [playlist]);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth <= 768) {
        setIsExpanded(false);
      } else {
        setIsExpanded(true);
      }
    };

    handleResize();

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);
  const togglePlayerView = () => {
    setIsExpanded((prev) => !prev);
  };

  useEffect(() => {
    if (playlist.length > 0) {
      setCurrentSong(playlist[currentIndex]);
    }
  }, [currentIndex, playlist]);
  // ✅ 서버에 재생 요청을 보내는 함수
  const requestPlayFromServer = async () => {
    if (!currentSong) {
      alert("재생할 곡이 없습니다.");
      return;
    }

    const token = sessionStorage.getItem("access_token");
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      console.log(`🎵 서버로 재생 요청: ${currentSong.id}`);
      console.log(`서버로 보내는 토큰 :  ${token}`);

      const response = await fetch(`http://localhost:8080/stream/play/${currentSong.id}`, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`서버 오류: ${response.status}`);
      }

      const audioBlob = await response.blob(); // 🔥 스트림을 Blob으로 변환
      const audioUrl = URL.createObjectURL(audioBlob);

      audioRef.current.src = audioUrl;
      audioRef.current.volume = 1.0; // 🔊 볼륨 조절
      audioRef.current.play(); // 🔥 실제 재생 시작

      console.log(`🎵 서버에서 재생 요청 성공: ${currentSong.track}`);
      setIsPlaying(true); // ✅ 실제 재생 상태 변경

    } catch (error) {
      console.error("🚨 재생 요청 중 오류 발생:", error);
      playNext();
      alert("음악을 재생하는 도중 오류가 발생했습니다.");
    }
  };

  // ✅ Play/Pause 버튼 클릭 이벤트
  const togglePlay = () => {
    if (!isPlaying) {
      requestPlayFromServer(); // ✅ 서버에 요청 후 재생
    } else {
      audioRef.current.pause(); // ⏸️ 일시정지
      setIsPlaying(false); // ✅ 일시정지
    }
  };

  const playNext = () => {
    setCurrentIndex((prev) => (prev < playlist.length - 1 ? prev + 1 : 0));
    setIsPlaying(true);
  };
  const playPrev = () => {
    if (currentTime >= 10 && !previousPressedOnce) {
      // 10초 이상 진행된 상태에서 이전곡을 한 번 눌렀을 때, 현재 곡을 0:00으로 초기화
      setPreviousPressedOnce(true);
      setCurrentTime(0);
      setProgress(0);
    } else {
      // 이전 곡 버튼을 다시 누르면 이전 곡으로 돌아감
      setPreviousPressedOnce(false);
      setCurrentIndex((prev) => (prev > 0 ? prev - 1 : playlist.length - 1));
      setCurrentTime(0);
      setProgress(0);
      setIsPlaying(true);
    }
  };

  useEffect(() => {
    if (currentSong && isPlaying) { 
      requestPlayFromServer(); // ✅ 곡이 변경되었을 때 자동 재생
    }
  }, [currentSong]);

  useEffect(() => {
    if (playlist.length > 0) {
      const newSong = playlist[currentIndex];
      let duration = newSong.playTime; // 서버에서 받은 playTime

      if (!duration || duration === 0) {
        duration = 190000; // 🔥 playTime이 0이면 3분 10초(190초)로 설정
      }

      setCurrentSong(newSong);
      setTotalDuration(duration);
      setProgress(0); // 노래가 바뀌면 프로그레스 바 초기화
      setCurrentTime(0); // 시작 시간 0초로 초기화
    }
  }, [currentIndex, playlist]);

  // ✅ 음악 재생이 끝나면 자동으로 다음 곡 재생
  useEffect(() => {
    const audio = audioRef.current;
    
    // 🔥 음악이 끝났을 때 자동으로 다음 곡으로 이동
    audio.onended = () => {
      console.log("🎵 음악 종료! 다음 곡으로 이동합니다.");
      playNext();
    };

    // 🔥 재생 시간이 3분 10초를 초과하면 강제적으로 다음 곡으로 이동
    const timeoutId = setTimeout(() => {
      if (isPlaying) {
        console.log("⏳ 3분 10초 경과! 강제로 다음 곡으로 이동합니다.");
        playNext();
      }
    }, 190000); // 3분 10초 = 190000ms

    // 🔥 음악이 로드되지 않거나 오류가 발생하면 자동으로 다음 곡 재생
    audio.onerror = () => {
      console.error("🚨 음악 재생 오류 발생! 다음 곡으로 이동합니다.");
      playNext();
    };

    return () => {
      clearTimeout(timeoutId); // ✅ 이전 타이머 제거
      audio.onended = null;
      audio.onerror = null;
    };
}, [currentSong, isPlaying]);

  useEffect(() => {
    const audio = audioRef.current;

    if (isPlaying && !isDragging) {
      const updateProgress = () => {
        setCurrentTime(audio.currentTime * 1000); // 🔥 초 → 밀리초 변환
        setProgress((audio.currentTime / (totalDuration / 1000)) * 100); // 비율 계산
      };

      audio.ontimeupdate = updateProgress;

      return () => {
        audio.ontimeupdate = null;
      };
    }
  }, [isPlaying, currentTime, totalDuration, isDragging]);

  const formatTime = (milliseconds) => {
    if (!milliseconds || isNaN(milliseconds)) return "0:00";
  
    const totalSeconds = Math.floor(milliseconds / 1000); // 🔥 밀리초 → 초 변환
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
  
    return `${minutes}:${seconds.toString().padStart(2, "0")}`;
  };  
  
  const handleProgressClick = (e) => {
    if (!progressRef.current) return;

    const rect = progressRef.current.getBoundingClientRect();
    const clickX = e.clientX - rect.left;
    const newProgress = (clickX / rect.width) * 100;
    const newTime = (newProgress / 100) * totalDuration;

    setProgress(newProgress);
    setCurrentTime(Math.floor(newTime));
    audioRef.current.currentTime = newTime;
  };

  const handleDragStart = (e) => {
    setIsDragging(true);
    handleDrag(e);
  };

  //드래그 중 (진행 바 이동)
  const handleDrag = (e) => {
    if (!progressRef.current || !isDragging) return;

    const clientX = e.type.includes("touch") ? e.touches[0].clientX : e.clientX;
    const rect = progressRef.current.getBoundingClientRect();
    let newProgress = ((clientX - rect.left) / rect.width) * 100;
    newProgress = Math.max(0, Math.min(100, newProgress));

    const newTime = (newProgress / 100) * (totalDuration / 1000);
    setProgress(newProgress);
    setCurrentTime(newTime * 1000);
  };

  const handleDragEnd = () => {
    setIsDragging(false);
    audioRef.current.currentTime = currentTime / 1000;
  };

  // ✅ 플레이리스트 전체 곡을 추가하는 함수
  const handlePlaySongs = (songs) => {
    setPlaylist(songs); // 전체 플레이리스트 교체
    setCurrentSong(songs[0]); // 첫 번째 곡 재생
    setCurrentIndex(0);
  };
  return (
    <>
      {isExpanded && (
        <Wrapper>
          {/* 전체 플레이어 */}
          <Container>
            <SongPlayDiv>
              <AlbumJacket>
                <img
                  src={currentSong.image}
                  alt={currentSong.track}
                  width="180"
                  height="180"
                />
              </AlbumJacket>
              <SongTitleP>
                {currentSong ? currentSong.track : "No Song"}
              </SongTitleP>
              <ArtistP>
                {currentSong ? currentSong.artist : "No Artist"}
              </ArtistP>
              <PlaySettingDiv>
                <PlaySettingBtn>
                  <FaShuffle />
                </PlaySettingBtn>
                <PlaySettingBtn>
                  <FaRepeat />
                </PlaySettingBtn>
              </PlaySettingDiv>
              <ProgressDiv
                ref={progressRef}
                $progress={progress}
                $isDragging={isDragging}
                onClick={handleProgressClick}
                onMouseDown={handleDragStart}
                onMouseMove={handleDrag}
                onMouseUp={handleDragEnd}
                onMouseLeave={handleDragEnd}
                onTouchStart={handleDragStart}
                onTouchMove={handleDrag}
                onTouchEnd={handleDragEnd}
              >
                <span id="start-time">{formatTime(currentTime)}</span>
                <div
                  className="progress-container"
                  onMouseDown={handleDragStart}
                >
                  <div className="progress-bar"></div>
                  <div className="progress-circle"></div>
                </div>
                <span id="end-time">{formatTime(totalDuration)}</span>
              </ProgressDiv>

              <PlayDiv>
                <PlaySettingBtn onClick={playPrev}>
                  <CgPlayTrackPrevO />
                </PlaySettingBtn>
                <PlaySettingBtn onClick={togglePlay}>
                  {isPlaying ? <FaRegPauseCircle /> : <FaRegPlayCircle />}
                </PlaySettingBtn>
                <PlaySettingBtn onClick={playNext}>
                  <CgPlayTrackNextO />
                </PlaySettingBtn>
              </PlayDiv>
            </SongPlayDiv>
            <PlaySongList
              playlist={playlist}
              setCurrentSong={setCurrentSong}
              setCurrentIndex={setCurrentIndex}
              setPlaylist={setPlaylist}
              onPlay={handlePlaySongs}
            />
          </Container>
        </Wrapper>
      )}

      {!isExpanded && (
        <CompactPlayerWrapper $isExpanded={isExpanded}>
          <CompactPlayer
            onClick={(e) => {
              // CompactPlayer 자체 클릭 시 아무 동작도 하지 않음
              e.stopPropagation();
            }}
          >
            <CompactAlbumJacket>
              <img
                  src={currentSong.image}
                  alt={currentSong.track}
                  width="100%"
                  height="100%"
                />
            </CompactAlbumJacket>
            <CompactSongInfo>
              <SongTitleP>{currentSong ? currentSong.track : "No Song"}</SongTitleP>
              <ArtistP>{currentSong ? currentSong.artist : "No Artist"}</ArtistP>
            </CompactSongInfo>
            <CompactPlayDiv>
              <PlaySettingBtn onClick={playPrev}>
                <CgPlayTrackPrevO />
              </PlaySettingBtn>
              <PlaySettingBtn
                onClick={(e) => {
                  e.stopPropagation(); // 부모 클릭 이벤트 방지
                  togglePlay(); // 재생/일시정지 상태 변경
                }}
              >
                {isPlaying ? <FaRegPauseCircle /> : <FaRegPlayCircle />}
              </PlaySettingBtn>
              <PlaySettingBtn onClick={playNext}>
                <CgPlayTrackNextO />
              </PlaySettingBtn>
            </CompactPlayDiv>
          </CompactPlayer>

          {/* Wrapper 클릭 시만 확장/축소 */}
          <div
            style={{
              position: "absolute",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
            }}
            onClick={togglePlayerView}
          />
        </CompactPlayerWrapper>
      )}
    </>
  );
}

export default Player;
