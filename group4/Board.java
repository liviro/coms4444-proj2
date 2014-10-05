package offset.group4;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.group4.Coord;
import offset.group4.Move;

public class Board {
	ArrayList<Point> grid;
	int[] gridNew;
	int size;
	
	// CONSTRUCTORS
	Board(int size) {
		this.size = size;
		this.gridNew = new int[size*size];
	}
	
	Board(int size, Point gridIn[]) {
		this.size = size;
		
		// old
		this.grid = new ArrayList<Point>();
		this.setGrid(gridIn);
		
		// new
		this.gridNew = new int[size*size];
		for(int i = 0; i < size*size; i++) {
			gridNew[i] = encodePoint(gridIn[i]);
		}
	}
	
	Board(Board board) {
		this.size = board.size;
		
		// old
		this.grid = new ArrayList<Point>();
		for (int i = 0; i < board.grid.size(); i++) {
			this.grid.add(new Point(board.grid.get(i)));
		}
		
		// new
		this.gridNew = new int[this.size*this.size];
		for(int j = 0; j < board.gridNew.length; j++) {
			this.gridNew[j] = board.gridNew[j];
		}
		
	}
	
	// PRIVATE METHODS
	private int encodePoint(Point pt) {
		// need to make owner++, since it can be -1.
		int enc = pt.x * (int) Math.pow(10, 8) 
				+ pt.y * (int) Math.pow(10, 6)
				+ pt.value * (int) Math.pow(10, 2)
				+ (pt.owner + 1) * (int) Math.pow(10, 1)
				+ (pt.change ? 1 : 0);
		return enc;
	}
	
	private Point decodePoint(int enc) {
		Point dec = new Point();
		dec.x = getX(enc);
		dec.y = getY(enc);
		dec.value = getVal(enc);
		dec.owner = getOwner(enc);
		dec.change = getChange(enc);
		return dec;
	}
	
	private int getX(int enc) {
		return enc / (int) Math.pow(10, 8);
	}

	private int getY(int enc) {
		return (enc % (int) Math.pow(10, 8)) / (int) Math.pow(10, 6);
	}

	private int getVal(int enc) {
		return (enc % (int) Math.pow(10, 6)) / (int) Math.pow(10, 2);
	}

	private int getOwner(int enc) {
		return ((enc % (int) Math.pow(10, 2)) / (int) Math.pow(10, 1)) -1;
	}

	private boolean getChange(int enc) {
		return (enc % 10 == 1 ? true : false);
	}

	private int setX(int enc, int xIn) {
		return xIn * (int) Math.pow(10, 8) + (enc % (int) Math.pow(10, 8));
	}

	private int setY(int enc, int yIn) {
		return (enc / (int) Math.pow(10, 8)) + yIn * (int) Math.pow(10, 6) + (enc % (int) Math.pow(10, 6));
	}

	private int setVal(int enc, int valIn) {
		return (enc / (int) Math.pow(10, 6)) + valIn * (int) Math.pow(10, 2) + (enc % (int) Math.pow(10, 2));
	}

	private int setOwner(int enc, int ownerIn) {
		return (enc / (int) Math.pow(10, 2)) + (ownerIn+1) * (int) Math.pow(10, 1) + (enc % (int) Math.pow(10, 1));
	}

	private int setChange(int enc, boolean changeIn) {
		return (enc / (int) Math.pow(10, 1)) + (changeIn ? 1 : 0);
	}

	private ArrayList<Point> getBoard() {
		ArrayList<Point> decoded = new ArrayList<Point>();
		for(int i = 0; i < gridNew.length; i++) {
			decoded.add( decodePoint(gridNew[i]) );
		}
		return decoded;
	}
	
	// get the index in grid* array from the x, y coords
	private int getIndex(int x, int y) {
		return x*size + y;
	}
	
	
	// PUBLIC METHODS
	public ArrayList<Point> get() {
		return getBoard();
	}
	
	public void setGrid(Point gridIn[]) {
		// old
		for (int i = 0; i < gridIn.length; i++) {
			this.grid.add(new Point(gridIn[i]));
		}
		
		// new
		this.gridNew = new int[this.size*this.size];
		for(int i = 0; i < this.size*this.size; i++) {
			this.gridNew[i] = encodePoint(new Point(gridIn[i]));
		}
		
	}
	
