package com.wynnteo.ordermgmt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.ordermgmt.dto.ProductDto;
import com.wynnteo.ordermgmt.entity.Order;
import com.wynnteo.ordermgmt.exception.ResourceNotFoundException;
import com.wynnteo.ordermgmt.feignclient.ProductClient;
import com.wynnteo.ordermgmt.repository.OrderRepository;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  private ProductClient productClient;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private OrderProducerService orderProducerService;

  private static final Logger logger = LoggerFactory.getLogger(
    OrderServiceImpl.class
  );

  @Override
  public OrderDto createOrder(OrderDto orderDto) {
    logger.info("Creating order: {}", orderDto);

    ModelMapper modelMapper = new ModelMapper();
    modelMapper
      .getConfiguration()
      .setMatchingStrategy(MatchingStrategies.STRICT);

    Order savedOrder = orderRepository.save(
      modelMapper.map(orderDto, Order.class)
    );
    orderDto.setId(savedOrder.getId());
    orderProducerService.sendOrder(orderDto);
    return modelMapper.map(savedOrder, OrderDto.class);
  }

  @Override
  public List<OrderDto> getAllOrders() {
    logger.info("Fetching all orders");
    List<Order> orderList = (List<Order>) orderRepository.findAll();

    Type listType = new TypeToken<List<OrderDto>>() {}.getType();

    return new ModelMapper().map(orderList, listType);
  }

  @Override
  public OrderDto getOrderById(Long id) {
    logger.info("Fetching order by ID: {}", id);
    Order order = orderRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    OrderDto orderResponse = new ModelMapper().map(order, OrderDto.class);
    ResponseEntity<Map<String, Object>> response = productClient.getProductById(
      order.getProductId()
    );
    if (response != null && response.getBody() != null) {
      ProductDto productDto = objectMapper.convertValue(
        response.getBody().get("data"),
        ProductDto.class
      );
      orderResponse.setProductDto(productDto);
    }

    return orderResponse;
  }

  @Override
  public OrderDto updateOrder(Long id, OrderDto orderDetails) {
    logger.info("Updating order with ID: {}", id);

    ModelMapper modelMapper = new ModelMapper();
    modelMapper
      .getConfiguration()
      .setMatchingStrategy(MatchingStrategies.STRICT);

    Order order = orderRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    order.setStatus(orderDetails.getStatus());
    orderRepository.save(order);

    return modelMapper.map(order, OrderDto.class);
  }

  @Override
  public void deleteOrder(Long id) {
    logger.info("Deleting order with ID: {}", id);
    Order order = orderRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    orderRepository.delete(order);
  }
}
