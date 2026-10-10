package org.example.spring.lab.order;

import lombok.RequiredArgsConstructor;
import org.example.spring.lab.order.model.OrdersDto;
import org.example.spring.lab.user.model.AuthUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;


    @PostMapping("/user/order")
    public ResponseEntity order(@AuthenticationPrincipal AuthUserDetails authUserDetails,
                                @RequestBody OrdersDto.OrderReq req) {

        Long userIdx = authUserDetails.getIdx();

        ordersService.order(userIdx, req);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
