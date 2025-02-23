package com.berry_comment.service;

import com.berry_comment.dto.SongChartDto;
import com.berry_comment.dto.SongRankDto;
import com.berry_comment.entity.*;
import com.berry_comment.exception.BerryCommentException;
import com.berry_comment.exception.ErrorCode;
import com.berry_comment.repository.ChartDetailRepository;
import com.berry_comment.repository.ChartRepository;
import com.berry_comment.repository.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final ChartRepository chartRepository;
    private final ChartDetailRepository chartDetailRepository;
    private final SongRepository songRepository;

    @Transactional
    public SongChartDto getSongChartDtoList(LocalDateTime localDateTime, Pageable pageable) {
        if(localDateTime.isAfter(LocalDateTime.now())) {
            //현재 시간대보다 이른 시간을 요청하면
            //오류 발생
            throw new BerryCommentException(ErrorCode.INVALID_TIME_REQUEST);
        }

        String formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));

        Chart chart = chartRepository.findByLocalDateTime(formattedDateTime);
        if(chart == null) {
            throw new EntityNotFoundException("차트를 찾기 못했습니다.");
        }

        //차트 정보가져오기
        Slice<ChartDetail> chartDetailSlice = chartDetailRepository.findByChartId(chart.getId(),pageable);

        SongChartDto songChartDto = new SongChartDto();
        songChartDto.setSong(new ArrayList<>());

        //리스트 사이즈 크기 지정
        songChartDto.setSize(chartDetailSlice.getSize());
        chartDetailSlice.stream().forEach(chartDetail -> {
            Song song = chartDetail.getSong();
            Album album = song.getAlbum();
            List<SongOfArtist> songOfArtists = song.getSongOfArtistList();
            StringBuilder artists = new StringBuilder();
            for (int i = 0; i < songOfArtists.size(); i++) {
                if( i > 0)
                    artists.append(",");
                artists.append(songOfArtists.get(i).getArtist().getName());
            }

            SongRankDto songRankDto = SongRankDto.builder()
                    .rank(chartDetail.getRank())
                    .id(song.getId().intValue())
                    .track(song.getTrack())
                    .artist(artists.toString())
                    .image(album.getImageUrl())
                    .playTime(song.getPlayTime())
                    .album(album.getName())
                    .build();
            songChartDto.getSong().add(songRankDto);
        });
        return songChartDto;
    }
}