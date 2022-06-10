package com.jesusmoro.vcm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jesusmoro.vcm.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	
}
