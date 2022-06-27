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
import com.jesusmoro.vcm.entities.Product;

import db.DB;
import db.DbException;
import model.dao.OrderItemDao;

public class OrderItemDaoJDBC implements OrderItemDao {

	private Connection conn;

	public OrderItemDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(OrderItem obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO tb_order_item " + "(price, quantity, product_id, order_id) "
					+ "VALUES " + "(?, ?, ?, ?)");

			st.setDouble(1, obj.getPrice());
			st.setInt(2, obj.getQuantity());
			st.setLong(3, obj.getProduct().getId());
			st.setLong(4, obj.getOrder().getId());

			st.executeUpdate();

//			if (rowsAffected > 0) {
//				ResultSet rs = st.getGeneratedKeys();
//				if (rs.next()) {
//					long order = rs.getLong(1);
//					obj.setOrder(order);
//				}
//				DB.closeResultSet(rs);
//			} else {
//				throw new DbException("Nenhuma linha foi afetada");
//			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(OrderItem obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE tb_order_item "
					+ "SET price = ? , quantity = ?, product_id = ?, order_id = ? " + "WHERE order_id = ?");

			st.setDouble(1, obj.getPrice());
			st.setInt(2, obj.getQuantity());
			st.setLong(3, obj.getProduct().getId());
			st.setLong(4, obj.getOrder().getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Long order) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM tb_order WHERE Id = ?");

			st.setLong(1, order);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public OrderItem findById(Long id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement
					("SELECT tb_order_item.*,tb_order.id " 
					+ "FROM tb_order_item INNER JOIN tb_order "
					+ "ON tb_order_item.order_id = tb_order.id " 
					+ "WHERE tb_order_item.order_id = ?");

			st.setLong(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Order ord = instantiateOrder(rs);
				Product prod = instantiateProduct(rs);
				OrderItem obj = instantiateOrderItem(rs, ord, prod);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private OrderItem instantiateOrderItem(ResultSet rs, Order order, Product produto) throws SQLException {
		OrderItem obj = new OrderItem();
		obj.setQuantity(rs.getInt("quantity"));
		obj.setPrice(rs.getDouble("price"));
		obj.setOrder(order);
		obj.setProductr(produto);
		obj.getSubTotal();
		return obj;
	}

	private Order instantiateOrder(ResultSet rs) throws SQLException {
		Order ord = new Order();
		ord.setId(rs.getLong("order_id"));
		return ord;
	}

	private Product instantiateProduct(ResultSet rs) throws SQLException {
		Product prod = new Product();
		prod.setId(rs.getLong("product_id"));
		return prod;

	}

	@Override
	public List<OrderItem> findByOrder(Order order) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT tb_order_item.*,tb_order.id as OrderId "
					+ "FROM tb_order_item INNER JOIN tb_order " 
					+ "ON tb_order_item.order_id = tb_order.id "
					+ "WHERE tb_order.id = ? " 
					+ "ORDER BY OrderId");

			st.setLong(1, order.getId());

			rs = st.executeQuery();

			List<OrderItem> list = new ArrayList<>();
			Map<Long, Order> map = new HashMap<>();
			Map<Long, Product> map1 = new HashMap<>();

			while (rs.next()) {

				Order ord = map.get(rs.getLong("order_id"));
				Product prod = map1.get(rs.getLong("product_id"));

				if (ord == null) {
					ord = instantiateOrder(rs);
					map.put(rs.getLong("Order_id"), ord);
				}
				if (prod == null) {
					prod = instantiateProduct(rs);
					map1.put(rs.getLong("product_id"), prod);
				}

				OrderItem obj = instantiateOrderItem(rs, ord, prod);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<OrderItem> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT tb_order_item.*,tb_order.id as orderId " 
					+ "FROM tb_order_item INNER JOIN tb_order "
					+ "ON tb_order_item.order_id = tb_order.id " 
					+ "ORDER BY OrderId");

			rs = st.executeQuery();

			List<OrderItem> list = new ArrayList<>();
			Map<Long, Order> map = new HashMap<>();
			Map<Long, Product> map1 = new HashMap<>();

			while (rs.next()) {

				Order ord = map.get(rs.getLong("order_id"));
				Product prod = map1.get(rs.getLong("product_id"));

				if (ord == null) {
					ord = instantiateOrder(rs);
					map.put(rs.getLong("order_id"), ord);
				}
				if (prod == null) {
					prod = instantiateProduct(rs);
					map1.put(rs.getLong("product_id"), prod);
				}

				OrderItem obj = instantiateOrderItem(rs, ord, prod);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
