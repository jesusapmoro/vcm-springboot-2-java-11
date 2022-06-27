package gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jesusmoro.vcm.entities.enums.OrderStatus;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class ComboBoxController implements Initializable {

	//private OrderStatus status;

	@FXML
    private ComboBox<OrderStatus> cbxStatus;
	
	//private ObservableList<OrderStatus> obsList;
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ComboBox<OrderStatus> cbxStatus = new ComboBox<>();
		cbxStatus.setItems(FXCollections.observableArrayList(OrderStatus.values()));
			//cbxStatus.setItems(null).setAll(OrderStatus.values());
		
		}

}
