package application;

import java.io.IOException;

import com.jesusmoro.vcm.resources.CategoryResource;

import gui.CategoryListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Mainteste extends Application {

	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CategoryList.fxml"));
			VBox vBox = loader.load();
		
			//vBox.setFitToHeight(true);
			//vBox.setFitToWidth(true);
		
					
			mainScene = new Scene(vBox);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Category");
			primaryStage.show();
		
			CategoryListController controller = loader.getController();
			controller.setCategoryResource(new CategoryResource());
			controller.updateTableView();
		
		}catch (IOException e) {
			e.printStackTrace();
	}
	}
	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
