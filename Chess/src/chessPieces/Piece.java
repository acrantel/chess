package chessPieces;

import java.util.ArrayList;
import java.util.LinkedList;

import chessStructure.Move;
import chessStructure.Square;

import javax.swing.ImageIcon;

public abstract class Piece {
	
	public ArrayList<Square> getMoveableSquares(Square[][] board, Square currentPos) {
		return null;
	}
	public ArrayList<Square> getMoveableSquares(Square[][] board, Square currentPos, LinkedList<Move> previousMoves) {
		return null;
	}
	abstract public boolean isValidMove(Square[][] board, Square start, Square end);
	abstract public ImageIcon getMiniImageIcon();
	abstract public ImageIcon getImageIcon();
	
	abstract public boolean isAlive();
	abstract public void killPiece();
	abstract public String getColor();
	
	public static final int CORNERX = 20;
	public static final int CORNERY = 20;
	
	public static final int UNIT = 60;

	/** the string that goes before the piece name 
	 * for generating the file path*/
	public static final String FILE_PATH_BEGINNING = "ChessIcons/";
		
}
