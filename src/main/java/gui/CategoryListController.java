package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.http.ResponseEntity;

import com.jesusmoro.vcm.entities.Category;
import com.jesusmoro.vcm.resources.CategoryResource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoryListController implements Initializable {

	private CategoryResource resource;
	
	@FXML
	private TableView<Category> tableViewCategory;
	
	@FXML
	private TableColumn<Category, Long> tableColumnId;
	
	@FXML
	private TableColumn<Category, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	ObservableList<ResponseEntity<List<Category>>> obsList;
	
	public void setCategoryResource(CategoryResource resource) {
		this.resource = resource;
	}

	@Override
	public void initialize(URL url, ResourceBundle ur) {
		initializeNodes();
		
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
	}

	@SuppressWarnings("unchecked")
	public void updateTableView() {
		if (resource == null) {
			throw new IllegalStateException("resource was null");
		}
		ResponseEntity<List<Category>> list = resource.findAll();
		obsList = FXCollections.observableArrayList(list);
		//tableViewCategory.setItems(obsList);
		//initEditButtons();
		//initRemoveButtons();
	}
}
