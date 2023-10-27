package com.wynnteo.productmgmt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.wynnteo.productmgmt.dto.ProductDto;
import com.wynnteo.productmgmt.entity.Product;
import com.wynnteo.productmgmt.exception.ResourceNotFoundException;
import com.wynnteo.productmgmt.repository.ProductRepository;
import com.wynnteo.shareddto.OrderEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceImplTest {

  @InjectMocks
  private ProductServiceImpl productService;

  @Mock
  private ProductRepository productRepository;

  @Test
  public void testCreateProduct() {
    ProductDto productDto = new ProductDto();
    Product product = new Product();
    when(productRepository.save(any(Product.class))).thenReturn(product);

    ProductDto result = productService.createProduct(productDto);
    assertNotNull(result);
  }

  @Test
  public void testGetAllProducts() {
    List<Product> products = Arrays.asList(new Product());
    when(productRepository.findAll()).thenReturn(products);

    List<ProductDto> result = productService.getAllProducts();
    assertEquals(1, result.size());
  }

  @Test
  public void testGetProductsByStoreId() {
    List<Product> products = Arrays.asList(new Product());
    when(productRepository.findByStoreId(anyString())).thenReturn(products);

    List<ProductDto> result = productService.getProductsByStoreId("storeId");
    assertEquals(1, result.size());
  }

  @Test
  public void testGetProductById() {
    Product product = new Product();
    when(productRepository.findById(anyLong()))
      .thenReturn(Optional.of(product));

    ProductDto result = productService.getProductById(1L);
    assertNotNull(result);
  }

  @Test
  public void testUpdateProduct() {
    Product product = new Product();
    when(productRepository.findById(anyLong()))
      .thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenReturn(product);

    ProductDto result = productService.updateProduct(1L, new ProductDto());
    assertNotNull(result);
  }

  @Test
  public void testUpdateNonExistentProduct() {
    when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(
      ResourceNotFoundException.class,
      () -> {
        productService.updateProduct(1L, new ProductDto());
      }
    );
  }

  @Test
  public void testDeleteProduct() {
    Product product = new Product();
    when(productRepository.findById(anyLong()))
      .thenReturn(Optional.of(product));
    doNothing().when(productRepository).delete(any(Product.class));

    productService.deleteProduct(1L);
    verify(productRepository, times(1)).delete(any(Product.class));
  }

  @Test
  public void testDeleteNonExistentProduct() {
    when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(
      ResourceNotFoundException.class,
      () -> {
        productService.deleteProduct(1L);
      }
    );
  }

  @Test
  public void testConsumeOrder() {
    // Create a mock OrderEvent object
    OrderEvent orderEvent = new OrderEvent();
    orderEvent.setProductId(1L);
    orderEvent.setQuantity(2);

    // Create a mock Product object
    Product product = new Product();
    product.setId(1L);
    product.setStock(10);

    // Mock the productRepository.findById() method
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    // Call the consumeOrder() method
    productService.consumeOrder(orderEvent);

    // Verify that the productRepository.save() method was called once
    verify(productRepository, times(1)).save(product);

    assertEquals(product.getStock(), 8);
  }
}
