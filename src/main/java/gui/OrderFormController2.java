package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.jesusmoro.vcm.entities.Order;
import com.jesusmoro.vcm.entities.OrderItem;
import com.jesusmoro.vcm.entities.User;
import com.jesusmoro.vcm.exceptions.ValidationException;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.services.OrderItemService;
import model.services.OrderService;
import model.services.UserService;

public class OrderFormController2 implements Initializable {

	private Order entity;

	private OrderService service;

	private UserService userService;

	private OrderItemService orderItemService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker dpOrderDate;

	@FXML
	private TextField txtClient;

	@FXML
	private ComboBox<User> comboBoxUser;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorOrderDate;

	@FXML
	//@ManyToOne
	//@JoinColumn(name = "order_id", referencedColumnName = "order")
	//private Order order;
	private TableColumn<OrderItem, Long> tableColumnOrderItemOrderId;

	@FXML
	private TableColumn<OrderItem, Double> tableColumnOrderItemPrice;

	@FXML
	private TableColumn<OrderItem, Long> tableColumnOrderItemProductId;

	@FXML
	private TableColumn<OrderItem, Integer> tableColumnOrderItemQuantity;

	@FXML
	private TableView<OrderItem> tableViewOrderItem;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<User> obsList;
	
	public void setOrder(Order entity) {
		this.entity = entity;
	}

	public void setServices(OrderService service, UserService userService, OrderItemService orderItemService) {
		this.service = service;
		this.userService = userService;
		this.orderItemService = orderItemService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saverOrUpdate(entity);
			notifyDataDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Error saving objetct", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Order getFormData() {
		Order obj = new Order();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (dpOrderDate.getValue() == null) {
			exception.addError("dateOrder", "Fiel can't be empty");
		} else {
			Instant instant = Instant.from(dpOrderDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setOrderDate(Date.from(instant));
		}

		obj.setClient(comboBoxUser.getValue());

		if (exception.getErros().size() > 0) {
			throw exception;
		}

		return obj;
	}
	

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		gui.util.Constraints.setTextFieldInteger(txtId);
		Utils.formatDatePicker(dpOrderDate, "dd/MM/yyyy");

		initializeComboBoxUser();
		InitializabletableViewOrderItem();
	}

	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));

		if (entity.getOrderDate() != null) {
			dpOrderDate.setValue(LocalDate.ofInstant(entity.getOrderDate().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getClient() == null) {
			comboBoxUser.getSelectionModel().selectFirst();
		} else {
			comboBoxUser.setValue(entity.getClient());
		}

		if (entity.getItems() == null) {
			tableColumnOrderItemOrderId.setText(String.valueOf(false));
		}

	}

	public void loadAssociatedOjects() {
		if (userService == null) {
			throw new IllegalStateException("UserService esta nulo");
		}
		List<User> list = userService.FindAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxUser.setItems(obsList);

		if (orderItemService == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<OrderItem> listOrderItem = orderItemService.findAll();
		obsListOrderItem = FXCollections.observableArrayList(listOrderItem);
		tableViewOrderItem.setItems(obsListOrderItem);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorOrderDate.setText((fields.contains("orderDate") ? errors.get("orderDate") : ""));
	}

	private void initializeComboBoxUser() {
		Callback<ListView<User>, ListCell<User>> factory = lv -> new ListCell<User>() {
			@Override
			protected void updateItem(User item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxUser.setCellFactory(factory);
		comboBoxUser.setButtonCell(factory.call(null));
	}
	
	private ObservableList<OrderItem> obsListOrderItem;
	
	
	public void setOrderItemService(OrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	private void InitializabletableViewOrderItem() {
		tableColumnOrderItemOrderId.setCellValueFactory(new PropertyValueFactory<>("order"));
		tableColumnOrderItemProductId.setCellValueFactory(new PropertyValueFactory<>("product"));
		tableColumnOrderItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tableColumnOrderItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		// Stage stage = (Stage) Main.getMainScene().getWindow();
		// tableViewOrderItem.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<OrderItem> list = orderItemService.findAll();
		obsListOrderItem = FXCollections.observableArrayList(list);
		tableViewOrderItem.setItems(obsListOrderItem);
	}

}
