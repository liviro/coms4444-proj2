package offset.group4;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;
import offset.group4.Board;
import offset.group4.Move;
import offset.group4.MoveSequence;

public class Player extends offset.sim.Player {
	private int size = 32;
	
	public Player(Pair prin, int idin) {
		super(prin, idin);
	}
	
	///////////////////////// PRIVATE CLASSES /////////////////////////

	
	///////////////////////// PRIVATE VARIABLES /////////////////////////
	Board board;
	boolean didSetup = false;
	int idOpponent;
	Pair pairSelf;
	Pair pairOpponent;
	
	///////////////////////// SETUP /////////////////////////
	public void init() {
		
	}
	
	///////////////////////// STRATEGY: CORNERS /////////////////////////
	// Choose move based on shortest Manhattan distance from the corners
	private Move cornerStrategy() {
		for (int d = 0; d < size; d++) {
			for (int n = 0; n <= d; n++) {
				int x = d - n;
				int y = n;
				
				for (int c = 0; c < 4; c++) {					
					ArrayList<Coord> validMoves = board.validMovesFrom(x, y, pairSelf);
					
					// Return any valid moves from that point
					if (!validMoves.isEmpty())
						return new Move(validMoves.get(0).x, validMoves.get(0).y, x, y, id);
					
					// Rotate to the next corner
					int temp = y;
					y = x;
					x = size - temp - 1;
				}
			}
		}
		
		return null;
	}
	
	///////////////////////// STRATEGY: RECURSIVE MOVE SEARCH /////////////////////////
	private Move searchStrategy() {
		MoveSequenceAnalysis analysisSelf = new MoveSequenceAnalysis(board);
		MoveSequenceAnalysis analysisOpponent = new MoveSequenceAnalysis(board);
		
		analysisSelf.analyze(id, pairSelf);
		analysisOpponent.analyze(idOpponent, pairOpponent);
		
		analysisSelf.prune(pairOpponent);
		
		/*
		 * TODO: Consider all possible moves in light of the analyses and choose the one with the highest value according to our valuation methodology
		 */
		
		
		Move move = null;
		double maxCoinsPerMove = 0;
		
		// If we can't capture neutral or opponent coins, just make some valid move
		if (move == null) {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					ArrayList<Coord> validMoves = board.validMovesFrom(x, y, pairSelf);
					
					if (!validMoves.isEmpty()) {
						move = new Move(x, y, validMoves.get(0).x, validMoves.get(0).y, id);
					}
				}
			}
		}
		
		return move;
	}
	
	
	///////////////////////// IMPLEMENT ABSTRACT CLASS MOVE METHOD /////////////////////////	
	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		// Setup our board class if this is the first time we have been called
		if (!didSetup) {
			board = new Board(size, grid);
			pairSelf = pr;
			pairOpponent = pr0;
			idOpponent = 1 - id;
			didSetup = true;
		} else {
			// Keep our board up to date by processing the most recent moves made by opponent (probably faster than copying the whole grid again)
			// An element in the history ArrayList is itself an ArrayList where the first element is the player id and the second is a movePair (each of which must be cast)
			int i = history.size() - 1;
			while (i >= 0 && (int) history.get(i).get(0) != id) {
				int player = (int) history.get(i).get(0);
				movePair mp = (movePair) history.get(i).get(1);
				if (mp.move)
					board.processMove(new Move(mp.src.x, mp.src.y, mp.target.x, mp.target.y, player));
				
				i--;
			}
		}
		
		// Call a strategy to actually determine the move to make
		Move move = searchStrategy();

		// Transform the resulting move from our representation to the simulator representation
		movePair movepr = new movePair();
		if (move != null) {
			board.processMove(move);
			
			movepr.move = true;
			movepr.src = grid[move.src.x*size + move.src.y];
			movepr.target = grid[move.target.x*size + move.target.y];
			return movepr;
		} else {
			movepr.move = false;
			return movepr;
		}
	}
}