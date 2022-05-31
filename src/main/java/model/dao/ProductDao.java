package model.dao;

import java.util.List;

import com.jesusmoro.vcm.entities.Product;

public interface ProductDao {

	void insert(Product obj);
	void update(Product obj);
	void deleteById(Long id);
	Product findById(Long id);
	List<Product> findAll();
}
