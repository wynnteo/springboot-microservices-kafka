package com.wynnteo.productmgmt.services;

import com.wynnteo.productmgmt.dto.ProductDto;
import com.wynnteo.productmgmt.entity.Product;
import com.wynnteo.productmgmt.exception.ResourceNotFoundException;
import com.wynnteo.productmgmt.repository.ProductRepository;
import com.wynnteo.shareddto.OrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockConsumerService {

  @Autowired
  private KafkaTemplate<String, OrderEvent> kafkaTemplate;

  @Autowired
  ProductRepository productRepository;

  @KafkaListener(topics = "order-requested", groupId = "order-group")
  public void listenOrderRequested(ConsumerRecord<String, OrderEvent> record) {
    OrderEvent orderEvent = record.value();

    Product product = productRepository
      .findById(orderEvent.getProductId())
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    if (product.getStock() >= orderEvent.getQuantity()) {
      product.setStock(product.getStock() - orderEvent.getQuantity());
      productRepository.save(product);
      // Send order-confirmed message
      orderEvent.setStatus("CONFIRMED");
      kafkaTemplate.send("order-confirmed", orderEvent);
    } else {
      // Send order-rejected message
      orderEvent.setStatus("REJECTED");
      kafkaTemplate.send("order-rejected", orderEvent);
    }
  }
}
