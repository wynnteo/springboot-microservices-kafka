package com.wynnteo.ordermgmt.services;

import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.ordermgmt.entity.Order;
import com.wynnteo.ordermgmt.exception.ResourceNotFoundException;
import com.wynnteo.ordermgmt.repository.OrderRepository;
import com.wynnteo.shareddto.OrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusListener {

  @Autowired
  OrderRepository orderRepository;

  @KafkaListener(topics = "order-confirmed", groupId = "order-group")
  public void listenOrderConfirmed(ConsumerRecord<String, OrderEvent> record) {
    OrderEvent orderEvent = record.value();
    System.out.println("Order Consumed: " + orderEvent.getOrderId());
    Order order = orderRepository
      .findById(orderEvent.getOrderId())
      .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    // Update order status to CONFIRMED
    order.setStatus("CONFIRMED");
    orderRepository.save(order);
    // TODO: Notify customer about the order confirmation
    System.out.println("Order confirmed: " + order);
  }

  @KafkaListener(topics = "order-rejected", groupId = "order-group")
  public void listenOrderRejected(ConsumerRecord<String, OrderEvent> record) {
    OrderEvent orderEvent = record.value();
    System.out.println("Order Consumed: " + orderEvent);
    Order order = orderRepository
      .findById(orderEvent.getOrderId())
      .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    // Update order status to REJECTED
    order.setStatus("REJECTED");
    orderRepository.save(order);
    // TODO: Notify customer about the order rejection
    System.out.println("Order rejected: " + order);
  }
}
