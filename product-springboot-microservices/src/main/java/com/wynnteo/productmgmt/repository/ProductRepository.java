package com.wynnteo.productmgmt.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.wynnteo.productmgmt.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findByStoreId(String storeId);
}
