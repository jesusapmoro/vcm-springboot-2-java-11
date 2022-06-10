package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.jesusmoro.vcm.entities.Order;
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
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.services.OrderService;
import model.services.UserService;

public class OrderFormController implements Initializable {

	private Order entity;

	private OrderService service;

	private UserService userService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker dpDateOrder;

	@FXML
	private TextField txtClient;

	@FXML
	private ComboBox<User> comboBoxUser;

	@FXML
	private Label labelErrorName;

	// @FXML
	// private TextField txtImgUrl;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	private Labeled txtOrderStatus;

	private ObservableList<User> obsList;

	public void setOrder(Order entity) {
		this.entity = entity;
	}

	public void setServices(OrderService service, UserService userService) {
		this.service = service;
		this.userService = userService;
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

		//if (txtClient.getText() == null || txtClient.getText().trim().equals("")) {
			//exception.addError("client", "O campo não pode ser vazio");
		// obj.setClient(txtClient.getText());

		if (dpDateOrder.getValue() == null) {
			exception.addError("DateOrder", "Fiel can't be empty");
		}

		// obj.setOrderStatus(Utils.tryParseToInt(txtOrderStatus.getText()));

		// obj.setImgUrl(txtImgUrl.getText());

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
		//gui.util.Constraints.setTextFieldMaxLength(txtClient, 40);
		Utils.formatDatePicker(dpDateOrder, "dd/MM/yyyy");
		
		initializeComboBoxUser();
	}

	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		if (entity.getDateOrder() != null) {
			dpDateOrder.setValue(LocalDate.ofInstant(entity.getDateOrder().toInstant(), ZoneId.systemDefault()));
		}
		// txtClient.setText(entity.getClient());
		// txtImgUrl.setText(entity.getImgUrl());
		if (entity.getClient() == null) {
			comboBoxUser.getSelectionModel().selectFirst();
		}
		else {
		comboBoxUser.setValue(entity.getClient());
		}
	}	
	
	public void loadAssociatedOjects() {
		if (userService == null) {
			throw new IllegalStateException("UserService esta nulo");
		}
		List<User> list = userService.FindAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxUser.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("client")) {
			labelErrorName.setText(errors.get("client"));
		}
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
}
