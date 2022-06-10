package model.dao;

import java.util.List;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.User;
import com.jesusmoro.vcm.entities.enums.OrderStatus;

public interface OrderDao {

	void insert(Order obj);
	void update(Order obj);
	void deleteById(Long id);
	Order findById(Long id);
	List<Order> findAll();
	List<Order> findByUser(User user);
	List<Order> findByOrderStatus(OrderStatus orderStatus);
}
