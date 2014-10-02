package offset.group4;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

public class Board {
	Point grid[];
	int size;
	
	// CONSTRUCTORS
	Board(int size) {
		this.size = size;
	}
	
	Board(int size, Point grid[]) {
		this.size = size;
		this.grid = grid.clone();
	}
	
	Board(Board board) {
		this.size = board.size;
		this.grid = grid.clone();
	}
	
	// PRIVATE METHODS
	
	
	// PUBLIC METHODS
	public void setGrid(Point grid[]) {
		this.grid = grid;
	}
	
	public void processMove(movePair mp) {
		// to be completed
	}
	
	public Point get(int x, int y) {
		return grid[x*size + y];
	}
	
	// Given a point and an offset pair, returns an ArrayList of neighbors of that point that are on the board
	public ArrayList<Point> neighborsOf(int x, int y, Pair pr) {
		ArrayList<Point> neighbors = new ArrayList<Point>();
		int p;
		int q;

		for (int k = 0; k <= 1; k++) {
			p = k == 0 ? pr.p : pr.q;
			q = k == 0 ? pr.q : pr.p;

			for(int i = -1; i <= 1; i += 2) {
				for(int j = -1; j <= 1; j += 2) {
					Point n = new Point();
					
					n.x = x + p*i;
					n.y = y + q*j;
					
					if (isInBounds(n))
						neighbors.add(n);
				}
			}
		}

		return neighbors;
	}
	
	public ArrayList<Point> neighborsOf(Point p, Pair pr) {
		return neighborsOf(p.x, p.y, pr);
	}
	
	public ArrayList<Point> validMovesFrom(int x, int y, Pair pr) {
		ArrayList<Point> neighbors = neighborsOf(x, y, pr);
		ArrayList<Point> validMoves = new ArrayList<Point>();
		
		for (Point p : neighbors) {
			if (isMoveValid(p.x, p.y, x, y, pr))
				validMoves.add(p);
		}
		
		return validMoves;
	}
	
	public ArrayList<Point> validMovesFrom(Point p, Pair pr) {
		return validMovesFrom(p.x, p.y, pr);
	}
	
	public boolean isInBounds(int x, int y) {
		return (x >= 0 && x < this.size && y >= 0 && y < this.size);
	}
	
	public boolean isInBounds(Point p) {
		return isInBounds(p.x, p.y);
	}
	
	public boolean isMoveValid(int x1, int y1, int x2, int y2, Pair pr) {
		return (isInBounds(x1, y1) && isInBounds(x2, y2) && 
				((Math.abs(x1 - x2) == pr.p && Math.abs(y1 - y2) == pr.q) || (Math.abs(x1 - x2) == pr.q && Math.abs(y1 - y2) == pr.p)) &&
				get(x1, y1).value == get(x2, y2).value &&
				get(x1, y1).value > 0);
	}
	
	public boolean isMoveValid(Point src, Point target, Pair pr) {
		return isMoveValid(src.x, src.y, target.x, target.y, pr);
	}
	
	public boolean isMoveValid(movePair mp, Pair pr) {
		return isMoveValid(mp.src, mp.target, pr);
	}
	
	public int distBetween(int x1, int y1, int x2, int y2) {
		return (Math.abs(x1 - x2) + Math.abs(y1 - y2));
	}
	
	public int distBetween(Point p1, Point p2) {
		return distBetween(p1.x, p1.y, p2.x, p2.y);
	}
	
	public double distFromCenter(int x, int y) {
		return (Math.abs(x - (double) size/2) + Math.abs(y - (double) size/2));
	}
	
	public double distFromCenter(Point p) {
		return distFromCenter(p.x, p.y);
	}
}
