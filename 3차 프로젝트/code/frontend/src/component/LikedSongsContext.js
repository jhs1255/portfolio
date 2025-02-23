import React, { createContext, useContext, useState, useEffect } from 'react';

// LikedSongsContext 생성
const LikedSongsContext = createContext();

export const useLikedSongs = () => {
  const context = useContext(LikedSongsContext);
  if (!context) {
    throw new Error('useLikedSongs must be used within a LikedSongsProvider');
  }
  return context;
};

export const LikedSongsProvider = ({ children }) => {
  // const [likedSongs, setLikedSongs] = useState([]);

  const [likedSongs, setLikedSongs] = useState(() => {
    // 🔥 localStorage에서 기존 좋아요한 노래 가져오기
    const savedSongs = localStorage.getItem("likedSongs");
    return savedSongs ? JSON.parse(savedSongs) : [];
  });


  const [loading, setLoading] = useState(true);

  // 로그인 시 "내가 좋아하는 노래" 플레이리스트 정보를 가져와서 likedSongs 상태에 설정
  useEffect(() => {
    const fetchLikedSongs = async () => {
      const token = sessionStorage.getItem("access_token");
      if (!token) {
        setLoading(false);
        return;
      }

      try {
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
        
        // if (favoritePlaylist) {
        //   setLikedSongs(favoritePlaylist.songs || []); // '내가 좋아하는 노래'의 노래 목록을 상태로 저장
        // }

        if (favoritePlaylist) {
          const serverLikedSongs = (favoritePlaylist.songs || []).map(song => ({
            ...song,
            liked: true,
          }));

          // 🔥 localStorage에 저장된 좋아요 목록과 병합
          const localStorageLikedSongs = localStorage.getItem("likedSongs")
            ? JSON.parse(localStorage.getItem("likedSongs"))
            : [];

          // ID 기준으로 중복 제거하여 병합
          const mergedLikedSongs = [...new Map(
            [...localStorageLikedSongs, ...serverLikedSongs].map(song => [song.id, song])
          ).values()];

          setLikedSongs(mergedLikedSongs);
          localStorage.setItem("likedSongs", JSON.stringify(mergedLikedSongs));
        }

      } catch (error) {
        console.error("🚨 오류 발생:", error);
      } finally {
        setLoading(false); // 데이터 로딩 완료
      }
    };

    fetchLikedSongs();
  }, []);

   // 🔥 likedSongs 변경될 때마다 localStorage 업데이트
  useEffect(() => {
    if (likedSongs.length > 0) {
      localStorage.setItem("likedSongs", JSON.stringify(likedSongs));
    }
  }, [likedSongs]);

  return (
    <LikedSongsContext.Provider value={{ likedSongs, setLikedSongs, loading }}>
      {children}
    </LikedSongsContext.Provider>
  );
};