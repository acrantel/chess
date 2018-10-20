package chessPieces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import chessStructure.ChessComponent;
import chessStructure.Square;

/**
 * This is the class for the Knight piece in chess.
 * It can move to 2 squares horizontally and 1 square vertically,
 * or 2 squares vertically and 1 square horizontally
 * @author serena
 *
 */
public class Knight extends Piece 
{
	private final String color;
	private boolean isAlive = true;
	private ImageIcon image;
	private ImageIcon miniIcon;

	/** Creates a Knight with the color as parameter
	 * @param color the color of the piece(black or white)*/
	public Knight(String color) 
	{
		if (color.toLowerCase().equals("black") 
				|| color.toLowerCase().equals("white"))
			this.color = color;
		else
			this.color = "white";
		this.image = this.generateImageIcon(this.color);
		this.miniIcon = this.generateMiniImageIcon(this.color);
	}
	
	public static ImageIcon generatePromotionIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Knight.png");
		Image img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {}
		img = img.getScaledInstance(ChessComponent.PROMOTION_UNIT-(ChessComponent.SIZE_MOD)-2, 
				ChessComponent.PROMOTION_UNIT-(ChessComponent.SIZE_MOD)-2, 0);
		
		return new ImageIcon(img);
	}
	
	private ImageIcon generateMiniImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Knight.png");
		Image img = null;
		try {
			img = ImageIO.read(file);
		}
		catch (IOException e){}
		img = img.getScaledInstance(ChessComponent.UNIT/2-ChessComponent.SIZE_MOD/2, 
				ChessComponent.UNIT/2-ChessComponent.SIZE_MOD/2, 0);
		
		ImageIcon icon = new ImageIcon(img);
		return icon;
	}
	@Override
	public ImageIcon getMiniImageIcon() {
		return miniIcon;
	}
	
	private ImageIcon generateImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Knight.png");
		Image img = null;
		try {
			img = ImageIO.read(file);
		}
		catch (IOException e){}
		img = img.getScaledInstance(ChessComponent.UNIT-ChessComponent.SIZE_MOD, 
				ChessComponent.UNIT-ChessComponent.SIZE_MOD, 0);
		
		ImageIcon icon = new ImageIcon(img);
		return icon;
	}

	@Override
	public ImageIcon getImageIcon() {
		return image;
	}
	
	/** Gets if the knight is still alive or not */
	@Override
	public boolean isAlive() {
		return isAlive;
	}

	/** Kills the knight if it isn't dead already  
	 * i.e. it changes isAlive to false */
	@Override
	public void killPiece() {
		isAlive = false;
	}
	
	/** Gets the color of this knight */
	@Override
	public String getColor() {
		return color;
	}

	/**
	 * checks if the square contains a Knight
	 * @param board the board
	 * @param square the square to check
	 */
	public static boolean isKnightOnSquare(Square square) {
		/* testing if square actually contains a Knight */
		boolean result = false;
		if (square != null && square.getPiece() != null &&
				square.getPiece() instanceof Knight) {
			result = true;
		}
		return result;
	}
	
	/**
	 * gets the knights that are in the board
	 * @param board the whole board
	 * @return the knights that are in the board
	 */
	public static ArrayList<Square> getKnights(Square[][] board, String color) {
		ArrayList<Square> knights = new ArrayList<Square>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col].getPiece() != null && 
						board[row][col].getPiece() instanceof Knight &&
						board[row][col].getPiece().getColor().equals(color)) {
					knights.add(board[row][col]);
				}
			}
		}
		return knights;
	}
	
	/**
	 * Checks if a move from 'start' to 'end' is a valid 
	 * move for a knight
	 * @param board the rest of the pieces
	 * @param start the starting place of the knight
	 * @param end the ending place of the knight
	 */
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		int rowDiff = Math.abs(start.getRow()-end.getRow());
		int colDiff = Math.abs(start.getColNum()-end.getColNum());
		if (isKnightOnSquare(start)
				&& ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
			if (end.getPiece() != null && 
					end.getPiece().getColor().equals(start.getPiece().getColor()))
				return false;
			//checking end square validity
			if (end.getPiece() != null && 
					end.getPiece().getColor().equals(start.getPiece().getColor())) {
				return false;
			}
			
			boolean result = true;
			Piece formerEndPiece = end.getPiece();
			String color = start.getPiece().getColor();
			end.setPiece(start.getPiece());
			start.setPiece(null);
			
			if (King.isKingInCheck(board, King.getKing(board, color))) {
				start.setPiece(end.getPiece());
				end.setPiece(formerEndPiece);
				return false;
			}
			start.setPiece(end.getPiece());
			end.setPiece(formerEndPiece);
			
			return result;
		}
		return false;
	}

	@Override
	public ArrayList<Square> getMoveableSquares(Square[][] board, 
			Square currentPos) {
		ArrayList<Square> possible = Square.getLAway(board, currentPos);
		ArrayList<Square> moveable = new ArrayList<Square>();
		for (int i = 0; i < possible.size(); i++) {
			if (isValidMove(board, currentPos, possible.get(i))) {
				moveable.add(possible.get(i));
			}
		}
		return moveable;
	}

}
