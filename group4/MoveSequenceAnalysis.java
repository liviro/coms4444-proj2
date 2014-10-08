package offset.group4;


import java.util.*;

import offset.group4.Board;
import offset.group4.MoveSequence;
import offset.group4.Move;
import offset.group4.Coord;
import offset.sim.Pair;
import offset.sim.Point;

public class MoveSequenceAnalysis {
	private Board board;

	private ArrayList<MoveSequence> allMoveSequences;
	private HashMap<String, ArrayList<MoveSequence>> moveSequencesByStart;
	
	public MoveSequenceAnalysis(Board board) {
		this.board = new Board(board);
		this.allMoveSequences = new ArrayList<MoveSequence>();
		this.moveSequencesByStart = new HashMap<String, ArrayList<MoveSequence>>();
	}
	
	// Generate all of the sequences that double each cell's value, and store them
	public void analyze(int playerId, Pair pr) {
		for (int x = 0; x < board.size; x++) {
			for (int y = 0; y < board.size; y++) {
				ArrayList<MoveSequence> moveSequences = movesToDoubleValue(board, new Coord(x,y), pr, playerId);
				
				if (moveSequences != null) {
					for (MoveSequence moveSequence : moveSequences) {
						moveSequence.coinSwing = (moveSequence.board.scores[playerId] - board.scores[playerId]) - (moveSequence.board.scores[1-playerId] - board.scores[1-playerId]);
						
						Move firstMove = moveSequence.moves.get(0);

						ArrayList<MoveSequence> moveSequencesStartingAt = moveSequencesByStart.get(firstMove.toString());
						if (moveSequencesStartingAt == null)
							moveSequencesStartingAt = new ArrayList<MoveSequence>();
						
						moveSequencesStartingAt.add(moveSequence);
						moveSequencesByStart.put(firstMove.toString(), moveSequencesStartingAt);
						
						allMoveSequences.add(moveSequence);
					}
				}
			}
		}
	}
	
	public ArrayList<MoveSequence> getAllMoveSequences() {
		return allMoveSequences;
	}
	
	public ArrayList<MoveSequence> getAllDisruptibleMoveSequences(Move move, Pair pair) {
		ArrayList<MoveSequence> moveSequencesFiltered = new ArrayList<MoveSequence>();
		
		for (MoveSequence moveSequence : allMoveSequences) {
			if (moveSequence.isDisruptedBy(board, move))
				moveSequencesFiltered.add(moveSequence);
		}
		
		return moveSequencesFiltered;
	}
	
	public ArrayList<MoveSequence> getMoveSequencesByStart(Move move) {
		return moveSequencesByStart.get(move.toString());
	}
	
	public ArrayList<MoveSequence> getNonDisruptibleMoveSequencesByStart(Move move, Pair pairOpponent) {
		ArrayList<MoveSequence> moveSequences = getMoveSequencesByStart(move);
		ArrayList<MoveSequence> nonDisruptibleMoveSequences = new ArrayList<MoveSequence>();
		
		for (MoveSequence moveSequence : moveSequences)
			if (!moveSequence.isDisruptible(board, pairOpponent))
				nonDisruptibleMoveSequences.add(moveSequence);
		
		return moveSequences;
	}
	
