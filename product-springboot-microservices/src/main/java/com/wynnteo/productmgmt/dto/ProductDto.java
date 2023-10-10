package com.wynnteo.productmgmt.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class ProductDto {

    @NotNull(message = "Title must not be null")
    @Size(min = 2, max = 255)
    private String title;

    private String description;

    @NotNull(message = "Price must not be null")
    @Min(0)
    private Double price;

    @NotNull(message = "Store Id must not be null")
    private String storeId;

    @NotNull(message = "Stock number must not be null")
    private Integer stock;

    private Date createdAt;

    private Date updatedAt;


    public ProductDto() {

    }

    public ProductDto(String title, String description, Double price, String storeId, Integer stock) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.storeId = storeId;
        this.stock = stock;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", price='" + getPrice() + "'" +
            ", storeId='" + getStoreId() + "'" +
            "}";
    }
    
}
