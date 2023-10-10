package com.wynnteo.productmgmt.services;

import com.wynnteo.productmgmt.dto.ProductDto;
import com.wynnteo.productmgmt.entity.Product;
import com.wynnteo.productmgmt.exception.ResourceNotFoundException;
import com.wynnteo.productmgmt.repository.ProductRepository;
import com.wynnteo.shareddto.OrderEvent;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  ProductRepository productRepository;

  private static final Logger logger = LoggerFactory.getLogger(
    ProductServiceImpl.class
  );

  @Override
  public ProductDto createProduct(ProductDto productDto) {
    logger.info("Creating product: {}", productDto);

    ModelMapper modelMapper = new ModelMapper();
    modelMapper
      .getConfiguration()
      .setMatchingStrategy(MatchingStrategies.STRICT);
    Product product = modelMapper.map(productDto, Product.class);
    productRepository.save(product);

    return modelMapper.map(product, ProductDto.class);
  }

  @Override
  public List<ProductDto> getAllProducts() {
    logger.info("Fetching all products");
    List<Product> productList = (List<Product>) productRepository.findAll();

    Type listType = new TypeToken<List<ProductDto>>() {}.getType();

    return new ModelMapper().map(productList, listType);
  }

  @Override
  public List<ProductDto> getProductsByStoreId(String storeId) {
    logger.info("Fetching products by store ID: {}", storeId);
    List<Product> productList = productRepository.findByStoreId(storeId);
    Type listType = new TypeToken<List<ProductDto>>() {}.getType();
    return new ModelMapper().map(productList, listType);
  }

  @Override
  public ProductDto getProductById(Long id) {
    logger.info("Fetching product by ID: {}", id);
    Product product = productRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    return new ModelMapper().map(product, ProductDto.class);
  }

  @Override
  public ProductDto updateProduct(Long id, ProductDto productDetails) {
    logger.info("Updating product with ID: {}", id);

    ModelMapper modelMapper = new ModelMapper();
    modelMapper
      .getConfiguration()
      .setMatchingStrategy(MatchingStrategies.STRICT);

    Product product = productRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    product.setTitle(productDetails.getTitle());
    product.setDescription(productDetails.getDescription());
    product.setPrice(productDetails.getPrice());
    product.setStoreId(productDetails.getStoreId());
    product.setStock(productDetails.getStock());
    productRepository.save(product);

    return modelMapper.map(product, ProductDto.class);
  }

  @Override
  public void deleteProduct(Long id) {
    logger.info("Deleting product with ID: {}", id);
    Product product = productRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    productRepository.delete(product);
  }

  public void updateStock(Long id, int quantity) {
    Product product = productRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
  }

  @KafkaListener(topics = "order-update-topic", groupId = "order-group")
  public void consumeOrder(OrderEvent orderEvent) {
    // Logic to process the order
    System.out.println("Order Consumed: " + orderEvent);

    Product product = productRepository
      .findById(orderEvent.getProductId())
      .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    product.setStock(product.getStock() - orderEvent.getQuantity());
    productRepository.save(product);
  }
}
