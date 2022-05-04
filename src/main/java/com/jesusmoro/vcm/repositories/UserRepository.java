package com.jesusmoro.vcm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jesusmoro.vcm.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
