package logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Score {
    private IntegerProperty score = new SimpleIntegerProperty(0);
    
    public IntegerProperty scoreProperty() {
    	return score;
    }
    
    public void add(int i) {
    	score.setValue(score.getValue() + i);
    }
}
