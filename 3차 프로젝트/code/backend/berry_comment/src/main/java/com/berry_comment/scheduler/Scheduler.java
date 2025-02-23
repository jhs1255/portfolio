package com.berry_comment.scheduler;

import com.berry_comment.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private final CrawlerService crawlerService;
    private final JobLauncher jobLauncher;
    private final Job subscribeBatchJob;

    @Scheduled(cron = "0 0 * * * ?")
    public void crawlerScheduler() {
        System.out.println("CrawlerScheduler.crawlerScheduler() : 스케줄링을 실행합니다.");
        //실행되는 시점에 크롤링하기
        crawlerService.crawl(LocalDateTime.now());
    }
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void subscribeBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("date", LocalDateTime.now().toString())
                    .toJobParameters();
            jobLauncher.run(subscribeBatchJob, jobParameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
