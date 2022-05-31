package model.services;

import java.util.List;

import com.jesusmoro.vcm.entities.Product;

import model.dao.DaoFactory;
import model.dao.ProductDao;

public class ProductService1 {
	
	private ProductDao dao = DaoFactory.createProductDao();
	
	public List<Product> findAll() {
		return dao.findAll();
	}
	
	public void saverOrUpdate(Product obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Product obj) {
		dao.deleteById(obj.getId());
	}
}
