package logic.bricks;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator {
    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    
    public RandomBrickGenerator() {
    	brickList = new ArrayList<Brick>();
    	brickList.add(new IBrick());
    	brickList.add(new JBrick());
    	brickList.add(new LBrick());
    	brickList.add(new OBrick());
    	brickList.add(new SBrick());
    	brickList.add(new TBrick());
    	brickList.add(new ZBrick());
    	
    	nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    	nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }
    
    public Brick getNextBrick() {
    	return nextBricks.peek();
    }
    
    public Brick getBrick() {
    	if (nextBricks.size() <= 1) 
    		nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    	return nextBricks.poll();
    }
}
