package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.OrderItem;
import com.jesusmoro.vcm.entities.User;
import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.OrderDao;

public class OrderDaoJDBC implements OrderDao {

	private Connection conn;
	
	public OrderDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Order obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO tb_order " +
					"(order_date, client_id) " +
					"VALUES " +
					"(?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setDate(1, new java.sql.Date(obj.getOrderDate().getTime()));
			st.setLong(2, obj.getClient().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					long id = rs.getLong(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Nenhuma linha foi afetada");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Order obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE tb_order " +
					"SET order_date = ? , client_id = ? " +
					"WHERE id = ?");
			
			st.setDate(1, new java.sql.Date(obj.getOrderDate().getTime()));
			st.setLong(2, obj.getClient().getId());
			st.setLong(3, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Long id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM tb_order WHERE Id = ?");
			
			st.setLong(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Order findById(Long id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_order.*,tb_user.Name as UserName "
					+ ",tb_user.Email as UserEmail "
					+ "FROM tb_order INNER JOIN tb_user "
					+ "ON tb_order.client_id = tb_user.id "
					+ "WHERE tb_order.id = ?");
			
			st.setLong(1, id);
			rs = st.executeQuery();	
			if (rs.next()) {
				User use = instantiateUser(rs);
				OrderItem orderIte = instantiateOrderItem(rs);
				Order obj = instantiateOrder(rs, use, orderIte);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Order instantiateOrder(ResultSet rs, User user, OrderItem orderItem) throws SQLException {
		Order obj = new Order();
		obj.setId(rs.getLong("id"));
		obj.setOrderDate(new java.util.Date(rs.getTimestamp("order_date").getTime()));
		obj.setClient(user);
		obj.getItems();
		obj.getTotal();
		return obj;
	}

	private User instantiateUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("client_id"));
		user.setName(rs.getString("userName"));
		user.setEmail(rs.getString("userEmail"));
		return user;
	}
	
	private OrderItem instantiateOrderItem(ResultSet rs) throws SQLException {
		OrderItem orderItem = new OrderItem();
		orderItem.getOrder();
		orderItem.hashCode();
		return orderItem;
	}

	@Override
	public List<Order> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_order.*,tb_user.Name as UserName "
					+ ",tb_user.Email as UserEmail "
					+ "FROM tb_order INNER JOIN tb_user "
					+ "ON tb_order.client_id = tb_user.id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();	
			
			List<Order> list = new ArrayList<>();
			Map<Long, User> map = new HashMap<>();
			
			while (rs.next()) {
				
				User use = map.get(rs.getLong("client_id"));
				
				if (use == null) {
					use = instantiateUser(rs);
					map.put(rs.getLong("client_id"), use);
				}
				OrderItem orderIte = instantiateOrderItem(rs);
				Order obj = instantiateOrder(rs, use, orderIte);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Order> findByUser(User user) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_order.*,tb_user.Name as UserName "
					+ ",tb_user.Email as UserEmail "
					+ "FROM tb_order INNER JOIN tb_user "
					+ "ON tb_order.client_id = tb_user.id "
					+ "WHERE client_id = ? "
					+ "ORDER BY Name");
			
			st.setLong(1, user.getId());
			rs = st.executeQuery();	
			
			List<Order> list = new ArrayList<>();
			Map<Long, User> map = new HashMap<>();
			
			while (rs.next()) {
				
				User use = map.get(rs.getLong("client_id"));
				
				if (use == null) {
					use = instantiateUser(rs);
					map.put(rs.getLong("client_id"), use);
				}
				OrderItem orderIte = instantiateOrderItem(rs);
				Order obj = instantiateOrder(rs, use, orderIte);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Order> findByOrderItem(OrderItem orderItem) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_order.*,tb_order_item.order_id as OrderId "
					+ ",tb_order_item.quantity as OrderQuantity "
					+ "FROM tb_order INNER JOIN tb_order_item "
					+ "ON tb_order.id = tb_order_item.order_id "
					+ "WHERE id = ? "
					+ "ORDER BY order_id");
			
			//st.setLong(1, orderItem.getOrder());
			rs = st.executeQuery();	
			
			List<Order> list = new ArrayList<>();
			Map<Long, OrderItem> map = new HashMap<>();
			
			while (rs.next()) {
				
				OrderItem orderIte = map.get(rs.getLong("order"));
				
				if (orderIte == null) {
					orderIte = instantiateOrderItem(rs);
					map.put(rs.getLong("order"), orderIte);
				}
				User use = instantiateUser(rs);
				Order obj = instantiateOrder(rs, use, orderIte);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}

	