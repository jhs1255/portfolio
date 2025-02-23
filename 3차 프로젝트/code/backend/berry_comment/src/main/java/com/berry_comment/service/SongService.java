package com.berry_comment.service;

import com.berry_comment.dto.ListInfoDto;
import com.berry_comment.dto.SongDetailDto;
import com.berry_comment.dto.SongDto;
import com.berry_comment.entity.Song;
import com.berry_comment.repository.AlbumRepository;
import com.berry_comment.repository.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final AlbumService albumService;
    private final LastFmService lastFmService;
    private final AlbumRepository albumRepository;

    //곡이름으로 검색한 결과
    public ListInfoDto getSongList(String track, Pageable pageable) {
        System.out.println("곡을 검색합니다..");
        System.out.println(track);
        //키워드별 검색 음악 목록 가져오기
        Slice<Song> songSlice = songRepository.findByTrackContaining(track, pageable);
        List<SongDto> songDtos = new ArrayList<>();
        for (Song song : songSlice) {
            StringBuilder artist = new StringBuilder();
            for (int i = 0; i < song.getSongOfArtistList().size(); i++) {
                if (i != 0) {
                    artist.append(", ");
                }
                artist.append(song.getSongOfArtistList().get(i).getArtist().getName());
            }
            SongDto songDto = SongDto.builder()
                    .id(song.getId().intValue())
                    .track(song.getTrack())
                    .image(song.getAlbum().getImageUrl())
                    .artist(artist.toString())
                    .playTime(song.getPlayTime())
                    .album(song.getAlbum().getName())
                    .build();
            songDtos.add(songDto);
        }
        return ListInfoDto.builder()
                .size(songDtos.size())
                .dataList(songDtos)
                .build();
    }

    public SongDetailDto getSongDetail(long id) {
        Song song = songRepository.findById(id);
        if(song == null){
            throw new EntityNotFoundException("요청하신 곡의 정보가 없습니다.");
        }
        StringBuilder artist = new StringBuilder();
        for (int i = 0; i < song.getSongOfArtistList().size(); i++) {
            if (i != 0) {
                artist.append(", ");
            }
            artist.append(song.getSongOfArtistList().get(i).getArtist().getName());
        }
        SongDetailDto songDetailDto = SongDetailDto.builder()
                .songId(song.getId().intValue())
                .songName(song.getTrack())
                .songLyrics(song.getLyric())
                .albumName(song.getAlbum().getName())
                .artistName(artist.toString())
                .imageUrl(song.getAlbum().getImageUrl())
                .build();
        return songDetailDto;
    }

    public SongDto getSong(long id) {
        Song song = songRepository.findById(id);
        if(song == null){
            throw new EntityNotFoundException("요청하신 곡의 정보가 없습니다.");
        }
        StringBuilder artist = new StringBuilder();
        System.out.println("아티스트 목록" + song.getSongOfArtistList().toString());
        for (int i = 0; i < song.getSongOfArtistList().size(); i++) {
            if (i != 0) {
                artist.append(", ");
            }
            artist.append(song.getSongOfArtistList().get(i).getArtist().getName());
        }
        return SongDto.builder()
                .id(song.getId().intValue())
                .playTime(song.getPlayTime())
                .track(song.getTrack())
                .image(song.getAlbum().getImageUrl())
                .album(song.getAlbum().getName())
                .artist(artist.toString())
                .build();
    }
}
