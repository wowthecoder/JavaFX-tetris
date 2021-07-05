package logic;

import logic.bricks.*;
import java.awt.*;
import java.io.*;

public class SimpleBoard {
    private final int width;
    private final int height;
    private int[][] currentGameMatrix;
    private final RandomBrickGenerator brickGenerator;
    private Brick brick;
    private int currentShape = 0;
    private Point currentOffset;
    private Score score;
    private ClearRow clearRow;
    private int nextLevel = 10;
    private boolean LevelUp;
    private int HighScore;
    
    public SimpleBoard(int width, int height) {
    	this.width = width;
    	this.height = height;
    	currentGameMatrix = new int[width][height];
    	brickGenerator = new RandomBrickGenerator();
    	score = new Score();
    	clearRow = new ClearRow(0, null, 0);
    }
    
    public boolean createNewBrick() {
    	currentShape = 0;
    	Brick currentBrick = brickGenerator.getBrick();
    	setBrick(currentBrick);
    	currentOffset = new Point(4, 0);
    	
    	return MatrixOperations.intersects(currentGameMatrix, getCurrentShape(), currentOffset.x, currentOffset.y);
    }
    
    public boolean createNewBrick(Brick brickMatrix) {
    	currentShape = 0;
    	setBrick(brickMatrix);
    	currentOffset = new Point(4, 0);
    	
    	return MatrixOperations.intersects(currentGameMatrix, getCurrentShape(), currentOffset.x, currentOffset.y);
    }
    
    public boolean moveBrickDown() {
    	Point p = new Point(currentOffset);
    	p.translate(0, 1);
    	currentOffset = p;
    	boolean conflict = MatrixOperations.intersects(currentGameMatrix, getCurrentShape(), p.x, p.y);
    	if (conflict)
    		return false;
    	else
    	{
    		currentOffset = p;
    	    return true;
    	}
    }
    
    public boolean onShadowDown(Point shadowPos) {
    	Point p = new Point(shadowPos);
		p.translate(0, 1);
		boolean conflict = MatrixOperations.intersects(currentGameMatrix, getCurrentShape(), p.x, p.y);
    	if (conflict)
    		return false;
    	else
    	{
    		shadowPos = p;
    	    return true;
    	}
    }
    
    public boolean moveBrickLeft() {
		Point p = new Point(currentOffset);
		p.translate(-1, 0);

		boolean conflict = MatrixOperations.intersects(currentGameMatrix, getCurrentShape(), p.x, p.y);
		if (conflict)
			return false;
		else {
			currentOffset = p; 
			return true;
		}
	}
    
    public boolean moveBrickRight() {
    	Point p = new Point(currentOffset);
    	p.translate(1, 0);
    	
    	boolean conflict = MatrixOperations.intersects(currentGameMatrix, getCurrentShape(), p.x, p.y);
    	if (conflict)
    		return false;
    	else {
    		currentOffset = p;
    		return true;
    	}
    }
    
    public ViewData getViewData() {
    	return new ViewData(getCurrentShape(), currentOffset.x, currentOffset.y, brickGenerator.getNextBrick().getBrickMatrix().get(0));
    }
    
    public int[][] getCurrentShape() {
    	return this.brick.getBrickMatrix().get(currentShape);
    }
    
    public Score getScore() {
    	return score;
    }

    public void setBrick(Brick brick) {
    	this.brick = brick;
    	currentOffset = new Point(4, 0);
    }
    
    public Brick getBrick() {
    	return this.brick;
    }
    
	public int[][] getBoardMatrix() {
		return currentGameMatrix;
	}
	
	public void mergeBrickToBackground() {
		currentGameMatrix = MatrixOperations.merge(currentGameMatrix, getCurrentShape(), currentOffset.x, currentOffset.y);
	}

	public NextShapeInfo getNextShape() {
		int nextShape = currentShape;
		nextShape = ++nextShape % brick.getBrickMatrix().size();
		return new NextShapeInfo(brick.getBrickMatrix().get(nextShape), nextShape);
	}
	
	public boolean rotateBrickLeft() {
		NextShapeInfo nextShape = getNextShape();
		boolean conflict = MatrixOperations.intersects(currentGameMatrix, nextShape.getShape(), currentOffset.x, currentOffset.y);
		if (conflict)
			return false;
		else 
		{
			setCurrentShape(nextShape.getPosition());
			return true;
		}
	}
	
	public void setCurrentShape(int currentShape) {
		this.currentShape = currentShape;
	}

	public ClearRow clearRows() {
		ClearRow thisRow = MatrixOperations.checkRemoving(currentGameMatrix);
		clearRow.setLinesRemoved(thisRow.getLinesRemoved());
		clearRow.setScoreBonus(thisRow.getScoreBonus());
		clearRow.setNextMatrix(thisRow.getNextMatrix());
		currentGameMatrix = clearRow.getNextMatrix();
		if (clearRow.linesProperty().getValue() >= nextLevel)
		{
			LevelUp = true;
			nextLevel += 10;
		}
		return clearRow;
	}
	
	public boolean getLevelUp() {
		return LevelUp;
	}
	
	public void setLevelUp(boolean levelup) {
		this.LevelUp = levelup;
	}
	
	public void loadHighScore() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("data\\SomeStuff.txt"));
			String score = br.readLine().split("/&/")[1];
			br.close();
			HighScore = Integer.parseInt(score);
		} catch (FileNotFoundException e) {
			HighScore = 0;
		} catch (IOException e) {}
	}
	
	public int getHighScore() {
		return HighScore;
	}
	
	public void saveHighScore() {
		if (score.scoreProperty().getValue() > HighScore)
		{
			try {
				File file = new File("data\\SomeStuff.txt");
			    FileWriter writer = new FileWriter(file);
			    writer.write("Bulala/&/" + score.scoreProperty().getValue() + "/&/" + (Math.random() * 1500 + 1));
			    writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
