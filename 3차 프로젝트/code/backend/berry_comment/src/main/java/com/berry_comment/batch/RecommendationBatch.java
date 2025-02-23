package com.berry_comment.batch;

import com.berry_comment.entity.PlayList;
import com.berry_comment.entity.PlayListDetail;
import com.berry_comment.entity.Song;
import com.berry_comment.repository.PlayListDetailRepository;
import com.berry_comment.repository.PlayListRepository;
import com.berry_comment.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class RecommendationBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PlayListDetailRepository playListDetailRepository;
    private final PlayListRepository playListRepository;
    private final SongRepository songRepository;
    @Bean
    public Job recommendationBatchJob() {
        return new JobBuilder("recommendationBatchJob", jobRepository)
                .start(recommendationBatchStep())
                .build();
    }

    @Bean
    public Step recommendationBatchStep() {
        return new StepBuilder("recommendationBatchStep", jobRepository)
                .<Song, PlayListDetail>chunk(20, transactionManager)
                .reader(itemReader(null))
                .processor(recommendationBatchProcessor(null)) // null을 주지 말고 실제 값이 전달될 수 있도록 수정
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Song> itemReader(@Value("#{jobParameters['recommendationId']}") Long recommendationId) {
        return new RepositoryItemReaderBuilder<Song>()
                .name("itemReader")
                .pageSize(10)
                .methodName("findByRecommendation")
                .arguments(Collections.singletonList(recommendationId))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .repository(songRepository)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Song, PlayListDetail> recommendationBatchProcessor(@Value("#{jobParameters['playListId']}") Long playlistId) {
        return song -> {
            PlayList playList = playListRepository.findById(playlistId)
                    .orElseThrow(() -> new IllegalArgumentException("PlayList not found: " + playlistId));
            return new PlayListDetail(song, playList);
        };
    }

    @Bean
    public RepositoryItemWriter<PlayListDetail> writer() {
        return new RepositoryItemWriterBuilder<PlayListDetail>()
                .repository(playListDetailRepository)
                .methodName("save")
                .build();
    }
    
}

