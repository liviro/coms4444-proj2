package offset.dumb2;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;



public class Player extends offset.sim.Player {
	private int size = 32;
	// sunyun 
	public int c = 0;
	public Player(Pair prin, int idin) {
		super(prin, idin);
	}
	
	///////////////////////// PRIVATE METHODS /////////////////////////
	// Light wrapper class for the board to make it easier to refer to specific locations
	private class Board {
		Point grid[];
		
		Board() {}
		
		Board(Point grid[]) {
			this.grid = grid;
		}
		
		public void update(Point grid[]) {
			this.grid = grid;
		}
		
		public Point get(int x, int y) {
			return grid[x*size + y];
		}
		
		public Point get(Point p) {
			return this.get(p.x, p.y);
		}
	}
	
	///////////////////////// PRIVATE VARIABLES /////////////////////////
	Board board;
	boolean didInit = false;
	
	///////////////////////// SETUP /////////////////////////
	public void init() {
		board = new Board();
	}
	
	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		if (!didInit)
			init();
		
		// Update the board with the latest grid.  Optimize later if need be based on history
		board.update(grid);
		
		movePair movepr = new movePair();
		
		// Choose move based on shortest Manhattan distance from the corners
	
		for (int d = 0; d <= size/2; d++) {
			for(int i = d; i <= size/2; i++)
			{
				int x 	= 0;
				int y 	= 0;
				Point p = new Point();
				
				switch(c)
				{
				case 0:
					x = i;
					y = d;
					break;
				case 1:
					x = i;
					y = size - d;
					break;
				
				case 3:
					y = i;
					x = size - d;
					break;
				case 4:
					x = size - i;
					y = d;
					break;
				case 5:
					x = size - i;
					y = size - d;
					break;
				case 6:
					y = size - i;
					x = d;
					break;
				case 7:
					y = size - i;
					x = size - d;
					break;
					
				}

					p.x = x;
					p.y = y;
					c 	= (c + 1) % 8;
					ArrayList<Point> validMoves = validMovesFrom(p);
					
					// Return any valid moves from that point
					if (!validMoves.isEmpty()) 
					{
						movepr.move = true;
						movepr.src = board.get(validMoves.get(0));
						movepr.target = board.get(p);
						
						return movepr;
					
					}	
				
			}
		}

		// Could not find a valid move
		movepr.move = false;
		return movepr;
	}

	///////////////////////// HELPER METHODS /////////////////////////
	
	// Given a point, returns an ArrayList of points that can be combined with it
	// A point can validly be combined if it is at the proper offset for the player and has the same value
	private ArrayList<Point> validMovesFrom(Point from) {
		ArrayList<Point> validMoves = new ArrayList<Point>();
		int p;
		int q;
		
		if (from.value == 0)
			return validMoves;
			
		for(int i = -1; i <= 1; i += 2) {
			for(int j = -1; j <= 1; j += 2) {
				for(int k = 0; k <= 1; k++) {
					Point to = new Point();
					p = k == 0 ? pr.p : pr.q;
					q = k == 0 ? pr.q : pr.p;
					
					to.x = from.x + p*i;
					to.y = from.y + q*j;
					if (isMoveValid(from, to))
						validMoves.add(to);
				}				
			}
		}

		return validMoves;
	}
	
	private boolean isMoveValid(Point src, Point target) {
		return (isInBounds(src) && isInBounds(target) &&
				((Math.abs(src.x - target.x) == pr.p && Math.abs(src.y - target.y) == pr.q) || (Math.abs(src.x - target.x) == pr.q && Math.abs(src.y - target.y) == pr.p)) &&
				board.get(src).value == board.get(target).value && board.get(src).value != 0);
	}
	
	private boolean isMoveValid(movePair mp) {
		return isMoveValid(mp.src, mp.target);
	}
	
	private boolean isInBounds(Point point) {
		return (point.x >= 0 && point.x < size && point.y >= 0 && point.y < size);
	}
	
	private int distBetween(Point p1, Point p2) {
		return (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y));
	}
	
	private double distFromCenter(Point p1) {
		return (Math.abs(p1.x - (double) size/2) + Math.abs(p1.y - (double) size/2));
	}
}