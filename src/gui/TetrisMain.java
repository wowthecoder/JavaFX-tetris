package gui;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.*; 
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;



public class TetrisMain extends Application{
    Stage mainStage;
	public static void main(String[] args) {
        launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		mainStage = new Stage();
	    Scene scene = startUp();
		mainStage.setTitle("TetrisFX");
		mainStage.getIcons().add(new Image("resources/Tetrislogo.png"));
		mainStage.setScene(scene);
		mainStage.show();
	}
	
	public Scene startUp() throws Exception {
		URL location = getClass().getClassLoader().getResource("resources/gameLayout.fxml");
		ResourceBundle resource = null;
		FXMLLoader fxmlLoader = new FXMLLoader(location, resource);
	    Parent root = fxmlLoader.load();
	    GuiController c = fxmlLoader.getController();
	    c.setStage(this);
		
		Scene scene = new Scene(root, 660, 670);
		
		new GameController(c);
		return scene;
	}
}
