package application;

import java.util.List;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.OrderItem;
import com.jesusmoro.vcm.entities.User;

import model.dao.DaoFactory;
import model.dao.OrderDao;

public class Program {

	public static void main(String[] args) {
		
		OrderDao orderDao = DaoFactory.createOrderDao();
		
		System.out.println("=== TEST 1: order findById ====");
		Order order = orderDao.findById((long) 1);
		System.out.println(order);
		
		System.out.println("\n=== TEST 2: order findByIdUser ====");
		User user = new User((long) 2, null, null, null, null);
		List<Order> list = orderDao.findByUser(user);
		for (Order obj : list) {
			System.out.println(obj);
		}	
		
		System.out.println("\n=== TEST 3: order findAll ====");
		list = orderDao.findAll();
		for (Order obj1 : list) {
		System.out.println(obj1);	
		}
		
		System.out.println("\n=== TEST 2: order findByIdOrderItem ====");
		OrderItem orderItem = new OrderItem(order, null, null, null);
		List<Order> list1 = orderDao.findByOrderItem(orderItem);
		for (Order obj : list1) {
			System.out.println(obj);
	}
	}
}

