package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jesusmoro.vcm.entities.Product;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ProductDao;

public abstract class ProductDaoJDBC implements ProductDao {

	private Connection conn;
	
	public ProductDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Product findById(Long id) {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		st = conn.prepareStatement(
			"SELECT * FROM product WHERE Id = ?");
		st.setLong(1, id);
		rs = st.executeQuery();
		if (rs.next()) {
			Product obj = new Product();
			obj.setId(rs.getLong("Id"));
			obj.setName(rs.getString("Name"));
			obj.setDescription(rs.getString("Description"));
			obj.setPrice(rs.getDouble("Price"));
			obj.setCodBarra(rs.getString("cod_barra"));
			obj.setImgUrl(rs.getString("img_url"));
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

@Override
public List<Product> findAll() {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		st = conn.prepareStatement(
			"SELECT * FROM tb_product ORDER BY Name");
		rs = st.executeQuery();

		List<Product> list = new ArrayList<>();

		while (rs.next()) {
			Product obj = new Product();
			obj.setId(rs.getLong("Id"));
			obj.setName(rs.getString("Name"));
			obj.setDescription(rs.getString("Description"));
			obj.setPrice(rs.getDouble("Price"));
			obj.setCodBarra(rs.getString("cod_barra"));
			obj.setImgUrl(rs.getString("img_url"));
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
public void insert(Product obj) {
	PreparedStatement st = null;
	try {
		st = conn.prepareStatement(
			"INSERT INTO tb_product " +
			"(Name, Description, Price, cod_barra, img_url) " +
			"VALUES " +
			"(?, ?, ?, ?, ?)", 
			Statement.RETURN_GENERATED_KEYS);

		st.setString(1, obj.getName());
		st.setString(2, obj.getDescription());
		st.setDouble(3, obj.getPrice());
		st.setString(4, obj.getCodBarra());
		st.setString(5, obj.getImgUrl());

		int rowsAffected = st.executeUpdate();
		
		if (rowsAffected > 0) {
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				Long id = rs.getLong(1);
				obj.setId(id);
			}
		}
		else {
			throw new DbException("Unexpected error! No rows affected!");
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
public void update(Product obj) {
	PreparedStatement st = null;
	try {
		st = conn.prepareStatement(
			"UPDATE tb_product " +
			"SET Name = ?, Description = ?, Price = ?, cod_barra = ?, img_url = ?"  +
			"WHERE Id = ?");

		st.setString(1, obj.getName());
		st.setString(2, obj.getDescription());
		st.setDouble(3, obj.getPrice());
		st.setString(4, obj.getCodBarra());
		st.setString(5, obj.getImgUrl());
		st.setLong(6, obj.getId());

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
		st = conn.prepareStatement(
			"DELETE FROM tb_product WHERE Id = ?");

		st.setLong(1, id);

		st.executeUpdate();
	}
	catch (SQLException e) {
		throw new DbIntegrityException(e.getMessage());
	} 
	finally {
		DB.closeStatement(st);
	}
}
@Override
public void retornaProduct(Product obj) {
	PreparedStatement st = null;
	try {
		st = conn.prepareStatement(
			"SELECT "
			+ "Id, "
			+ "Name,"
			+ "Descripition "
			+ "Price "
			+ "cod_barra "
			+ "img_url "
			+ "FROM tb_product WHERE Id = Product.id");

			st.setLong(1, obj.getId());
			st.setString(2, obj.getName());
			st.setString(3, obj.getDescription());
			st.setDouble(4, obj.getPrice());
			st.setString(5, obj.getCodBarra());
			st.setString(6, obj.getImgUrl());

	}
	catch (SQLException e) {
		throw new DbException(e.getMessage());
	}
	finally {
		DB.closeStatement(st);
		
	}
}
}
