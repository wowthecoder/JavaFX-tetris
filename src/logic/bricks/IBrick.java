package logic.bricks;

import java.util.*;

public class IBrick implements Brick{

	private final List<int[][]> brickMatrix = new ArrayList<>();
	
	public IBrick() {
    	brickMatrix.add(new int[][] {
    		{0, 0, 0, 0},
    		{2, 2, 2, 2}, 
    		{0, 0, 0, 0},
    		{0, 0, 0, 0}
    	});
    	
    	brickMatrix.add(new int[][] {
    		{0, 2, 0, 0},
    		{0, 2, 0, 0}, 
    		{0, 2, 0, 0},
    		{0, 2, 0, 0}
    	});
    }

	public List<int[][]> getBrickMatrix() {
		return brickMatrix;
	}
}
