package com.wynnteo.ordermgmt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.ordermgmt.dto.ProductDto;
import com.wynnteo.ordermgmt.entity.Order;
import com.wynnteo.ordermgmt.exception.ResourceNotFoundException;
import com.wynnteo.ordermgmt.feignclient.ProductClient;
import com.wynnteo.ordermgmt.repository.OrderRepository;

@SpringBootTest
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;

    private OrderDto orderDto;
    private Long orderID = 1L;

    @BeforeEach
    public void setUp() {
        orderDto = new OrderDto(new Date(), 1L, 2, "CREATED");
    }

    @Test
    public void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDto result = orderService.createOrder(orderDto);
        assertNotNull(result);
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = Arrays.asList(new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderDto> result = orderService.getAllOrders();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        ProductDto mockProduct = new ProductDto();
        mockProduct.setTitle("Sample Product");
        mockProduct.setPrice(29.90);
        mockProduct.setDescription("This is a sample product description.");
        Mockito.when(productClient.getProductById(orderID))
            .thenReturn(new ResponseEntity<>(Map.of("data", mockProduct), HttpStatus.OK));
        Mockito.when(objectMapper.convertValue(Mockito.any(), Mockito.eq(ProductDto.class)))
            .thenReturn(new ProductDto("Sample Product", "This is a sample product description.", 29.90));
  
        OrderDto result = orderService.getOrderById(orderID);
        assertNotNull(order);

        Mockito.verify(productClient, Mockito.times(1)).getProductById(1L);
        assertEquals("Sample Product", result.getProductDto().getTitle());
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderDto result = orderService.updateOrder(orderID, orderDto);
        assertNotNull(result);
    }

    @Test
    public void testUpdateNonExistentOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.updateOrder(1L, new OrderDto());
        });
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).delete(any(Order.class));

        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).delete(any(Order.class));
    }

    @Test
    public void testDeleteNonExistentProduct() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.deleteOrder(1L);
        });
    }
}
