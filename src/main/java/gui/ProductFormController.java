package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.jesusmoro.vcm.entities.Product;
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
import model.services.ProductService;

public class ProductFormController implements Initializable {

	private Product entity;
	
	private ProductService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtCodBarra;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private TextField txtDescription;
	
	@FXML
	private TextField txtPrice;
	
	@FXML
	private TextField txtImgUrl;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setProduct(Product entity) {
		this.entity = entity;
	}
	
	public void setProductService(ProductService service) {
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

	private Product getFormData() {
		Product obj = new Product();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "O campo não pode ser vazio");
		}
		obj.setName(txtName.getText());
		
		obj.setDescription(txtDescription.getText());
		
		obj.setPrice(Utils.tryParseToDouble(txtPrice.getText()));
		
		obj.setCodBarra(Constraints.setEmptyIfNull(txtCodBarra.getText()));
		
		//obj.setImgUrl(txtImgUrl.getText());
		
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
		gui.util.Constraints.setTextFieldMaxLength(txtName, 30);
		gui.util.Constraints.setTextFieldMaxLength(txtDescription, 40);
		gui.util.Constraints.setTextFieldDouble(txtPrice);
		gui.util.Constraints.setTextFieldMaxLength(txtCodBarra, 13);
		//gui.util.Constraints.setTextFieldMaxLength(txtImgUrl, 40);
		
	}
	
	public void updateFormDate() {
		if (entity == null) {
			throw new IllegalStateException ("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtDescription.setText(entity.getDescription());
		txtPrice.setText(String.valueOf(entity.getPrice()));
		txtCodBarra.setText(entity.getCodBarra());
		//txtImgUrl.setText(entity.getImgUrl());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
