package com.jesusmoro.vcm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jesusmoro.vcm.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
