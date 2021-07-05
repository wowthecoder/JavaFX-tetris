package logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ClearRow {
	private int scoreBonus;
    private int linesRemoved;
    private int[][] nextMatrix;
    private IntegerProperty linesRemovedProperty = new SimpleIntegerProperty(0);
    public ClearRow(int linesRemoved, int[][] nextMatrix, int scoreBonus) {
    	this.linesRemoved = linesRemoved;
    	this.nextMatrix = nextMatrix;
    	this.scoreBonus = scoreBonus;
    }
	public int getLinesRemoved() {
		return linesRemoved;
	}
	public int[][] getNextMatrix() {
		return nextMatrix;
	}
    public int getScoreBonus() {
    	return scoreBonus;
    }
    public IntegerProperty linesProperty() {
    	return linesRemovedProperty;
    }
    public void setLinesRemoved(int x) {
    	linesRemoved = x;
    	linesRemovedProperty.setValue(linesRemovedProperty.getValue() + x);
    }
    public void setScoreBonus(int y) {
    	scoreBonus = y;
    }
    public void setNextMatrix(int[][] matrix) {
    	nextMatrix = matrix;
    }
}
