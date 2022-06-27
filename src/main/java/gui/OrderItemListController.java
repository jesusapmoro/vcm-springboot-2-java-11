package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jesusmoro.vcm.entities.OrderItem;

import gui.listeners.DataChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.services.OrderItemService;

public class OrderItemListController implements Initializable, DataChangeListener {
	
	private OrderItemService service;
	
	@FXML
    private TableColumn<OrderItem, Long> tableColumnOrderId;

    @FXML
    private TableColumn<OrderItem, Double> tableColumnPrice;

    @FXML
    private TableColumn<OrderItem, Long> tableColumnProductId;

    @FXML
    private TableColumn<OrderItem, Integer> tableColumnQuantity;

    @FXML
    private TableView<OrderItem> tableViewOrderItem;

	 
    
	 private ObservableList<OrderItem> obsList;

	 
	 public void setOrderItemService (OrderItemService service) {
	 this.service = service;
	 }

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InitializableNode();
	}

	private void InitializableNode() {
		tableColumnOrderId.setCellValueFactory(new PropertyValueFactory<>("order"));
		tableColumnProductId.setCellValueFactory(new PropertyValueFactory<>("product"));
		tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		
		//Stage stage = (Stage) Main.getMainScene().getWindow();
		//tableViewOrderItem.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<OrderItem> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrderItem.setItems(obsList);
	
	}
		
	@Override
	public void onDataChanged() {
		updateTableView();
	}
	}

