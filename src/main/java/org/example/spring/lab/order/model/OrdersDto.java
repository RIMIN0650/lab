package org.example.spring.lab.order.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class OrdersDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderReq {
        private int price;

        public Orders toEntity(Long userIdx) {
            return Orders.builder()
                    .userIdx(userIdx)
                    .price(this.price)
                    .orderTime(LocalDateTime.now())
                    .build();
        }
    }

}
