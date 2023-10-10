package com.wynnteo.ordermgmt.services;

import java.util.List;
import com.wynnteo.ordermgmt.dto.OrderDto;

public interface OrderService {

    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto createOrder(OrderDto order);
    OrderDto updateOrder(Long id, OrderDto orderDetails);
    void deleteOrder(Long id);
}