package offset.group4;

import java.util.*;

import offset.group4.Board;
import offset.group4.Move;

import offset.sim.Pair;

public class MoveSequence {
	public ArrayList<Move> moves;		// The list of moves in the sequence
	public Pair pair;					// The pair used to generate the moves
	public int coinSwing;				// The change in coins resulting from the sequence
	public Board board;					// The board that *results* from playing the moves in the sequence
		
	public MoveSequence(Board board, Pair pair) {
		this.moves = new ArrayList<Move>();
		this.board = new Board(board);
		this.pair = new Pair(pair);
	}
	
	public boolean isDisruptedBy(Board board, Move testMove) {
		//Board newBoard = new Board(board);
		//newBoard.processMove(testMove);
		//System.out.printf("Testing if %s disrupts me\n", testMove);
		/*for (Move move : this.moves) {
			if (newBoard.isMoveValid(move, pair))
				newBoard.processMove(move);
			else
				return true;
		}*/
		
		return false;
	}
	
	@Override public String toString() {
		String str = "Coin swing " + coinSwing + " by ";
		
		for (Move move : this.moves) {
			str = str + move.toString() + " ";
		}
				
		return str;
	}
}
