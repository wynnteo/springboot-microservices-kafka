package com.wynnteo.ordermgmt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class OrderDto {

  private Long id;

  @NotNull(message = "Order date must not be null")
  private Date orderDt;

  @NotNull(message = "Product must not be null")
  private Long productId;

  @Min(1)
  private int quantity;

  private String status;

  private Date createdAt;

  private Date updatedAt;

  private ProductDto productDto;

  public OrderDto() {}

  public OrderDto(String status) {
    this.status = status;
  }

  public OrderDto(Date orderDt, Long productId, int quantity, String status) {
    this.orderDt = orderDt;
    this.productId = productId;
    this.quantity = quantity;
    this.status = status;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getOrderDt() {
    return this.orderDt;
  }

  public void setOrderDt(Date orderDt) {
    this.orderDt = orderDt;
  }

  public Long getProductId() {
    return this.productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public ProductDto getProductDto() {
    return this.productDto;
  }

  public void setProductDto(ProductDto productDto) {
    this.productDto = productDto;
  }
}
