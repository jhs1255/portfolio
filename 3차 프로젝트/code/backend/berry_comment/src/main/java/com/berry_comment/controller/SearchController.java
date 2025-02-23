package com.berry_comment.controller;

import com.berry_comment.dto.*;
import com.berry_comment.service.AlbumService;
import com.berry_comment.service.ArtistService;
import com.berry_comment.service.ChartService;
import com.berry_comment.service.SongService;
import com.berry_comment.type.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SongService songService;
    private final ChartService chartService;
    private final AlbumService albumService;
    private final ArtistService artistService;
    @GetMapping("/chart")
    public ResponseEntity<?> getChartList(
            @PageableDefault(
                    page = 0,
                    size = 20,
                    sort = "rank",
                    direction = Sort.Direction.ASC)
            Pageable pageable,
            @RequestParam(required = false,name = "dateTime")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime localDateTime
    ) {
        SongChartDto songChartDto = chartService.getSongChartDtoList(localDateTime, pageable);

        return ResponseEntity.ok(songChartDto);
    }

    //키워드 검색(곡명, 앨범명)
    //값 (=value)
    @GetMapping("/")
    public ResponseEntity<?> getDetail(
            @RequestParam(name = "keyword") SearchType searchType,
            @RequestParam(name = "value") String value,
            @PageableDefault(
                    page = 0,
                    size = 4
            )Pageable pageable
    ){
        ListInfoDto listInfoDto;
        switch (searchType){
            //곡명일경우
            case SearchType.SONG:
                listInfoDto = songService.getSongList(value,pageable);
                break;

            //앨범일경우 작성하기
            case SearchType.ALBUM:
                listInfoDto = albumService.getAlbumListByName(value,pageable);
                break;

            case SearchType.ARTIST:
                listInfoDto = artistService.getInformationArtistByName(value, pageable);
                break;

            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(listInfoDto);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetail(
            @RequestParam(name = "keyword") SearchType keyword,
            @RequestParam(name = "id") int id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size
    ){
        switch (keyword){
            case SONG:
                SongDetailDto songDetailDto = songService.getSongDetail(id);
                return ResponseEntity.ok(songDetailDto);
            case ALBUM:
                AlbumDetailDto albumDetailDto = albumService.getAlbumDetailById(id);
                return ResponseEntity.ok(albumDetailDto);
            case ARTIST:
                ArtistDto artistDto = artistService.getArtistById(id);
                Pageable pageableOfSong = PageRequest.of(page, size);
                Pageable pageableOfAlbum = PageRequest.of(page, size);
                List<SongDto> songDtoList = artistService.getArtistDetailById(id, pageableOfSong);
                List<AlbumDto> albumDtoList = albumService.getAlbumByArtistId(id, pageableOfAlbum);
                ListInfoDto songListInfoDto = ListInfoDto.builder()
                        .size(songDtoList.size())
                        .dataList(songDtoList)
                        .build();
                ListInfoDto albumListInfoDto = ListInfoDto.builder()
                        .size(albumDtoList.size())
                        .dataList(albumDtoList)
                        .build();

                ArtistDetailDto artistDetailDto = ArtistDetailDto.builder()
                        .albumList(albumListInfoDto)
                        .artist(artistDto)
                        .songList(songListInfoDto)
                        .build();
                return ResponseEntity.ok(artistDetailDto);
            default:
                return ResponseEntity.badRequest().build();
        }
    }
}
