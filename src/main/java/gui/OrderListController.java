package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.persistence.EmbeddedId;

import com.jesusmoro.vcm.entities.Order;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.services.OrderItemService;
import model.services.OrderService;
import model.services.ProductService;
import model.services.UserService;

public class OrderListController implements Initializable, DataChangeListener {

	@EmbeddedId
	private OrderService service;

	@FXML
	private TableView<Order> tableViewOrder;

	@FXML
	private TableColumn<Order, Integer> tableColumnId;

	@FXML
	private TableColumn<Order, Date> tableColumnOrderDate;

	@FXML
	private TableColumn<Order, Long> tableColumnClient;

	@FXML
	private TableColumn<Order, Integer> tableColumnStatus;

	@FXML
	private TableColumn<Order, Order> tableColumnEDIT;

	@FXML
	private TableColumn<Order, Order> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Order> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Order obj = new Order();
		createDialogForm(obj, "/gui/OrderForm.fxml", parentStage);
	}

	public void setOrderService(OrderService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InitializeableNode();
	}

	private void InitializeableNode() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		Utils.formatTableColumnDate(tableColumnOrderDate, "dd/MM/yyyy");
		tableColumnClient.setCellValueFactory(new PropertyValueFactory<>("client"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewOrder.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service esta nulo");
		}
		List<Order> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewOrder.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Order obj, String AbsoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AbsoluteName));
			Pane pane = loader.load();

			OrderFormController controller = loader.getController();
			controller.setOrder(obj);
			controller.setServices(new OrderService(), new UserService(), new OrderItemService(), new ProductService());
			controller.loadAssociatedOjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormDate();
			controller.updateTableView();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira os dados do pedido");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Excepnulltion", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Order, Order>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Order obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/OrderForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Order, Order>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Order obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});

	}

	private void removeEntity(Order obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que você quer Deletar");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço nulo");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
