package com.wynnteo.ordermgmt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.shareddto.OrderEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class OrderProducerServiceTest {

  @InjectMocks
  private OrderProducerService orderProducerService;

  @Mock
  private KafkaTemplate<String, OrderEvent> kafkaTemplate;

  @Captor
  private ArgumentCaptor<OrderEvent> orderEventCaptor;

  @Test
  public void testSendOrder() {
    // Create a sample order
    OrderDto order = new OrderDto();
    order.setId(123L);
    order.setProductId(456L);
    order.setQuantity(10);

    // Call the method being tested
    orderProducerService.sendOrder(order);

    // Verify that kafkaTemplate.send was called with the expected parameters
    Mockito
      .verify(kafkaTemplate)
      .send(Mockito.eq("order-update-topic"), orderEventCaptor.capture());

    OrderEvent capturedOrderEvent = orderEventCaptor.getValue();

    // Create an expected OrderEvent
    OrderEvent expectedEvent = new OrderEvent();
    expectedEvent.setOrderId(123L);
    expectedEvent.setProductId(456L);
    expectedEvent.setQuantity(10);

    assertEquals(
      expectedEvent.getProductId(),
      capturedOrderEvent.getProductId()
    );
    assertEquals(expectedEvent.getQuantity(), capturedOrderEvent.getQuantity());
  }
}
