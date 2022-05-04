package com.jesusmoro.vcm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jesusmoro.vcm.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
