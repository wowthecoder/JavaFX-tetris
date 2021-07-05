package logic.bricks;

import java.util.*;

public class OBrick implements Brick{
    private final List<int[][]> brickMatrix = new ArrayList<>();
    
    public OBrick() {
    	brickMatrix.add(new int[][] {
    		{0, 0, 0, 0},
    		{0, 1, 1, 0}, 
    		{0, 1, 1, 0},
    		{0, 0, 0, 0}
    	});
    }

	public List<int[][]> getBrickMatrix() {
		return brickMatrix;
	}
    
}