	// Returns all sequences of moves such that, after the moves, the point at coordinate c has value value
	private ArrayList<MoveSequence> movesToMakeValue(Board board, Coord c, int value, Pair pair, int playerId) {
		Point p = board.getPoint(c);
		
		boolean debug = false;
		if (c.x == 5 && c.y == 4 && playerId == 1)
			debug = true;
		//debug = false;
		
		if (p.value == value) {
			MoveSequence moveSequence = new MoveSequence(board, pair);
			ArrayList<MoveSequence> moveSequences = new ArrayList<MoveSequence>();
			moveSequences.add(moveSequence);			
			return moveSequences;						// No moves needed, so sequences contains a single sequence with no moves
		} else if (p.value == 0 || p.value > value) {
			return null;								// Impossible to get p to have value
		} else {
			if (debug)
				System.out.printf("Trying to make %s have value %d from %d\n", c, value, board.getPoint(c).value);
			
			ArrayList<MoveSequence> moveSequences = new ArrayList<MoveSequence>();
			int currentValue = p.value;
			
			while (currentValue < value) {
				if (debug)
					System.out.printf("  STEP: make %s double its current value %d\n", c, currentValue);
	
				if (moveSequences.isEmpty()) {
					if (debug)
						System.out.printf("  Existing moveSequences was empty\n", c, currentValue);
					
					ArrayList<MoveSequence> moveSequencesToDoubleValue = movesToDoubleValue(board, c, pair, playerId);
					
					if (moveSequencesToDoubleValue != null) {
						for (MoveSequence moveSequenceToDoubleValue : moveSequencesToDoubleValue)
								moveSequences.add(moveSequenceToDoubleValue);
						
						currentValue *= 2;
					} else {
						if (debug)
							System.out.printf("  BREAK: returning null\n");
						
						return null;
					}
				} else {
					if (debug)
						System.out.printf("  Existing moveSequences was not empty\n", c, currentValue);
					
					boolean canDoubleValue = false;
					
					ArrayList<MoveSequence> newMoveSequences = new ArrayList<MoveSequence>();
					
					for (MoveSequence moveSequence : moveSequences) {
						ArrayList<MoveSequence> moveSequencesToDoubleValue = movesToDoubleValue(moveSequence.board, c, pair, playerId);
						
						if (moveSequencesToDoubleValue != null) {
							canDoubleValue = true;
							
							for (MoveSequence moveSequenceToDoubleValue : moveSequencesToDoubleValue) {
								MoveSequence newMoveSequence = new MoveSequence(board, pair);
								newMoveSequence.moves.addAll(moveSequence.moves);
								newMoveSequence.moves.addAll(moveSequenceToDoubleValue.moves);
								
								for (Move move : newMoveSequence.moves)
									newMoveSequence.board.processMove(move);
								
								newMoveSequences.add(newMoveSequence);
							}
						}
					}
					
					moveSequences = newMoveSequences;
					
					if (canDoubleValue) {
						currentValue *= 2;
					} else {
						return null;
					}
				}
				
				
				if (debug) {
					System.out.printf("  END OF STEP:\n");
					for (MoveSequence moveSequence : moveSequences) {
						System.out.printf("    %d   %s\n", currentValue, moveSequence);
					}
				}
			}
			
			return moveSequences;
		}
	}
	
	// Returns an array of all move sequences such that the value of the point at the given coordinate is doubled, i.e., one combination
	// Includes the combination move itself as the last move in each list, with the given point receiving the coins.  (Of course, this final move can be reversed if desired)
	private ArrayList<MoveSequence> movesToDoubleValue(Board board, Coord c, Pair pair, int playerId) {
		Point p = board.getPoint(c);
		
		if (p.value == 0)
			return null;			// Cannot increase the value if it is already zero
		
		ArrayList<MoveSequence> moveSequences = new ArrayList<MoveSequence>();
		
		ArrayList<Coord> neighbors = board.neighborsOf(c, pair);
		
		for (Coord n : neighbors) {
			ArrayList<MoveSequence> moveSequencesToMakeValue = movesToMakeValue(board, n, p.value, pair, playerId);
			
			if (moveSequencesToMakeValue != null) {
				for (MoveSequence moveSequenceToMakeValue : moveSequencesToMakeValue) {
					moveSequenceToMakeValue.moves.add(new Move(n, c, playerId));
					moveSequences.add(moveSequenceToMakeValue);
				}
			}
		}
		
		if (moveSequences.isEmpty()) {
			return null;
		} else {
			for (MoveSequence moveSequence : moveSequences) {
				moveSequence.board = new Board(board);
				
				for (Move move : moveSequence.moves)
					moveSequence.board.processMove(move);
			}
			
			return moveSequences;
		}
	}
}
