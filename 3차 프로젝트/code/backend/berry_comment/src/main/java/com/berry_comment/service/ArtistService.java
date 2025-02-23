package com.berry_comment.service;

import com.berry_comment.dto.ArtistDto;
import com.berry_comment.dto.ArtistSearchAnswerDto;
import com.berry_comment.dto.ListInfoDto;
import com.berry_comment.dto.SongDto;
import com.berry_comment.entity.Artist;
import com.berry_comment.entity.Song;
import com.berry_comment.entity.SongOfArtist;
import com.berry_comment.repository.ArtistRepository;
import com.berry_comment.repository.SongOfArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final SongOfArtistRepository songOfArtistRepository;

    public ListInfoDto getInformationArtistByName(String artistName, Pageable pageable) {
        ListInfoDto listInfoDto = new ListInfoDto();
        Slice<Artist> artists = artistRepository.findByNameContaining(artistName, pageable);
        List<ArtistSearchAnswerDto> answerDtoList = new ArrayList<>();
        listInfoDto.setDataList(answerDtoList);
        listInfoDto.setSize(artists.getNumberOfElements());
        artists.forEach(artist -> {
            System.out.println(artist.toString());
            Set<String> genreSet = new HashSet<>();
            artist.getSongs().forEach(song -> {
               String value = song.getSong().getGenre();
               genreSet.add(value);
            });
            ArtistSearchAnswerDto artistSearchAnswerDto = ArtistSearchAnswerDto.builder()
                    .id(artist.getId().intValue())
                    .name(artist.getName())
                    .genre(genreSet.stream().toList())
                    .image(artist.getImage())
                    .build();
            answerDtoList.add(artistSearchAnswerDto);
        });
        return listInfoDto;
    }

    public List<SongDto> getArtistDetailById(long id, Pageable pageableOfSong){
        Artist artist = artistRepository.findById(id).orElse(null);
        if(artist == null){
            throw new EntityNotFoundException("해당하는 아티스트가 없습니다.");
        }
        List<SongDto> songDtoList = new ArrayList<>();
        Page<SongOfArtist> songOfArtistPage = songOfArtistRepository.findByArtistId(artist.getId(), pageableOfSong);
        songOfArtistPage.forEach(songOfArtist -> {
            Song song = songOfArtist.getSong();
            SongDto songDto = SongDto.builder()
                    .id(song.getId().intValue())
                    .artist(artist.getName())
                    .track(song.getTrack())
                    .image(song.getAlbum().getImageUrl())
                    .playTime(song.getPlayTime())
                    .album(song.getAlbum().getName())
                    .build();
            songDtoList.add(songDto);
        });

        return songDtoList;
    }

    public ArtistDto getArtistById(long id) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if(artist == null){
            throw new EntityNotFoundException("해당하는 아티스트가 없습니다.");
        }
        return ArtistDto.builder()
                .id(artist.getId().intValue())
                .artistName(artist.getName())
                .imageUrl(artist.getImage())
                .build();
    }
}
