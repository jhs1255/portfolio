package com.berry_comment.service;

import com.berry_comment.dto.*;
import com.berry_comment.entity.Album;
import com.berry_comment.entity.Artist;
import com.berry_comment.entity.Song;
import com.berry_comment.entity.SongOfArtist;
import com.berry_comment.repository.AlbumRepository;
import com.berry_comment.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public ListInfoDto getAlbumListByName(String albumName, Pageable pageable) {
        Slice<Album> albumSlice = albumRepository.findByNameContaining(albumName, pageable);
        ListInfoDto listInfoDto = new ListInfoDto();
        //배열에 들어갈 타입 지정
        //AlbumDto이다.
        ArrayList<AlbumDto> albumDtos = new ArrayList<>();
        listInfoDto.setDataList(albumDtos);
        listInfoDto.setSize(albumSlice.getNumberOfElements());
        albumSlice.getContent().forEach(album -> {

            Set<String> artists = new HashSet<>();
            album.getSongList().forEach(song -> {
                song.getSongOfArtistList().forEach(artist -> {
                    artists.add(artist.getArtist().getName());
                });
            });

            StringBuilder stringBuilder = new StringBuilder();
            List<String> artistList = artists.stream().toList();
            for(int i = 0; i< artistList.size(); i++){
                if(i != 0){
                    stringBuilder.append(", ");
                }
                stringBuilder.append(artistList.get(i));
            }

            AlbumDto albumDto = AlbumDto.builder()
                    .id(album.getId().intValue())
                    .name(album.getName())
                    .url(album.getImageUrl())
                    .artist(stringBuilder.toString())
                    .build();
            albumDtos.add(albumDto);
        });

        return listInfoDto;
    }

    public AlbumDetailDto getAlbumDetailById(int albumId) {
        Album album = albumRepository.findById((long)albumId).orElse(null);
        if(album == null){
            throw new EntityNotFoundException("요청하신 앨범의 정보가 없습니다.");
        }
        int id = album.getId().intValue();
        String albumName = album.getName();
        List<Song> songList = album.getSongList();
        List<SongDto> songDtoList = new ArrayList<>();
        Set<String> artistNameSet = new HashSet<>();
        songList.forEach(song -> {
            SongDto songDto = new SongDto();
            songDto.setId(song.getId().intValue());
            songDto.setTrack(song.getTrack());
            songDto.setPlayTime(song.getPlayTime());
            songDto.setImage(album.getImageUrl());
            StringBuilder stringBuilder = new StringBuilder();
            List<SongOfArtist> songOfArtistList = song.getSongOfArtistList();
            for(int i = 0; i< songOfArtistList.size(); i++){
                if(i != 0){
                    stringBuilder.append(", ");
                }
                stringBuilder.append(songOfArtistList.get(i).getArtist().getName());
                artistNameSet.add(songOfArtistList.get(i).getArtist().getName());
            }
            songDto.setArtist(stringBuilder.toString());
            songDtoList.add(songDto);
        });
        StringBuilder artistName = new StringBuilder();
        List<String> artistList = artistNameSet.stream().sorted().toList();
        for(int i = 0; i< artistList.size(); i++){
            if(i != 0){
                artistName.append(", ");
            }
            artistName.append(artistList.get(i));
        }
        ListInfoDto listInfoDto = ListInfoDto.builder()
                .size(songDtoList.size())
                .dataList(songDtoList)
                .build();

        return AlbumDetailDto.builder()
                .artist(artistName.toString())
                .id(id)
                .album(albumName)
                .listInfo(listInfoDto)
                .build();
    }

    public List<AlbumDto> getAlbumByArtistId(int artistId, Pageable pageable) {
        System.out.println("앨범 찾기..");
        Page<Album> albumPage = albumRepository.findAlbumsBySongOfArtistId((long)artistId, pageable);
        List<AlbumDto> albumDtoList = new ArrayList<>();
        System.out.println("찾은 앨범 수"+albumPage.getTotalElements());
        albumPage.getContent().forEach(album -> {
            System.out.println(album.getName());
           AlbumDto albumDto = AlbumDto.builder()
                   .id(album.getId().intValue())
                   .name(album.getName())
                   .url(album.getImageUrl())
                   .artist("")
                   .build();
           albumDtoList.add(albumDto);
        });
        return albumDtoList;
    }
}
