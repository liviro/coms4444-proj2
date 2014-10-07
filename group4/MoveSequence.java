package offset.group4;

import java.util.*;

import offset.group4.Board;
import offset.group4.Move;

public class MoveSequence {
	public ArrayList<Move> moves;		// The list of moves in the sequence
	public int coinSwing;				// The change in coins resulting from the sequence
	public Board board;					// The board that *results* from playing the moves in the sequence
	
	public MoveSequence() {
		this.moves = new ArrayList<Move>();
	}
	
	public MoveSequence(Board board) {
		this.moves = new ArrayList<Move>();
		this.board = new Board(board);
	}
	
	@Override public String toString() {
		String str = "Coin swing " + coinSwing + " by ";
		
		for (Move move : this.moves) {
			str = str + move.toString() + " ";
		}
				
		return str;
	}
}
