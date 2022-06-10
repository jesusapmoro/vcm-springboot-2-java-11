package com.jesusmoro.vcm.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.jesusmoro.vcm.entities.Client;
import com.jesusmoro.vcm.entities.User;
import com.jesusmoro.vcm.repositories.ClientRepository;
import com.jesusmoro.vcm.services.exceptions.DatabaseException;
import com.jesusmoro.vcm.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	public List<Client> findAll() {
		return repository.findAll();
	}

	public Client findById(Long Id) {
		Optional<Client> obj = repository.findById(Id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Id));
	}

	public Client insert(Client obj) {
		return repository.save(obj);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public Client update(Long id, Client obj) {
		try {
			Client entity = repository.getById(id);
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Client entity, Client obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
	}

	public void remove(User obj) {
		repository.deleteById(obj.getId());

	}
}
