package gui;

import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.*;
import logic.*;
import logic.bricks.*;
import logic.events.*;

public class GuiController implements Initializable {
	private static final int BRICK_SIZE = 25;
	TetrisMain tetris;
	Timeline timeLine;
	private InputEventListener eventListener;
	private Rectangle[][] displayMatrix;
	private Rectangle[][] rectangles;
	private BooleanProperty paused = new SimpleBooleanProperty();
	private BooleanProperty isGameOver = new SimpleBooleanProperty();
	private Brick savedBrickMatrix;
	private Point shadowPos;
	private int speed = 400;
	
	@FXML
	private GridPane gamePanel;
	@FXML
	private GridPane nextBrick;
	@FXML
	private GridPane savedBrick;
	@FXML
	private GridPane shadowPanel;
	@FXML
	private GridPane brickPanel;
	@FXML
	private Text scoreValue;
	@FXML
	private Text linesCleared;
	@FXML
	private Text highScore;
	@FXML
	private Group groupNotification;
	@FXML
	private Button newGameBtn;
	@FXML
	private ToggleButton pauseButton;
	@FXML
	private GameOverPanel gameOverPanel;
	
	public void setStage(TetrisMain tetris) {
		this.tetris = tetris;
	}
	
	public void initGameView(int[][] boardMatrix, ViewData viewData) {
		displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
		for (int i = 2; i < boardMatrix.length; i++)
		{
			for (int j = 0; j < boardMatrix[i].length; j++) {
				Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				rectangle.setFill(Color.TRANSPARENT);
				rectangle.setStroke(Color.rgb(148, 147, 139, 0.3));
				rectangle.setStrokeWidth(1);
				rectangle.setStrokeType(StrokeType.INSIDE);
				displayMatrix[i][j] = rectangle;
				gamePanel.add(rectangle, j, i-2);
			}
		}
		rectangles = new Rectangle[viewData.getBrickData().length][viewData.getBrickData()[0].length];
		for (int i = 0; i < viewData.getBrickData().length; i++)
		{
			for (int j = 0; j < viewData.getBrickData()[i].length; j++)
			{
				Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				rectangle.setFill(getFillColor(viewData.getBrickData()[i][j]));
				rectangles[i][j] = rectangle;
				brickPanel.add(rectangle, j, i);
			}
		}
		
		brickPanel.setLayoutX(gamePanel.getLayoutX() + (viewData.getxPosition() * BRICK_SIZE) + (viewData.getxPosition() * 2));
		brickPanel.setLayoutY(-54 + gamePanel.getLayoutY() + (viewData.getyPosition() * BRICK_SIZE) + (viewData.getyPosition() * 2));
	    
		generatePreviewPanel(viewData.getNextBrickData());
		
		newGameBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				timeLine.pause();
				AlertBox.show(GuiController.this);
			}			
		});
		
		timeLine = new Timeline(new KeyFrame(Duration.millis(speed), ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}
	
	private void generatePreviewPanel(int[][] nextBrickData) {
		nextBrick.getChildren().clear();
		for (int i = 0; i < nextBrickData.length; i++) 
		{
			for (int j = 0; j < nextBrickData[i].length; j++)
			{
				Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				setRectangleData(nextBrickData[i][j], rectangle);
				if (nextBrickData[i][j] != 0)
					nextBrick.add(rectangle,j, i);
			}
		}
	}
	
	private void generateSavedBrick(int[][] savedBrickData) {
		savedBrick.getChildren().clear();
		for (int i = 0; i < savedBrickData.length; i++) 
		{
			for (int j = 0; j < savedBrickData[i].length; j++) 
			{
				Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				setRectangleData(savedBrickData[i][j], rectangle);
				if (savedBrickData[i][j] != 0)
					savedBrick.add(rectangle, j, i);
			}
		}
	}
	
	public void refreshShadow(ViewData viewData) {
		shadowPanel.getChildren().clear();
		int[][] brickMatrix = viewData.getBrickData();
		int[][] shadow = new int[brickMatrix.length][brickMatrix[0].length];
		shadowPos = new Point(viewData.getxPosition(), viewData.getyPosition());
		for (int i = 0; i < brickMatrix.length; i++)
		{
			for (int j = 0; j < brickMatrix.length; j++)
			{
				if (brickMatrix[i][j] != 0)
					shadow[i][j] = 8;
			}
		}
		for (int i = 0; i < shadow.length; i++)
		{
			for (int j = 0; j < shadow[i].length; j++)
			{
				Rectangle rec = new Rectangle(BRICK_SIZE, BRICK_SIZE);
				rec.setFill(Color.TRANSPARENT);
				if (shadow[i][j] != 0)
				{
					rec.setArcHeight(5);
					rec.setArcWidth(5);
				    rec.setStroke(Color.GOLD);
				    rec.setStrokeWidth(2);
				    rec.setStrokeType(StrokeType.INSIDE);
				}
				shadowPanel.add(rec, j, i);
			}
		}
		updateShadowPos();
		for (int i = 0; i < 25; i++)
		{
			if (!eventListener.onShadowDown(shadowPos))
				break;
			Point p = new Point(shadowPos);
			p.translate(0, 1);
			shadowPos = p;
			updateShadowPos();
		}
	}

	public void updateShadowPos() {
		shadowPanel.setLayoutX(gamePanel.getLayoutX() + (shadowPos.x * BRICK_SIZE) + (shadowPos.x * 2));
		shadowPanel.setLayoutY(gamePanel.getLayoutY() + ((shadowPos.y-2) * BRICK_SIZE) + ((shadowPos.y-2) * 2));
	}
	
	public void refreshGameBackground(int[][] board) {
		for (int i = 2; i < board.length; i++) 
		{
			for (int j = 0; j < board[i].length; j++)
			{
				setRectangleData(board[i][j], displayMatrix[i][j]);
			}	
		}
	}
	
	private void setRectangleData(int color, Rectangle rectangle) {
		rectangle.setFill(getFillColor(color));
		rectangle.setArcHeight(5);
		rectangle.setArcWidth(5);
	}

	public void bindScore(IntegerProperty scoreProperty, IntegerProperty linesProperty, int highScoreValue) {
		scoreValue.textProperty().bind(scoreProperty.asString());
		linesCleared.textProperty().bind(linesProperty.asString());
		highScore.setText(Integer.toString(highScoreValue));
	}
	
	public void goFaster() {
		if (speed > 100)
		{
			speed -= 10;
			LevelUpPanel levelupPanel = new LevelUpPanel();
			groupNotification.getChildren().add(levelupPanel);
			levelupPanel.showPanel(groupNotification.getChildren());
			timeLine.setRate(timeLine.getCycleDuration().toMillis() / speed);
			System.out.println(timeLine.getCycleDuration() + " / " + timeLine.getCurrentRate());
		}
	}
	
	private void moveDown(MoveEvent event) {
		DownData downData = eventListener.onDownEvent(event);
		if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) 
		{
			NotificationPanel notifyPanel = new NotificationPanel(" + " + downData.getClearRow().getScoreBonus());
			groupNotification.getChildren().add(notifyPanel);
			notifyPanel.showScore(groupNotification.getChildren());
		}
		refreshBrick(downData.getViewData());
	}
	
	private void refreshBrick(ViewData viewData) {
		brickPanel.setLayoutX(gamePanel.getLayoutX() + (viewData.getxPosition() * BRICK_SIZE) + (viewData.getxPosition() * 2));
		brickPanel.setLayoutY(-54 + gamePanel.getLayoutY() + (viewData.getyPosition() * BRICK_SIZE) + (viewData.getyPosition() * 2));
		for (int i = 0; i < viewData.getBrickData().length; i++)
		{
			for (int j = 0; j < viewData.getBrickData()[i].length; j++)
			{
				setRectangleData(viewData.getBrickData()[i][j], rectangles[i][j]);
			}
		}
		
		generatePreviewPanel(viewData.getNextBrickData());
		refreshShadow(viewData);
	}
	
	public void setEventListener(InputEventListener eventListener) {
		this.eventListener = eventListener;
	}
	
	public Paint getFillColor(int i) {
		Paint returnPaint;
		switch (i) {
		case 0: 
			returnPaint = Color.TRANSPARENT;
			break;
		case 1:
			returnPaint = Color.AQUA;
		    break;
		case 2:
			returnPaint = Color.BLUEVIOLET;
			break;
		case 3:
			returnPaint = Color.DARKGREEN;
			break;
		case 4:
			returnPaint = Color.YELLOW;
			break;
		case 5:
			returnPaint = Color.RED;
			break;
		case 6:
			returnPaint = Color.BEIGE;
			break;
		case 7: 
			returnPaint = Color.BURLYWOOD;
			break;
		default:
			returnPaint = Color.WHITE;
			break;
		}
		return returnPaint;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gamePanel.setFocusTraversable(true);
		gamePanel.requestFocus();
		gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (paused.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE)
				{
					if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) 
					{
						refreshBrick(eventListener.onRotateEvent());
						event.consume();
					}			
					if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S)
					{
						moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
						event.consume();
					}
					if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A)
					{
						refreshBrick(eventListener.onLeftEvent());
						event.consume();
					}
					if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D)
					{
						refreshBrick(eventListener.onRightEvent());
						event.consume();
					}
					if (event.getCode() == KeyCode.SPACE)
					{
						for (int i = 0; i < 25; i++)
						{
							moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
							boolean canMove = eventListener.getCanMove();
							if (!canMove)
								break;
						}
						event.consume();
					}
					if (event.getCode() == KeyCode.C)
					{
						savedBrickMatrix = eventListener.saveCurrentBrick(savedBrickMatrix);
						generateSavedBrick(savedBrickMatrix.getBrickMatrix().get(0));
						event.consume();
					}
				}
				if (event.getCode() == KeyCode.P) 
				{
					pauseButton.selectedProperty().setValue(!pauseButton.selectedProperty().getValue());
				}
				if (event.getCode() == KeyCode.N)
				{
					timeLine.pause();
					AlertBox.show(GuiController.this);
				}
			}			
		});
		
		gameOverPanel.setVisible(false);
		
		pauseButton.selectedProperty().bindBidirectional(paused);
		pauseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue)
				{
					timeLine.pause();
					pauseButton.setText("Resume");
				}
				else 
				{
					timeLine.play();
					pauseButton.setText("Pause");
					gamePanel.requestFocus();
				}
			}			
		});
		
		Reflection reflection = new Reflection();
		reflection.setFraction(0.8);
		reflection.setTopOpacity(0.9);
		reflection.setTopOffset(-12);
		scoreValue.setEffect(reflection);
	}
	
	public void gameOver() {
		timeLine.stop();
		gameOverPanel.setVisible(true);
		isGameOver.setValue(Boolean.TRUE);
		System.out.println("Sike Nigga Game Over");
	}
	
	public void newGame() {
		eventListener.saveHighScore();
		try {
			Scene scene = tetris.startUp();
			tetris.mainStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
	    }
	}
	
	public void cancelNewGame() {
		timeLine.play();
	}
}
