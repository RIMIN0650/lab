package org.example.spring.lab.order;

import org.example.spring.lab.batch.dto.OrderSummary;
import org.example.spring.lab.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {


    @Query("""
        SELECT new org.example.spring.lab.batch.dto.OrderSummary(o.userIdx, SUM(o.price))
        FROM Orders o
        WHERE o.orderTime >= :startDate AND o.orderTime < :endDate
        GROUP BY o.userIdx
        ORDER BY o.userIdx
    """)
    List<OrderSummary> sumOrdersByUserAndPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
