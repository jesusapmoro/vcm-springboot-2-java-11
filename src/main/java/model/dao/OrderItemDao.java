package model.dao;

import java.util.List;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.OrderItem;

public interface OrderItemDao {

	void insert(OrderItem obj);
	void update(OrderItem obj);
	void deleteById(Long id);
	OrderItem findById(Long id);
	List<OrderItem> findAll();
	List<OrderItem> findByOrder(Order order);
}	
