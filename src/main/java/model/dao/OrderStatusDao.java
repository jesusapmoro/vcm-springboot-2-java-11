package model.dao;

import java.util.List;

import com.jesusmoro.vcm.entities.enums.OrderStatus;

public interface OrderStatusDao {

	void insert(OrderStatus obj);
	void update(OrderStatus obj);
	void deleteById(Integer id);
	OrderStatus findById(Integer id);
	List<OrderStatus> findAll();
}
