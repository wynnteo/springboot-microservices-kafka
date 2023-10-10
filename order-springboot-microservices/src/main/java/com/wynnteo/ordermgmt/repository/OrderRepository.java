package com.wynnteo.ordermgmt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wynnteo.ordermgmt.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}