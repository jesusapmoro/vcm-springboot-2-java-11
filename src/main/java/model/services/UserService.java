package model.services;

import java.util.List;

import com.jesusmoro.vcm.entities.User;

import model.dao.DaoFactory;
import model.dao.UserDao;

public class UserService {
	
	private UserDao  dao = DaoFactory.createUserDao();
	
	public List<User> FindAll() {
		return dao.findAll();
	}

	public void saverOrUpdate(User obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(User obj) {
		dao.deleteById(obj.getId());
	}
}