	// Updates the grid based on a move performed by a player
	// Assumes that the move is valid
	public void processMove(int xSrc, int ySrc, int xTarget, int yTarget, int playerId) {
		int srcIdx = getIndex(xSrc, ySrc);
		int tgtIdx = getIndex(xTarget, yTarget);
		
		grid.get(xSrc*size + ySrc).value = 0;
		grid.get(xSrc*size + ySrc).owner = -1;
		gridNew[srcIdx] = setOwner(setVal(gridNew[srcIdx], 0), -1);

		grid.get(xTarget*size + yTarget).value *= 2;
		grid.get(xTarget*size + yTarget).owner = playerId;
		gridNew[tgtIdx] = setOwner(setVal(gridNew[tgtIdx], 2*getVal(gridNew[tgtIdx])), playerId);
	}
	
	public void processMove(Coord src, Coord target, int player) {
		processMove(src.x, src.y, target.x, target.y, player);
	}
	
	public void processMove(Move move) {
		processMove(move.src, move.target, move.playerId);
	}
	
	public Point getPoint(int x, int y) {
		return new Point(grid.get(x*size + y));		// Return a copy so user cannot modify the board
	}
	
	public Point getPoint(Coord c) {
		return getPoint(c.x, c.y);
	}
	
	// Given a point and an offset pair, returns an ArrayList of neighbors of that point that are on the board
	public ArrayList<Coord> neighborsOf(int x, int y, Pair pr) {
		ArrayList<Coord> neighbors = new ArrayList<Coord>();
		int p;
		int q;

		for (int k = 0; k <= 1; k++) {
			p = k == 0 ? pr.p : pr.q;
			q = k == 0 ? pr.q : pr.p;

			for(int i = -1; i <= 1; i += 2) {
				for(int j = -1; j <= 1; j += 2) {
					if (isInBounds(x + p*i, y + q*j))
						neighbors.add(new Coord(x + p*i, y + q*j));
				}
			}
		}

		return neighbors;
	}
	
	public ArrayList<Coord> neighborsOf(Coord c, Pair pr) {
		return neighborsOf(c.x, c.y, pr);
	}
	
	public ArrayList<Coord> validMovesFrom(int x, int y, Pair pr) {
		ArrayList<Coord> neighbors = neighborsOf(x, y, pr);
		ArrayList<Coord> validMoves = new ArrayList<Coord>();
		
		for (Coord c : neighbors) {
			if (isMoveValid(c.x, c.y, x, y, pr))
				validMoves.add(new Coord(c.x, c.y));
		}
		
		return validMoves;
	}

	public ArrayList<Coord> validMovesFrom(Coord c, Pair pr) {
		return validMovesFrom(c.x, c.y, pr);
	}
	
	public boolean isInBounds(int x, int y) {
		return (x >= 0 && x < this.size && y >= 0 && y < this.size);
	}
	
	public boolean isInBounds(Coord c) {
		return isInBounds(c.x, c.y);
	}
	
	public boolean isMoveValid(int x1, int y1, int x2, int y2, Pair pr) {
		return (isInBounds(x1, y1) && isInBounds(x2, y2) && 
				((Math.abs(x1 - x2) == pr.p && Math.abs(y1 - y2) == pr.q) || (Math.abs(x1 - x2) == pr.q && Math.abs(y1 - y2) == pr.p)) &&
				getPoint(x1, y1).value == getPoint(x2, y2).value &&
				getPoint(x1, y1).value > 0);
	}
	
	public boolean isMoveValid(Coord src, Coord target, Pair pr) {
		return isMoveValid(src.x, src.y, target.x, target.y, pr);
	}
	
	public boolean isMoveValid(Move move, Pair pr) {
		return isMoveValid(move.src, move.target, pr);
	}
	
	public int distBetween(int x1, int y1, int x2, int y2) {
		return (Math.abs(x1 - x2) + Math.abs(y1 - y2));
	}
	
	public int distBetween(Coord c1, Coord c2) {
		return distBetween(c1.x, c1.y, c2.x, c2.y);
	}
		
	public double distFromCenter(int x, int y) {
		return (Math.abs(x - (double) size/2) + Math.abs(y - (double) size/2));
	}
	
	public double distFromCenter(Coord c) {
		return distFromCenter(c.x, c.y);
	}
	
	@Override public String toString() {
		String str = "";
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++)
				str = str + getPoint(x,y).value + " ";
				
			str = str + "\n";
		}
		
		return str;
	}
}
