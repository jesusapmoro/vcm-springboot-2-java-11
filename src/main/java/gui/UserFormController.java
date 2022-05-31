package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.jesusmoro.vcm.entities.User;
import com.jesusmoro.vcm.exceptions.ValidationException;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.services.UserService;

public class UserFormController implements Initializable {

	private User entity;
	
	private UserService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtPhone;
	
	@FXML
	private TextField txtPassaword;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorPhone;
	
	@FXML
	private Label labelErrorPassaword;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setUser(User entity) {
		this.entity = entity;
	}
	
	public void setService(UserService service) {
		this.service = service;
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
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving objetct", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private User getFormData() {
		User obj = new User();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Fiel can't be empty");
		}
		obj.setName(txtName.getText());
		
		obj.setEmail(Constraints.setEmptyIfNull(txtEmail.getText()));
		
		obj.setPhone(Constraints.setEmptyIfNull(txtPhone.getText()));
		
		if (txtPassaword.getText() == null || txtPassaword.getText().trim().equals("")) {
			exception.addError("passawor", "Fiel can't be empty");
		}
		obj.setPassword(txtPassaword.getText());
		
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
		gui.util.Constraints.setTextFieldMaxLength(txtName, 70);
		gui.util.Constraints.setTextFieldMaxLength(txtEmail, 60);
		gui.util.Constraints.setTextFieldMaxLength(txtPhone, 30);
		gui.util.Constraints.setTextFieldMaxLength(txtPassaword, 10);
	}
	
	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException ("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		txtPhone.setText(entity.getPhone());
		txtPassaword.setText(entity.getPassword());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErrorPhone.setText((fields.contains("phone") ? errors.get("phone") : ""));
		labelErrorPassaword.setText((fields.contains("passaword") ? errors.get("passaword") : ""));
		}
	}

