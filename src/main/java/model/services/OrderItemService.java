package model.services;

import java.util.List;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.OrderItem;

import model.dao.DaoFactory;
import model.dao.OrderItemDao;

public class OrderItemService {

	private OrderItemDao dao = DaoFactory.createOrderItemDao();

	public List<OrderItem> findAll() {
		return dao.findAll();
	}

	public void saverOrUpdate(OrderItem obj) {
		if (obj.getOrder() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(OrderItem obj) {
		dao.deleteById(obj.getProduct().getId());
	}

	public List<OrderItem> findByOrder(Order order) {
		return  dao.findByOrder(order); 
	}
}	
