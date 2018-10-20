package chessStructure;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import chessPieces.Bishop;
import chessPieces.King;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.Piece;
import chessPieces.Queen;
import chessPieces.Rook;

/**
 * Does not have stalemate, or draws
 * @author serena
 *
 */
public class ChessBoard
{
	/** The 8*8 board array: [0-1][0-7] are white, [6-7][0-7] are black.
	 * board[0] is row 1 of the chessboard, 
	 * board[any row][0] is col a of the chessboard. 
	 * If there is nothing in a spot, that spot is null */
	private final Square[][] board = new Square[8][8];
	
	public ChessBoard() {
		initializePieces();
	}
	
	/**
	 * initializes the board with 32 pieces, 16 black and 16 white
	 */
	public void initializePieces() {
		String color = "white";
		
		//goes through all the rows
		for (int row = 0; row < board.length; row++) {
			/* initializing row 1 or 7 on the board */
			if (row == 0 || row == 7) {
				board[row][0] = new Square(row, 'a', new Rook(color));
				board[row][7] = new Square(row, 'h', new Rook(color));
				board[row][1] = new Square(row, 'b', new Knight(color));
				board[row][6] = new Square(row, 'g', new Knight(color));
				board[row][2] = new Square(row, 'c', new Bishop(color));
				board[row][5] = new Square(row, 'f', new Bishop(color));
				board[row][3] = new Square(row, 'd', new Queen(color));
				board[row][4] = new Square(row, 'e', new King(color));
			} else if (row == 1 || row == 6) {
				for (int col = 0; col < board[row].length; col++) {
					board[row][col] = new Square(row, Square.getCol(col+""), 
							new Pawn(color));
				}
			} else {
				for (int col = 0; col < board[row].length; col++) {
					board[row][col] = new Square(row, Square.getCol(col+""), null);
				}
			}
			if (row == 4)
				color = "black";
		}
	}
	
	/**
	 * Checks if the move is valid, then makes the move
	 * @param start the square the move starts with
	 * @param end the square the move ends with
	 * @return if the move was executed or not
	 */
	public boolean makeMove(Square squareStart, Square squareEnd) {
		Piece pieceStart = squareStart.getPiece();
		if (pieceStart.isValidMove(board, squareStart, squareEnd))
			return true;
		else
			return false;
		
	}
	/**
	 * draws the image of a chess piece 
	 * given corner coordinates, file name, 
	 * and Graphics2D
	 * @param x the corner x coordinate
	 * @param y the corner y coordinate
	 * @param fileName the name of the file
	 * @param g the graphics context for the 
	 * image to be drawn on
	 * @param comp the default image observer
	 */
	public static void drawChessPiece(int x, int y,
			ImageIcon icon, Graphics2D g2, JComponent comp) {
		
		icon.paintIcon(comp, g2, x+3, y+3);
	}
	
	public Square[][] getBoard() {
		return board;
	}
	
	
	public static String getOpponentColor(String color) {
		if (color.equals("black"))
			return "white";
		else 
			return "black";
	}
	
	public static final String COLOR_1 = "white";
	public static final String COLOR_2 = "black";
}
