package com.wynnteo.ordermgmt.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wynnteo.ordermgmt.dto.OrderDto;
import com.wynnteo.ordermgmt.dto.ProductDto;
import com.wynnteo.ordermgmt.exception.ResourceNotFoundException;
import com.wynnteo.ordermgmt.feignclient.ProductClient;
import com.wynnteo.ordermgmt.services.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private OrderController orderController;

    @MockBean
    private OrderService orderService;

    private OrderDto orderDto;
    private Long orderId = 1L;

    @BeforeEach
    public void setUp() {
        reset(orderService); 

        ProductDto mockProduct = new ProductDto();
        mockProduct.setTitle("Sample Product");
        mockProduct.setPrice(29.90);
        mockProduct.setDescription("This is a sample product description.");

        orderDto = new OrderDto(new Date(), 1L, 2, "CREATED");
        orderDto.setProductDto(mockProduct);

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(orderDto);
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(orderDto));
        when(orderService.getOrderById(orderId)).thenReturn(orderDto); 
        when(orderService.updateOrder(eq(orderId), any(OrderDto.class))).thenReturn(new OrderDto(new Date(), 1L, 2, "PAID"));
        doNothing().when(orderService).deleteOrder(orderId);
    }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()", is(1)))
            .andExpect(jsonPath("$.data[0].status", is(orderDto.getStatus())));
            verify(orderService, times(1)).getAllOrders();    
    }

    @Test
    public void testGetOrderById() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(get("/api/orders/" + orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status", is(orderDto.getStatus())))
            .andExpect(jsonPath("$.data.quantity", is(orderDto.getQuantity())))
            .andExpect(jsonPath("$.data.productId", is(orderDto.getProductId().intValue())))
            .andExpect(jsonPath("$.data.productDto.title", is(orderDto.getProductDto().getTitle())));
    }

    @Test
    public void testGetOrderByIdNotFound() throws Exception {
        when(orderService.getOrderById(anyLong())).thenThrow(new ResourceNotFoundException("Order not found"));
        mockMvc.perform(get("/api/orders/6"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Order not found!")));
    }

    @Test
    public void testCreateOrder() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(orderDto);
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.status", is("CREATED")))
            .andExpect(jsonPath("$.data.quantity", is(orderDto.getQuantity())))
            .andExpect(jsonPath("$.data.productId", is(orderDto.getProductId().intValue())));
    }

    @Test
    public void testCreateOrderInvalidInput() throws Exception {
        OrderDto orderDto = new OrderDto(); 
        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(orderDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateOrder() throws Exception {
        Long orderId = 1L;
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(orderDto);

        mockMvc.perform(put("/api/orders/" + orderId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status", is("PAID")))
            .andExpect(jsonPath("$.data.quantity", is(orderDto.getQuantity())))
            .andExpect(jsonPath("$.data.productId", is(orderDto.getProductId().intValue())));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        Long orderId = 1L;

        doThrow(new ResourceNotFoundException("Order not found")).when(orderService).getOrderById(orderId);
        mockMvc.perform(delete("/api/orders/" + orderId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isNotFound());
    }
}
