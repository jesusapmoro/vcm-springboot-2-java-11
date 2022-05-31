package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jesusmoro.vcm.entities.User;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.UserDao;

public abstract class UserDaoJDBC implements UserDao {

	private Connection conn;
	
	public UserDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public User findById(Long id) {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		st = conn.prepareStatement(
			"SELECT * FROM user WHERE Id = ?");
		st.setLong(1, id);
		rs = st.executeQuery();
		if (rs.next()) {
			User obj = new User();
			obj.setId(rs.getLong("Id"));
			obj.setName(rs.getString("Name"));
			obj.setEmail(rs.getString("Email"));
			obj.setPhone(rs.getString("Phone"));
			obj.setPassword(rs.getString("Password"));
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
public List<User> findAll() {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		st = conn.prepareStatement(
			"SELECT * FROM tb_user ORDER BY Name");
		rs = st.executeQuery();

		List<User> list = new ArrayList<>();

		while (rs.next()) {
			User obj = new User();
			obj.setId(rs.getLong("Id"));
			obj.setName(rs.getString("Name"));
			obj.setEmail(rs.getString("Email"));
			obj.setPhone(rs.getString("Phone"));
			obj.setPassword(rs.getString("Password"));
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
public void insert(User obj) {
	PreparedStatement st = null;
	try {
		st = conn.prepareStatement(
			"INSERT INTO tb_user " +
			"(Name, Email, Phone, Password) " +			
			"VALUES " +
			"(?, ?, ?, ?)", 
			Statement.RETURN_GENERATED_KEYS);

		st.setString(1, obj.getName());
		st.setString(2, obj.getEmail());
		st.setString(3, obj.getPhone());
		st.setString(4, obj.getPassword());
		

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
public void update(User obj) {
	PreparedStatement st = null;
	try {
		st = conn.prepareStatement(
			"UPDATE tb_user " +
			"SET Name = ?, Email = ?, Phone = ?, Password = ?" +	
			"WHERE Id = ?");

		st.setString(1, obj.getName());
		st.setString(2, obj.getEmail());
		st.setString(3, obj.getPhone());
		st.setString(4, obj.getPassword());
		st.setLong(5, obj.getId());
		
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
			"DELETE FROM tb_user WHERE Id = ?");

		st.setLong(1, id);

		st.executeUpdate();
	}
	catch (SQLException e) {
		throw new DbIntegrityException(e.getMessage());
	} 
	finally {
		DB.closeStatement(st);
	}
}}
