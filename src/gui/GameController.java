package gui;

import java.awt.Point;

import logic.ClearRow;
import logic.DownData;
import logic.SimpleBoard;
import logic.ViewData;
import logic.bricks.Brick;
import logic.events.*;

public class GameController implements InputEventListener {
	private SimpleBoard board = new SimpleBoard(25, 10);
    private final GuiController viewController;
    private boolean canMove;
    
    public GameController(GuiController c) {
    	this.viewController = c;
    	this.viewController.setEventListener(this);
    	board.createNewBrick();
    	board.loadHighScore();
    	this.viewController.initGameView(board.getBoardMatrix(), board.getViewData());
    	this.viewController.bindScore(board.getScore().scoreProperty(), board.clearRows().linesProperty(), board.getHighScore());
    }
    
    @Override
    public DownData onDownEvent(MoveEvent event) {
    	canMove = board.moveBrickDown();
    	ClearRow clearRow = null;
    	if (!canMove) 
    	{
    		board.mergeBrickToBackground();
    		clearRow = board.clearRows();
    		if (clearRow.getLinesRemoved() > 0)
    		{
    			board.getScore().add(clearRow.getScoreBonus());
    			if (board.getLevelUp())
    			{
    				viewController.goFaster();
    				board.setLevelUp(false);
    			}
    		}
    		if (board.createNewBrick())
    		{
    			viewController.gameOver();
    			board.saveHighScore();
    		}
    	} else {
    		if (event.getEventSource() == EventSource.USER)
    			board.getScore().add(1);
    	}
    	viewController.refreshGameBackground(board.getBoardMatrix());
    	return new DownData(clearRow, board.getViewData());
    }
    
    public void saveHighScore() {
    	board.saveHighScore();
    }
    
    @Override
    public boolean onShadowDown(Point shadowPos) {
    	boolean conflict = board.onShadowDown(shadowPos);
    	return conflict;
    }
    
    @Override
    public boolean getCanMove() {
    	return canMove;
    }
    
    @Override
    public ViewData onLeftEvent() {
    	board.moveBrickLeft();
    	return board.getViewData();
    }
    
    @Override 
    public ViewData onRightEvent() {
    	board.moveBrickRight();
    	return board.getViewData();
    }
    
    @Override 
    public ViewData onRotateEvent() {
    	board.rotateBrickLeft();
    	return board.getViewData();
    }
    
    @Override
    public Brick saveCurrentBrick(Brick previousBrick) {
    	if (previousBrick == null)
    	{
    		Brick currentBlock= board.getBrick();
    		if (board.createNewBrick())
    			viewController.gameOver();
    		return currentBlock;
    	}
    	else
    	{
    		Brick currentBrick = board.getBrick();
    		if (board.createNewBrick(previousBrick))
    			viewController.gameOver();
    		return currentBrick;
    	}
    }
}
