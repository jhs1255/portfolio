const express = require("express");
const youtubeDl = require("youtube-dl-exec");
const path = require("path");
const ffmpegPath = require("ffmpeg-static");
const ffmpeg = require("fluent-ffmpeg");
const fs = require("fs");

const app = express();
const port = 3000;

app.get("/download", async (req, res) => {
  const videoUrl = req.query.url;
  if (!videoUrl) {
    return res.status(400).send("유튜브 URL을 제공해주세요!");
  }
  const videoId = videoUrl.split("v=")[1];
  const outputFileName = `${videoId}.mp3`;
  //특정 경로에 파일이 있다면
  const filePath = path.join(__dirname, "song", `${videoId}.mp3`);
  try {
    // 파일 존재 여부 및 접근 가능 여부 확인
    fs.accessSync(filePath, fs.constants.F_OK);
    console.log("파일이 존재합니다!");
    const stat = fs.statSync(filePath);
    const fileSize = stat.size;
    res.setHeader(
      "Content-Disposition",
      `attachment; filename="${outputFileName}"`
    );
    res.setHeader("Content-Type", "audio/mpeg");

    res.sendFile(filePath, (err) => {
      if (err) {
        console.error("파일 전송 오류:", err);
        res.status(500).send("파일 전송 실패!");
      }
    });
    return;
  } catch (err) {
    console.log("파일이 존재하지 않습니다.");
  }

  // 상대 경로를 사용하여 경로 설정
  const videoFilePath = path.join("./webm", `${videoId}.webm`);
  const outputFilePath = path.join("./song", outputFileName);

  try {
    // 유튜브 영상 다운로드
    await youtubeDl(videoUrl, {
      output: videoFilePath,
    });

    // ffmpeg를 사용해 변환
    ffmpeg()
      .input(videoFilePath)
      .setFfmpegPath(ffmpegPath)
      .output(outputFilePath)
      .on("end", () => {
        console.log("변환 완료!");
        console.log(outputFilePath);
        res.setHeader(
          "Content-Disposition",
          `attachment; filename="${outputFileName}"`
        );
        res.setHeader("Content-Type", "audio/mpeg");
        res.sendFile(path.join(__dirname, "song", outputFileName), (err) => {
          if (err) {
            console.error("파일 전송 오류:", err);
            res.status(500).send("파일 전송 실패!");
          }
        });
      })
      .on("error", (err) => {
        console.error("ffmpeg 오류:", err);
        res.status(500).send("오디오 변환 실패!");
      })
      .run();
  } catch (error) {
    console.error("다운로드 실패:", error);
    res.status(500).send("다운로드 실패!");
  }
});

app.listen(port, () => {
  console.log(`서버 실행 중: http://localhost:${port}`);
});
