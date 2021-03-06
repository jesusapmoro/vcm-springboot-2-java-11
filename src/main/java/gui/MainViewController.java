package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.OrderService;
import model.services.ProductService;
import model.services.UserService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemProduto;
	
	@FXML
	private MenuItem menuItemUsuario;
	
	@FXML
	private MenuItem menuItemOrder;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	private MenuItem menuItemPdv;
	
	@FXML
	public void onMenuItemProdutoAction() {
		loadView("/gui/ProductList.fxml", (ProductListController controller) -> {
			controller.setProductService1(new ProductService());
			controller.updateTableView();
		});
	}
	
	
	@FXML
	public void onMenuItemUsuarioAction() {
		loadView("/gui/UserList.fxml", (UserListController controller) -> {
			controller.setUserService(new UserService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemOrderAction() {
		loadView("/gui/OrderList.fxml", (OrderListController controller) -> {
			controller.setOrderService(new OrderService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemPdvAction() {
		loadView("/gui/PdvList.fxml", (PdvFormController controller) -> {
			controller.setServices(new OrderService(), new UserService());
			controller.loadAssociatedOjects();
		});
		}

	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a pagina", e.getMessage(), AlertType.ERROR);
		}
	}
}
