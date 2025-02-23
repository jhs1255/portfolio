package com.berry_comment.init;

import com.berry_comment.entity.*;
import com.berry_comment.repository.*;
import com.berry_comment.service.CrawlerService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class Init {
    Path path = Paths.get("C:\\berry\\RPlaylist");

    ArrayList<Path> fileList = new ArrayList<>();
    private final CrawlerService crawlerService;
    private final RecommendationRepository recommendationRepository;
    private final RecommendationDetailRepository recommendationDetailRepository;
    @Transactional
    public void init() throws IOException {
        try (Stream<Path> files = Files.walk(path)) {
            files.filter(file -> Files.isRegularFile(file) && file.toString().endsWith(".csv"))  // .csv 파일만 필터링
                    .forEach(file -> fileList.add(file));  // 파일 경로 출력
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Path file : fileList) {
            System.out.println(file.toString());
        }
        addDatabases();
    }

    public void addDatabases() {
        try {
            fileList.parallelStream().forEach(file -> {
                try (CSVReader csvReader = new CSVReader(new FileReader(file.toFile()))) {
                    String[] line;
                    // 첫 번째 줄은 헤더일 경우, 이를 건너뛴다.
                    csvReader.readNext();

                    // 추천 플레이리스트 이름
                    String recommendationName = file.toString().replace(path.toString(), "");
                    recommendationName = recommendationName.replace(".csv", ""); // 수정: replace()는 원본을 변경하지 않음
                    recommendationName = recommendationName.replace("/","");
                    System.out.println(recommendationName);

                    //추천 음악이름 저장
                    Recommendation recommendation = new Recommendation(recommendationName);
                    recommendationRepository.save(recommendation);

                    // 각 행을 읽어들이고 처리
                    while ((line = csvReader.readNext()) != null) {
                        String songId = line[0];       // 곡 ID
                        String artist = line[1];       // 아티스트
                        String songName = line[2];     // 곡 이름
                        String album = line[3];        // 앨범
                        String lyrics = line[4];       // 가사
                        String genre = line[5];        // 장르
                        String emotionRatio = line[6]; // 감정 비율
                        String finalEmotion = line[7]; // 최종 감정 분류

                        // 각 데이터를 출력
                        System.out.println("Song ID: " + songId);
                        System.out.println("Artist: " + artist);
                        System.out.println("Song Name: " + songName);
                        System.out.println("Album: " + album);
                        System.out.println("Lyrics: " + lyrics);
                        System.out.println("Genre: " + genre);
                        System.out.println("Emotion Ratio: " + emotionRatio);
                        System.out.println("Final Emotion: " + finalEmotion);
                        System.out.println("------------------------------------------------");

                        Song song = crawlerService.saveSongById(Integer.parseInt(songId));
                        RecommendationDetail recommendationDetail = new RecommendationDetail(song, recommendation);
                        recommendationDetailRepository.save(recommendationDetail);
                    }
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
