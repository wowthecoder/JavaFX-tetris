package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameOverPanel extends BorderPane {
    public GameOverPanel() {
    	Label gameOverLabel = new Label("GAME OVER!");
    	gameOverLabel.getStyleClass().add("gameOverStyle");
    	
    	setCenter(gameOverLabel);
    }
}
