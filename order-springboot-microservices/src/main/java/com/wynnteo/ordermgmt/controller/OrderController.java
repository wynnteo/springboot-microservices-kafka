package com.wynnteo.ordermgmt.controller;

import static com.wynnteo.ordermgmt.constants.Messages.*;

import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.ordermgmt.exception.ResourceNotFoundException;
import com.wynnteo.ordermgmt.services.OrderProducerService;
import com.wynnteo.ordermgmt.services.OrderService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  OrderService orderService;

  @Autowired
  private OrderProducerService orderProducerService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAllOrders() {
    List<OrderDto> orders = orderService.getAllOrders();
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("statusCode", HttpStatus.OK.value());
    response.put("message", ORDERS_RETRIEVED_SUCCESS);
    response.put("data", orders);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getOrderById(
    @PathVariable Long id
  ) {
    try {
      OrderDto order = orderService.getOrderById(id);
      Map<String, Object> response = new HashMap<>();
      response.put("status", "success");
      response.put("statusCode", HttpStatus.OK.value());
      response.put("message", ORDERS_RETRIEVED_SUCCESS);
      response.put("data", order);
      return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException e) {
      Map<String, Object> response = new HashMap<>();
      response.put("status", "error");
      response.put("statusCode", HttpStatus.NOT_FOUND.value());
      response.put("message", ORDER_NOT_FOUND);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<Map<String, Object>> createOrder(
    @Valid @RequestBody OrderDto order
  ) {
    OrderDto createdOrder = orderService.createOrder(order);
    orderProducerService.sendOrder(order);
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("statusCode", HttpStatus.CREATED.value());
    response.put("message", ORDER_CREATED_SUCCESS);
    response.put("data", createdOrder);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> updateOrder(
    @PathVariable Long id,
    @Valid @RequestBody OrderDto orderDetails
  ) {
    try {
      OrderDto updatedOrder = orderService.updateOrder(id, orderDetails);
      Map<String, Object> response = new HashMap<>();
      response.put("status", "success");
      response.put("statusCode", HttpStatus.OK.value());
      response.put("message", ORDER_UPDATED_SUCCESS);
      response.put("data", updatedOrder);
      return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException e) {
      Map<String, Object> response = new HashMap<>();
      response.put("status", "error");
      response.put("statusCode", HttpStatus.NOT_FOUND.value());
      response.put("message", ORDER_NOT_FOUND);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> deleteOrder(
    @PathVariable Long id
  ) {
    try {
      orderService.deleteOrder(id);
      Map<String, Object> response = new HashMap<>();
      response.put("status", "success");
      response.put("statusCode", HttpStatus.NO_CONTENT.value());
      response.put("message", ORDER_DELETED_SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    } catch (ResourceNotFoundException e) {
      Map<String, Object> response = new HashMap<>();
      response.put("status", "error");
      response.put("statusCode", HttpStatus.NOT_FOUND.value());
      response.put("message", ORDER_NOT_FOUND);
      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
  }
}
