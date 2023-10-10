package com.wynnteo.ordermgmt.services;

import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.shareddto.OrderEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

  @Autowired
  private KafkaTemplate<String, OrderEvent> kafkaTemplate;

  public void sendOrder(@Valid OrderDto order) {
    OrderEvent orderEvent = new OrderEvent();
    orderEvent.setOrderId(order.getId());
    orderEvent.setProductId(order.getProductId());
    orderEvent.setQuantity(order.getQuantity());
    kafkaTemplate.send("order-update-topic", orderEvent);
  }
}
