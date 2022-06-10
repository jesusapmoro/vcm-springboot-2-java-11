package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jesusmoro.vcm.entities.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ComboBoxController implements Initializable {

	private Product product;

	@FXML
    private ComboBox<Product> cbProduct;
	
	
	private ObservableList<Product> obsList;
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		List<Product> list = new ArrayList<>();
		list.add(new Product(null, "Lapis", null, null, null, null));
		//cbProduct.getItems().add(list);
	
		obsList = FXCollections.observableArrayList(list);
		cbProduct.setItems(obsList);
		
		Callback<ListView<Product>, ListCell<Product>> factory = lv -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cbProduct.setCellFactory(factory);
		cbProduct.setButtonCell(factory.call(null));	
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}


}
