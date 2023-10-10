package com.wynnteo.productmgmt.services;

import com.wynnteo.productmgmt.dto.ProductDto;
import com.wynnteo.shareddto.OrderEvent;
import java.util.List;

public interface ProductService {
  List<ProductDto> getAllProducts();
  List<ProductDto> getProductsByStoreId(String storeId);
  ProductDto createProduct(ProductDto product);
  ProductDto getProductById(Long id); // Get a product by ID
  ProductDto updateProduct(Long id, ProductDto productDetails); // Update a product
  void deleteProduct(Long id); // Delete a product
  void updateStock(Long id, int quantity);
  void consumeOrder(OrderEvent order);
}
