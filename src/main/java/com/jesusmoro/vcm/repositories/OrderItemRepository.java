package com.jesusmoro.vcm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jesusmoro.vcm.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
