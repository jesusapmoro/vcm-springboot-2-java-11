package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemProduto;
	
	@FXML
	private MenuItem menuItemUsuario;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemProdutoAction() {
		System.out.println("onMenuItemProdutoAction");
	}
	
	@FXML
	public void onMenuItemUsuarioAction() {
		System.out.println("onMenuItemUsuarioAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		System.out.println("onMenuItemAboutAction");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
}
