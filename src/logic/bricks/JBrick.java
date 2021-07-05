package logic.bricks;

import java.util.*;

public class JBrick implements Brick{
    private final List<int[][]> brickMatrix = new ArrayList<>();
    
    public JBrick() {
    	brickMatrix.add(new int[][] {
    		{0, 0, 0, 0},
    		{3, 3, 3, 0}, 
    		{0, 0, 3, 0},
    		{0, 0, 0, 0}
    	});
    	
    	brickMatrix.add(new int[][] {
    		{0, 0, 0, 0},
    		{0, 3, 3, 0}, 
    		{0, 3, 0, 0},
    		{0, 3, 0, 0}
    	});
    	
    	brickMatrix.add(new int[][] {
    		{0, 0, 0, 0},
    		{0, 3, 0, 0}, 
    		{0, 3, 3, 3},
    		{0, 0, 0, 0}
    	});
    	
    	brickMatrix.add(new int[][] {
    		{0, 0, 3, 0},
    		{0, 0, 3, 0}, 
    		{0, 3, 3, 0},
    		{0, 0, 0, 0}
    	});
    }

	public List<int[][]> getBrickMatrix() {
		return brickMatrix;
	}
}
