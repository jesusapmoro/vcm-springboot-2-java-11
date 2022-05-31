package model.dao;

import java.util.List;

import com.jesusmoro.vcm.entities.User;

public interface UserDao {

	void insert(User obj);
	void update(User obj);
	void deleteById(Long id);
	User findById(Long id);
	List<User> findAll();
}
