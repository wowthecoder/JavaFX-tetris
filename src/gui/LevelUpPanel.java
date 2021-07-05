package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class LevelUpPanel extends BorderPane {
	private Label levelUpLabel;
	
    public LevelUpPanel() {
    	levelUpLabel = new Label("*LEVEL UP*");
    	levelUpLabel.getStyleClass().add("levelUpStyle");
    	
    	setCenter(levelUpLabel);
    }

	public void showPanel(ObservableList<Node> children) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(600), evt -> levelUpLabel.setVisible(false)), 
				new KeyFrame(Duration.millis(100), evt -> levelUpLabel.setVisible(true)));
		timeline.setCycleCount(3);
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				children.remove(LevelUpPanel.this);
			}
		});
		timeline.play();
	}
}
