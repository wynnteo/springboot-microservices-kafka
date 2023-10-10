package com.wynnteo.productmgmt.controller;

import static com.wynnteo.productmgmt.constants.Messages.*;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wynnteo.productmgmt.dto.ProductDto;
import com.wynnteo.productmgmt.exception.ResourceNotFoundException;
import com.wynnteo.productmgmt.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("statusCode", HttpStatus.OK.value());
        response.put("message", PRODUCTS_RETRIEVED_SUCCESS);
        response.put("data", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<Map<String, Object>> getProductsByStoreId(@PathVariable String storeId) {
        List<ProductDto> products = productService.getProductsByStoreId(storeId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("statusCode", HttpStatus.OK.value());
        response.put("message", PRODUCTS_STORE_RETRIEVED_SUCCESS);
        response.put("data", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        try {
            ProductDto product = productService.getProductById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", PRODUCTS_RETRIEVED_SUCCESS);
            response.put("data", product);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            response.put("message", PRODUCT_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody ProductDto product) {
        ProductDto createdProduct = productService.createProduct(product);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("statusCode", HttpStatus.CREATED.value());
        response.put("message", PRODUCT_CREATED_SUCCESS);
        response.put("data", createdProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDetails) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", PRODUCT_UPDATED_SUCCESS);
            response.put("data", updatedProduct);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            response.put("message", PRODUCT_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("statusCode", HttpStatus.NO_CONTENT.value());
            response.put("message", PRODUCT_DELETED_SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            response.put("message", PRODUCT_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        try {
            productService.updateStock(id, quantity);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "Product stock updated successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            response.put("message", "Product not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
