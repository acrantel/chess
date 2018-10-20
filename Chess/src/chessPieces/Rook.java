package chessPieces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import chessStructure.ChessBoard;
import chessStructure.ChessComponent;
import chessStructure.Square;

/**
 * This is the class for the Rook piece in chess.
 * @author serena
 *
 */
public class Rook extends Piece 
{
	private final String color;
	private boolean isAlive = true;
	private ImageIcon image;
	private ImageIcon miniIcon;

	/** Creates a Rook with the color as parameter
	 * @param color the color of the piece(black or white)*/
	public Rook(String color) 
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
		File file = new File(FILE_PATH_BEGINNING + color + "Rook.png");
		Image img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {}
		img = img.getScaledInstance(ChessComponent.PROMOTION_UNIT-ChessComponent.SIZE_MOD-2, 
				ChessComponent.PROMOTION_UNIT-ChessComponent.SIZE_MOD-2, 0);
		
		return new ImageIcon(img);
	}
	
	private ImageIcon generateMiniImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Rook.png");
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
		File file = new File(FILE_PATH_BEGINNING + color + "Rook.png");
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
	
	/** Gets if the rook is still alive or not */
	@Override
	public boolean isAlive() {
		return isAlive;
	}

	/** Kills the rook if it isn't dead already  
	 * i.e. it changes isAlive to false */
	@Override
	public void killPiece() {
		isAlive = false;
	}
	
	/** Gets the color of this rook */
	@Override
	public String getColor() {
		return color;
	}
	
	/**
	 * checks if the square contains a Rook
	 * @param board the board
	 * @param square the square to check
	 */
	public static boolean isRookOnSquare(Square square) {
		/* testing if square actually contains a Rook */
		boolean result = false;
		if (square != null && square.getPiece() != null &&
				square.getPiece() instanceof Rook) {
			result = true;
		}
		return result;
	}
	
	/**
	 * gets the Rooks that are in the board
	 * @param board the whole board
	 * @return the Rooks that are in the board
	 */
	public static ArrayList<Square> getRooks(Square[][] board, String color) {
		ArrayList<Square> rooks = new ArrayList<Square>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col].getPiece() != null && 
						board[row][col].getPiece() instanceof Rook &&
						board[row][col].getPiece().getColor().equals(color)) {
					rooks.add(board[row][col]);
				}
			}
		}
		return rooks;
	}
	
	/**
	 * Checks if a move from 'start' to 'end' is a valid 
	 * move for a rook
	 * @param board the rest of the pieces
	 * @param start the starting place of the rook
	 * @param end the ending place of the rook
	 */
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		//moving horizontally
		if (start.getRow() == end.getRow() && end.getColNum() != start.getColNum()) {
			Square largerCol = end;
			Square smallerCol = start;
			if (smallerCol.getColNum() > largerCol.getColNum()) {smallerCol = end; largerCol = start;}
			for (int i = smallerCol.getColNum()+1; i < largerCol.getColNum(); i++) {
				if (board[start.getRow()][i].getPiece() != null) 
					return false;
			}
			
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
				result = false;
			}
			start.setPiece(end.getPiece());
			end.setPiece(formerEndPiece);
			
			return result;
		}
		// moving vertically
		else if (start.getColNum() == end.getColNum() && start.getRow() != end.getRow()) {
			Square largerRow = end;
			Square smallerRow = start;
			if (smallerRow.getRow() > largerRow.getRow()) {largerRow = start; smallerRow = end;}
			for (int i = smallerRow.getRow()+1; i < largerRow.getRow(); i++) {
				if (board[i][start.getColNum()].getPiece() != null) 
					return false;
			}
			
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
				result = false;
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
		ArrayList<Square> possible = Square.getAllHorizontalAway(board, currentPos, 
				ChessBoard.getOpponentColor(currentPos.getPiece().getColor()));
		possible.addAll(Square.getAllVerticalAway(board, currentPos, 
				ChessBoard.getOpponentColor(currentPos.getPiece().getColor())));
		
		ArrayList<Square> moveable = new ArrayList<Square>();
		for (int i = 0; i < possible.size(); i++) {
			if (isValidMove(board, currentPos, possible.get(i))) {
				moveable.add(possible.get(i));
			}
		}
		return moveable;
	}

}
