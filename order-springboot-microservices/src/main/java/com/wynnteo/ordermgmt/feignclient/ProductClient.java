package com.wynnteo.ordermgmt.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${product-service.name}", url = "${products.url}", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getProductById(@PathVariable("id") Long id);
}