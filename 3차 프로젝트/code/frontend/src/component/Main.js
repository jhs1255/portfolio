import React from "react";
import { Routes, Route } from "react-router-dom";
import TodayChart from "./pages/TodayChart";
import Recommend from "./pages/Recommend";
import PlaylistInfo from "./pages/PlaylistInfo";
import MyPage from "./pages/MyPage";
import { Wrapper, Container } from "./ui/AllDiv";

function Main({ onPlay }) {
  return (
    <Wrapper>
      <Container>
        <Routes>
          <Route path="/" element={<TodayChart onPlay={onPlay} />} />
          <Route path="/chart" element={<TodayChart onPlay={onPlay} />} />
          <Route path="/recommend" element={<Recommend onPlay={onPlay} />} />
          <Route path="/playlist/:id" element={<PlaylistInfo onPlay={onPlay} />} />
          <Route path="/myPage" element={<MyPage onPlay={onPlay} />} />
        </Routes>
      </Container>
    </Wrapper>
  );
}
export default Main;
