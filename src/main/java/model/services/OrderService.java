package model.services;

import java.util.List;

import com.jesusmoro.vcm.entities.Order;

import model.dao.DaoFactory;
import model.dao.OrderDao;

public class OrderService {
	
	private OrderDao dao = DaoFactory.createOrderDao();
	
	public List<Order> findAll() {
		return dao.findAll();
	}
	
	public void saverOrUpdate(Order obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Order obj) {
		dao.deleteById(obj.getId());
	}
}
