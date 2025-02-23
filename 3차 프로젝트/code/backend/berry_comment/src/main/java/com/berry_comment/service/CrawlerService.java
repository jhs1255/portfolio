package com.berry_comment.service;

import com.berry_comment.entity.*;
import com.berry_comment.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final LastFmService lastFmService;
    private final SongOfArtistRepository songOfArtistRepository;
    //TODO
    /*
        매 시간 마다 Top100 스케줄러를 실행
        음악 정보를 들고 온다
       1. 음악 테이블 --> 그 다음 이미 존재하는 음악이라면 내용을 저장하지 않고
            존재하지 않는 음악이라면 넣기

       2. 앨범테이블 --> 이미 존재하는 앨범이면 넣지 않기

       3. 차트 정보에 정보 넣어주기

       4. 아티스트 정보 갱신하기
    * */
    private final ArtistRepository artistRepository;
    private final ChartRepository chartRepository;
    private final ChartDetailRepository chartDetailRepository;
    private List<Integer> songLists = new ArrayList<>();

    //크롤링 정보 수정
    public void crawl(LocalDateTime localDateTime) {
        songLists = new ArrayList<>();
        String queryParamTime = String.format("%s%s%s%s",localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour());
        int queryParamTimeInt = Integer.parseInt(queryParamTime);
        //모든 정보를 크롤링합니다.
        //크롤링할 url
        System.out.println("test실행..");
        String url = "https://www.melon.com/chart/index.htm?dayTime="+queryParamTimeInt;
        try {
            Document document = Jsoup.connect(url).get();

            // table body xPath = //*[@id="frm"]/div/table/tbody
            Elements tBodyElements = document.selectXpath("//*[@id=\"frm\"]/div/table/tbody");
            Element tBodyElement = tBodyElements.get(0);  // getFirst() 대신 get(0) 사용
            Elements rankElements = tBodyElement.getElementsByTag("tr");
            AtomicInteger rank = new AtomicInteger(1);
            //차트 저장
            Chart chart = new Chart(localDateTime);
            chartRepository.save(chart);

            for (Element element : rankElements) {
                int dataSongId = Integer.parseInt(element.selectFirst("tr").attr("data-song-no"));
                ArrayList<Integer> artistIds = new ArrayList<>();

                // 아티스트 element
                Element rank02Element = element.selectFirst(".rank02").selectFirst(".checkEllipsis");
                Elements artistHref = rank02Element.getElementsByTag("a");

                for (Element artist : artistHref) {  // forEach를 for문으로 교체
                    String href = artist.attr("href");
                    Pattern pattern = Pattern.compile("goArtistDetail\\('(\\d+)'\\)");
                    Matcher matcher = pattern.matcher(href);
                    if (matcher.find()) {
                        String artistId = matcher.group(1); // 매칭된 그룹(앨범 ID)
                        System.out.println("아티스트 아이디: " + artistId);
                        artistIds.add(Integer.parseInt(artistId));
                    } else {
                        System.out.println("앨범 ID를 찾을 수 없습니다.");
                    }
                }

                // 앨범 저장
                int albumId = 0;
                Element rank03Element = element.selectFirst(".rank03").selectFirst("a");
                Pattern albumPattern = Pattern.compile("goAlbumDetail\\('(\\d+)'\\)");
                String href = rank03Element.attr("href");
                Matcher matcher = albumPattern.matcher(href);
                if (matcher.find()) {
                    albumId = Integer.parseInt(matcher.group(1));
                }
                saveAlbumById(albumId);
                //아티스트 저장하기
                for (int artistId : artistIds) {  // forEach를 for문으로 교체
                    Artist artist = saveArtistByIdAndName(artistId);
                }
                // 음악 저장하기
                Song song = saveSongById(dataSongId);

                
                ChartDetail chartDetail = new ChartDetail(chart,song, rank.getAndIncrement());
                chartDetailRepository.save(chartDetail);

            }
            songLists.forEach(this::saveSongById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void setAlbumOfSong(int songId) {
        String url = "https://www.melon.com/song/detail.htm?songId=" + songId;

    }

    public SongOfArtist saveSongOfArtist(Song song, Artist artist) {
        SongOfArtist songOfArtist = new SongOfArtist(artist, song);
        songOfArtistRepository.save(songOfArtist);
        return songOfArtist;
    }


    public Artist saveArtistByIdAndName(int id) {
        //만약 아티스트가 존재한다면
        Optional<Artist> artist = artistRepository.findById((long)id);
        if(artist.isEmpty()){

            String url = "https://www.melon.com/artist/timeline.htm?artistId=" + id;
            try {
                Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36").get();
                String imageUrl = document.getElementById("artistImgArea").getElementsByTag("img").getFirst().attr("src");
                System.out.println("아티스트 이미지"+imageUrl);
                String artistName = document.selectFirst("p.title_atist").ownText();
                Artist newArtist = new Artist((long)id, artistName, imageUrl);
                newArtist = artistRepository.save(newArtist);
                return newArtist;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            return artist.get();
        }
        return null;
    }

    public Album saveAlbumById(int albumId){
        Optional<Album> returnValue = albumRepository.findById((long) albumId);
        if(returnValue.isPresent()){
            return returnValue.get();
        }
        String url = "https://www.melon.com/album/detail.htm?albumId=" + albumId;
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36").get();
            Element tBodyElement = document.getElementsByTag("tbody").first();
            Elements tBodyElements = tBodyElement.getElementsByTag("tr");

            tBodyElements.stream().forEach(element -> {
                try {
                    Element songElementTd = element.getElementsByClass("wrap_song_info").getFirst().getElementsByClass("ellipsis").getFirst().getElementsByTag("span").getFirst(); //인덱스 outofError고치기..
                    if(songElementTd.selectFirst("a")!=null) {
                        String href = songElementTd.selectFirst("a").attr("href");
                        Pattern pattern = Pattern.compile("\\d+"); // 숫자(0~9)를 추출
                        Matcher matcher = pattern.matcher(href);
                        int cnt = 0;
                        while (matcher.find()) {
                            cnt++;
                            if (cnt == 2) {
                                String number = matcher.group();
                                System.out.println("추출된 숫자: " + number);
                                //음악 저장 진행...로직 짜기!!
                                songLists.add(Integer.parseInt(number));
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            String albumName = document.selectXpath("//*[@id=\"conts\"]/div[2]/div/div[2]/div[1]/div[1]").first().ownText();
            String albumImageUrl = document.getElementById("d_album_org").selectFirst("img").attr("src");
            Album newAlbum = new Album((long)albumId, albumImageUrl,albumName);
            albumRepository.save(newAlbum);
            return newAlbum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Song saveSongById(int songId) {
        Song song = songRepository.findById((long) songId);
        if(song != null){
            return song;
        }
        String url = "https://www.melon.com/song/detail.htm?songId=" + songId;
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36")
                    .get();
            String track = document.selectXpath("//*[@id=\"downloadfrm\"]/div/div/div[2]/div[1]/div[1]").first().ownText();
            int playTime = 0;
            int albumId = 0;
            String genre;
            String musicUrl = "";
            String lyric = "";
            //앨범 아이디 가져오기
            Element albumIdElement = document.selectXpath("//*[@id=\"downloadfrm\"]/div/div/div[2]/div[2]/dl/dd[1]/a").first();
            String href = albumIdElement.selectFirst("a").attr("href");
            Pattern pattern = Pattern.compile("'(\\d+)'"); // 숫자(0~9)를 추출
            Matcher matcher = pattern.matcher(href);
            if (matcher.find()) {
                albumId = Integer.parseInt(matcher.group(1));
            }
            
            Album album = saveAlbumById(albumId);
            Elements elements = document.getElementsByTag("dd");
            genre = elements.get(2).text();
            System.out.println("장르 "+ genre);
            Element artistNameElement = document.selectXpath("//*[@id=\"downloadfrm\"]/div/div/div[2]/div[1]/div[2]").first();
            Elements artistLinks = artistNameElement.select("a.artist_name");
            Elements artistIdHref = artistNameElement.getElementsByTag("a");
            for (Element artistLink : artistLinks) {
                JsonNode jsonNode = lastFmService.getSongPlayTime(track, artistLink.attr("title"));
                if(jsonNode.has("error")){
                    continue;
                }
                playTime = jsonNode.get("track").get("duration").asInt();
                musicUrl = getSongUrl(jsonNode.get("track").get("url").asText());
            }
            //가사 가져오기
            //널값 오류 해결..
            Element lyricElement = document.getElementById("d_video_summary");
            lyric = (lyricElement != null) ? lyricElement.html() : "";
            System.out.println("가사 "+lyric);
            song = new Song((long)songId, track, playTime, album, genre, musicUrl, lyric);
            songRepository.save(song);
            Song finalSong = song;
            artistIdHref.forEach(element -> {
                Matcher matcher2 = pattern.matcher(element.attr("href"));
                if (matcher2.find()) {
                    Optional<Artist> artist = artistRepository.findById((long)Integer.parseInt(matcher2.group(1)));
                    if(artist.isPresent()){
                        saveSongOfArtist(finalSong,artist.get());
                    }
                    else {
                        Artist newArtist = saveArtistByIdAndName(Integer.parseInt(matcher2.group(1)));
                    }
                }
            });
            }
        catch (Exception e) {
            e.printStackTrace();
        }
        return song;
    }

    public String getSongUrl(String url) {
        String youtubeUrl = "";
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36").get();
            Element link = document.selectFirst("a.image-overlay-playlink-link");
            if (link != null) {
                String href = link.attr("href");
                youtubeUrl = href;
            } else {
                youtubeUrl = "";
            }
        } catch (IOException e) {
            return "";
        }
        return youtubeUrl;
    }

}
