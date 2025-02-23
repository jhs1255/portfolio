package com.berry_comment.batch;

import com.berry_comment.entity.UserEntity;
import com.berry_comment.repository.UserRepository;
import com.berry_comment.type.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SubscribeBatch {
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final PlatformTransactionManager transactionManager;

    @Value("${expire-day}")
    private int expireDay;

    @Bean
    public Job subscribeBatchJob() {

        return new JobBuilder("subscribeBatchJob", jobRepository)
                .start(subscribeBatchStep())
                .build();
    }

    @Bean
    public Step subscribeBatchStep() {
        return new StepBuilder("subscribeBatchStep", jobRepository)
                .<UserEntity, UserEntity> chunk(20, transactionManager)
                .reader(userReader())
                .processor(userProcessor())
                .writer(userWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<UserEntity> userReader() {
        return new RepositoryItemReaderBuilder<UserEntity>()
                .name("userReader")
                .pageSize(20)
                .methodName("findLatestPayments")
                .arguments(List.of(LocalDateTime.now().minusDays(expireDay)))
                .repository(userRepository)
                .sorts(Map.of("createdAt", Sort.Direction.DESC))
                .build();

    }

    @Bean
    public ItemProcessor<UserEntity, UserEntity> userProcessor() {
        return user -> {
            //30일이 지났으므로 노말로 다시 설정
            user.setRoleUser(RoleUser.NORMAL);
            return user;
        };
    }

    @Bean
    public ItemWriter<UserEntity> userWriter() {
        return new RepositoryItemWriterBuilder<UserEntity>()
                .repository(userRepository)
                .methodName("save")
                .build();
    }
}
