package com.jesusmoro.vcm.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jesusmoro.vcm.entities.Client;

@RestController
@RequestMapping(value = "/client")
public class ClientResource {
	
	@GetMapping
	public ResponseEntity<Client> findAll() {
		Client cli = new Client(1L, "jesus", "vestcasa", "10992297826", "26559327", "284444", "jose c", "428", "cassi", "ms", "79540", "jesusap", "98156", "6712");
		return ResponseEntity.ok().body(cli);
	}

}
