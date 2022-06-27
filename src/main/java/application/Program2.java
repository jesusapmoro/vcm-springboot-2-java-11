package application;

import java.util.List;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.OrderItem;

import model.dao.DaoFactory;
import model.dao.OrderItemDao;

public class Program2 {

	public static void main(String[] args) {

		OrderItemDao orderItemDao = DaoFactory.createOrderItemDao();

		System.out.println("=== TEST 1: orderItem findById ====");
		OrderItem orderItem = orderItemDao.findById((long) 1);
		System.out.println(orderItem);

		System.out.println("\n=== TEST 3: orderItem findAll ====");
		List<OrderItem> list = orderItemDao.findAll();
		for (OrderItem obj1 : list) {
			System.out.println(obj1);
		}

		System.out.println("\n=== TEST 2: order findByIdOrderItem ====");
		Order order = new Order((long) 1, null, null);
		List<OrderItem> list1 = orderItemDao.findByOrder(order);
		for (OrderItem obj : list1) {
			System.out.println(obj);
		}

	}
}
