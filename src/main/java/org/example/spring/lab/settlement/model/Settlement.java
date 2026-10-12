package org.example.spring.lab.settlement.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_settlement_user_month",
                columnNames = {"userIdx", "settlementMonth"}
        )
})
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private Long userIdx;

    private String settlementMonth;

    private Long totalAmount;

    private LocalDateTime createdAt;

    @Builder
    public Settlement(Long userIdx, String settlementMonth, Long totalAmount, LocalDateTime createdAt) {
        this.userIdx = userIdx;
        this.settlementMonth = settlementMonth;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

}
