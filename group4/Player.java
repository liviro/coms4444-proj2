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
	boolean didSetup = false;
	
	///////////////////////// SETUP /////////////////////////
	public void init() {
		
	}
	
	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		if (!didSetup) {
			board = new Board(size, grid);
			didSetup = true;
		}
		
		// An element in the history ArrayList is itself an ArrayList where the first element is the player id and the second is a movePair (each of which must be cast)
		// Keep our board up to date by processing the most recent moves made by opponent (faster than copying the whole grid again)
		int i = history.size() - 1;
		while (i >= 0 && (int) history.get(i).get(0) != id) {
			int player = (int) history.get(i).get(0);
			movePair mp = (movePair) history.get(i).get(1);
			board.processMove(mp, player);
			i--;
		}
		
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
						movepr.src = grid[validMoves.get(0).x*size + validMoves.get(0).y];
						movepr.target = grid[x*size + y];
						board.processMove(movepr, id);
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
		board.processMove(movepr, id);
		return movepr;
	}
}