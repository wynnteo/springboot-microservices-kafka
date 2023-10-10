package com.wynnteo.productmgmt.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wynnteo.productmgmt.dto.ProductDto;
import com.wynnteo.productmgmt.exception.ResourceNotFoundException;
import com.wynnteo.productmgmt.services.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private ProductDto productDto;
    private Long productId = 1L;

    @BeforeEach
    public void setUp() {
        reset(productService); // Reset the mock behavior
        productDto = new ProductDto("Product title", "Product description", 349.90, "S-4", 100);
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);
        when(productService.getProductById(productId)).thenReturn(productDto); // Return the original productDto
        when(productService.updateProduct(eq(productId), any(ProductDto.class))).thenReturn(new ProductDto("Updated title", "Updated description", 299.90, "S-4", 50)); // Return a new instance with updated values
        doNothing().when(productService).deleteProduct(productId);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetProductsByStoreId() throws Exception {
        mockMvc.perform(get("/api/products/store/storeId"))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetProductById() throws Exception {
        Long productId = 1L;
        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is(productDto.getTitle())))
                .andExpect(jsonPath("$.data.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.data.price", is(productDto.getPrice())))
                .andExpect(jsonPath("$.data.storeId", is(productDto.getStoreId())))
                .andExpect(jsonPath("$.data.stock", is(productDto.getStock())));
    }

    @Test
    public void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(anyLong())).thenThrow(new ResourceNotFoundException("Product not found"));
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Product not found!")));
    }

    @Test
    public void testCreateProduct() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productDto);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title", is(productDto.getTitle())))
                .andExpect(jsonPath("$.data.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.data.price", is(productDto.getPrice())))
                .andExpect(jsonPath("$.data.storeId", is(productDto.getStoreId())))
                .andExpect(jsonPath("$.data.stock", is(productDto.getStock())));
    }

    @Test
    public void testCreateProductInvalidInput() throws Exception {
        ProductDto productDto = new ProductDto(); // Invalid product without required fields

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Long productId = 1L;
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(put("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is(productDto.getTitle())))
                .andExpect(jsonPath("$.data.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.data.price", is(productDto.getPrice())))
                .andExpect(jsonPath("$.data.storeId", is(productDto.getStoreId())))
                .andExpect(jsonPath("$.data.stock", is(productDto.getStock())));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Long productId = 1L;
    
        // Assuming that your service layer throws a ResourceNotFoundException if the product doesn't exist
        doThrow(new ResourceNotFoundException("Product not found")).when(productService).getProductById(productId);
    
        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isNoContent());
    
        // Optionally, you can also verify that the product no longer exists
        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isNotFound());
    }
    
}
