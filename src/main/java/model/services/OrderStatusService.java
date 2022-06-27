package model.services;

import java.util.List;

import com.jesusmoro.vcm.entities.enums.OrderStatus;

import model.dao.OrderStatusDao;

public class OrderStatusService {
	
	private OrderStatusDao dao; 
	
	public List<OrderStatus> findAll() {
		return dao.findAll();
	}
	
	public void saverOrUpdate(OrderStatus obj) {
		if (obj.getDeclaringClass() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	//public void remove(OrderStatus obj) {
	//	dao.deleteById(obj.getDeclaringClass());
	//}
}
