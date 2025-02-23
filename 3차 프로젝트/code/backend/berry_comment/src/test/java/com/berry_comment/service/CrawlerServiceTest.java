package com.berry_comment.service;

import com.berry_comment.entity.Song;
import com.berry_comment.repository.SongRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class CrawlerServiceTest {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private SongRepository songRepository;

    //크롤링 정보 테스트
    @Test
    void testCrawlerService() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String queryParamTime = String.format("%s%s%s%s",localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour());
        int queryParamTimeInt = Integer.parseInt(queryParamTime);
        //모든 정보를 크롤링합니다.
        //크롤링할 url
        System.out.println("test실행..");
        String url = "https://www.melon.com/chart/index.htm?dayTime="+queryParamTimeInt;
        try {
            //rank XPath = //*[@id="lst50"]/td[2]/div/span[1], //*[@id="lst50"]/td[2]/div/span[1]
            Document document = Jsoup.connect(url).get();
            //table body xPath = //*[@id="frm"]/div/table/tbody
            Elements tBodyElements = document.selectXpath("//*[@id=\"frm\"]/div/table/tbody");
            Element tBodyElement = tBodyElements.getFirst();
            Elements rankElements = tBodyElement.getElementsByTag("tr");
            rankElements.stream().forEach(element -> {
                //곡 ID
                int dataSongId = Integer.parseInt(element.selectFirst("tr").attr("data-song-no"));

                //곡 이름
                String songTitle = element.selectFirst("a[title*='재생']").text();

                //아티스트 이름
                StringBuilder artist = new StringBuilder();

                Elements artistElements = element.select("div.ellipsis.rank02 a");
                System.out.println("아티스트 수" + artistElements.size());
                for(int i = 0;i < artistElements.size();i++) {
                    Element artistElement = artistElements.get(i);
                    //중복된 값이 있으면 빼기
                    if(artist.toString().contains(artistElement.text())) continue;
                    //만약 첫번째가 아니라면 ,추가
                    if(i != 0) artist.append(", ");
                    artist.append(artistElement.text());
                }

                //5앨범 제목 추출
                // 5. 앨범 제목 추출
                String albumTitle = element.selectFirst("div.ellipsis.rank03 a").text();
                String imageUrl = element.selectFirst("img").attr("src");

                System.out.printf("곡 아이디: %d 곡 이름: %s 아티스트: %s 앨범: %s 이미지 url: %s\n",dataSongId,songTitle,artist.toString(),albumTitle,imageUrl);

            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //실제로 잘 작동되는가 확인하고 있습니다.
    @Test
    public void testSchedule() throws InterruptedException {
        //5초마다 크롤링 잘되는가 확인하기

            LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
            crawlerService.crawl(localDateTime);

    }

    @Test
    public void getSong() {
        Song song = songRepository.findById(38048464);
        System.out.println("곡이름" + song.getTrack());
    }
}