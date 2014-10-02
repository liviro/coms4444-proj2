package offset.group4;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;
import offset.group4.Board;

public class Player extends offset.sim.Player {
	private int size = 32;
	
	public Player(Pair prin, int idin) {
		super(prin, idin);
	}
	
	///////////////////////// PRIVATE CLASSES /////////////////////////

	
	///////////////////////// PRIVATE VARIABLES /////////////////////////
	Board board;
	boolean didInit = false;
	
	///////////////////////// SETUP /////////////////////////
	public void init() {
		board = new Board(size);
	}
	
	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		if (!didInit)
			init();
		
		// Update the board with the latest grid.  Optimize later if need be based on history
		board.setGrid(grid);
		
		movePair movepr = new movePair();
		
		// Choose move based on shortest Manhattan distance from the corners
		for (int d = 0; d < size; d++) {
			for (int n = 0; n <= d; n++) {
				int x = d - n;
				int y = n;
				
				for (int c = 0; c < 4; c++) {					
					ArrayList<Point> validMoves = board.validMovesFrom(x, y, pr);
					
					// Return any valid moves from that point
					if (!validMoves.isEmpty()) {
						movepr.move = true;
						movepr.src = board.get(validMoves.get(0).x, validMoves.get(0).y);
						movepr.target = board.get(x, y);
						
						return movepr;
					}
					
					// Rotate to the next corner
					int temp = y;
					y = x;
					x = size - temp - 1;
				}
			}
		}
		
		// Could not find a valid move
		movepr.move = false;
		return movepr;
	}
}