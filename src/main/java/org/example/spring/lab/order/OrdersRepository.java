package org.example.spring.lab.order;

import org.example.spring.lab.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
