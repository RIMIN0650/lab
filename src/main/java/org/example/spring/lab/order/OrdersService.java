package org.example.spring.lab.order;

import lombok.RequiredArgsConstructor;
import org.example.spring.lab.order.model.Orders;
import org.example.spring.lab.order.model.OrdersDto;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository orderRepository;

    public void order(Long userIdx, OrdersDto.OrderReq req) {
        Orders orders = req.toEntity(userIdx);
        orderRepository.save(orders);
    }
}
