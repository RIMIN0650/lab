package org.example.spring.lab.batch.config;

import jakarta.persistence.EntityManagerFactory;
import org.example.spring.lab.batch.dto.OrderSummary;
import org.example.spring.lab.settlement.model.Settlement;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class SettlementJobConfig {

    // 지난 달 사용자 별 주문 금액 조회
    @Bean
    @StepScope
    public JpaPagingItemReader<OrderSummary> settlementReader(EntityManagerFactory entityManagerFactory) {
        LocalDate thisMonth = LocalDate.now()
                .withDayOfMonth(1);

        LocalDate lastMonth = thisMonth.minusMonths(1);

        LocalDateTime startDate = lastMonth.atStartOfDay();
        LocalDateTime endDate = thisMonth.atStartOfDay();

        String settlementMonth = lastMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        return new JpaPagingItemReaderBuilder<OrderSummary>()
                .name("settlementReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                        SELECT new org.example.spring.lab.batch.dto.OrderSummary(
                            o.userIdx,
                            SUM(o.price)
                        )
                        FROM Orders o
                        WHERE o.orderTime >= :startDate
                            AND o.orderTime < :endDate
                            AND NOT EXISTS (
                                SELECT s.idx
                                FROM Settlement s
                                WHERE s.userIdx = o.userIdx
                                    AND s.settlementMonth = :settlementMonth)
                        GROUP BY o.userIdx
                        ORDER BY o.userIdx
                        """)
                .parameterValues(Map.of("startDate", startDate,
                                        "endDate", endDate,
                                        "settlementMonth", settlementMonth))
                .pageSize(100)
                .build();
    }

    // 집계 결과를 Settlement Entity로 변환
    @Bean
    @StepScope
    public ItemProcessor<OrderSummary, Settlement> settlementProcessor() {
        return summary -> {
            LocalDate lastMonth = LocalDate.now()
                    .withDayOfMonth(1)
                    .minusMonths(1);

            String settlementMonth = lastMonth.format(
                    DateTimeFormatter.ofPattern("yyyy-MM")
            );

            return Settlement.builder()
                    .userIdx(summary.userIdx())
                    .settlementMonth(settlementMonth)
                    .totalAmount(summary.totalAmount())
                    .createdAt(LocalDateTime.now())
                    .build();
        };
    }

    // 정산 결과를 DB에 저장
    @Bean
    public JpaItemWriter<Settlement> settlementWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Settlement>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


    @Bean
    public Step settlementStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<OrderSummary> settlementReader,
            ItemProcessor<OrderSummary, Settlement> settlementProcessor,
            JpaItemWriter<Settlement> settlementWriter
    ) {
        return new StepBuilder("settlemenmtStep", jobRepository)
                .<OrderSummary, Settlement> chunk(
                        100, transactionManager
                )
                .reader(settlementReader)
                .processor(settlementProcessor)
                .writer(settlementWriter)
                .build();
    }

    @Bean
    public Job settlementJob(JobRepository jobRepository, Step settlementStep) {
        return new JobBuilder("SettlementJob", jobRepository)
                .start(settlementStep)
                .build();
    }



}
