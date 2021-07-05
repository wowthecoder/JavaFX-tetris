package gui;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {
	private static Stage stage;
	private static GuiController viewController;
    public static void show(GuiController c) {
    	viewController = c;
    	stage = new Stage();
    	stage.initModality(Modality.APPLICATION_MODAL);
    	stage.setTitle("Start New Game");
    	stage.setMinWidth(300);
    	
    	Label label = new Label("If you start a new game, your current progress will be lost. \nHowever, your current score"
    			+ " may be saved. \nDo you wish to continue?");
    	Button yesBtn = new Button("Continue");
    	yesBtn.setOnAction(e -> yes());
    	Button noBtn = new Button("Cancel");
    	noBtn.setOnAction(e -> no());
    	HBox hBox = new HBox(30, yesBtn, noBtn);
    	hBox.setAlignment(Pos.CENTER);
    	VBox vbox = new VBox(20, label, hBox);
    	Scene scene = new Scene(vbox, 400, 200);
    	stage.setScene(scene);
    	stage.setOnCloseRequest(e -> no());
    	stage.show();
    }
    
    private static void yes() {
    	viewController.newGame();
    	stage.close();
    }
    
    private static void no() {
    	viewController.cancelNewGame();
    	stage.close();    	
    }
}
