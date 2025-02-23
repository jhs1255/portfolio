package com.berry_comment.service;

import com.berry_comment.dto.ListInfoDto;
import com.berry_comment.dto.PlayListDto;
import com.berry_comment.dto.RecommendationDto;
import com.berry_comment.dto.SongDto;
import com.berry_comment.entity.*;
import com.berry_comment.repository.PlayListRepository;
import com.berry_comment.repository.RecommendationDetailRepository;
import com.berry_comment.repository.RecommendationRepository;
import com.berry_comment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationDetailRepository recommendationDetailRepository;
    private final SongService songService;
    private final UserRepository userRepository;
    private final PlayListRepository playListRepository;
    private final JobLauncher jobLauncher;
    private final Job recommendationBatchJob;
    public ListInfoDto getRecommendation(Pageable pageable) {
        List<Recommendation> recommendationList = recommendationRepository.findAll(pageable).getContent();
        List<RecommendationDto> recommendationDtoList = new ArrayList<>();
        recommendationList.forEach(
                recommendation -> {
                    recommendationDtoList.add(
                            RecommendationDto.builder()
                                    .id(recommendation.getId())
                                    .title(recommendation.getTitle())
                                    .build());
                }
        );
        ListInfoDto listInfoDto = ListInfoDto.builder()
                .size(recommendationList.size())
                .dataList(recommendationDtoList)
                .build();
        return listInfoDto;
    }

    public ListInfoDto getRecommendationDetail(int id,Pageable pageable) {
        Slice< RecommendationDetail> recommendationDetailSlice = recommendationDetailRepository.findByRecommendationId(id, pageable);
        List<RecommendationDetail> recommendationDetailList = recommendationDetailSlice.getContent();
        ListInfoDto listInfoDto = new ListInfoDto();
        listInfoDto.setSize(recommendationDetailList.size());
        List<SongDto> songDtoList = new ArrayList<>();
        recommendationDetailList.forEach(
                recommendationDetail -> {
                    songDtoList.add(songService.getSong(recommendationDetail.getSong().getId()));
                }
        );
        listInfoDto.setDataList(songDtoList);
        return listInfoDto;
    }

    public List<Song> getRecommendationDetailList(int id) {
        List<Song> recommendationSong = new ArrayList<>();
        List<RecommendationDetail> recommendationDetailList = recommendationDetailRepository.findAllByRecommendationId(id);
        recommendationDetailList.forEach(recommendationDetail -> {
            recommendationSong.add(recommendationDetail.getSong());
        });
        return recommendationSong;
    }

    public String getRecommendationNameById(int id) {
        Recommendation recommendation = recommendationRepository.findById(id).orElse(null);
        if(recommendation == null) {
            throw new EntityNotFoundException("해당되는 AI추천 플레이리스트를 찾을 수 없습니다.");
        }
        return recommendation.getTitle();
    }

    public PlayListDto addPlayListFromRecommendation(String userId, int recommendId) {
        UserEntity user = userRepository.findById(userId);
        String recommend = getRecommendationNameById(recommendId);
        PlayList playList = new PlayList(recommend, user);
        playListRepository.save(playList);
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("recommendationId", (long) recommendId)
                    .addLong("playListId", playList.getId())
                    .addLocalDateTime("runTime", LocalDateTime.now())
                    .toJobParameters();
            jobLauncher.run(recommendationBatchJob,jobParameters);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return PlayListDto.builder()
                .id(playList.getId().intValue())
                .name(playList.getPlayListName()) // name 필드에 playListName 저장
                .build();
    }
}
