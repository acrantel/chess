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
 * This is the class for the Bishop piece in chess.
 * @author serena
 *
 */
public class Bishop extends Piece 
{
	private final String color;
	private boolean isAlive = true;
	private ImageIcon image;
	private ImageIcon miniIcon;

	/** Creates a Bishop with the color as parameter
	 * @param color the color of the piece(black or white)*/
	public Bishop(String color) 
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
		File file = new File(FILE_PATH_BEGINNING + color + "Bishop.png");
		Image img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {}
		img = img.getScaledInstance(ChessComponent.PROMOTION_UNIT-(ChessComponent.SIZE_MOD)-2, 
				ChessComponent.PROMOTION_UNIT-(ChessComponent.SIZE_MOD)-2, 0);
		
		return new ImageIcon(img);
	}
	
	private ImageIcon generateMiniImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Bishop.png");
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
		File file = new File(FILE_PATH_BEGINNING + color + "Bishop.png");
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
	
	/** Gets if the bishop is still alive or not */
	@Override
	public boolean isAlive() {
		return isAlive;
	}

	/** Kills the bishop if it isn't dead already  
	 * i.e. it changes isAlive to false */
	@Override
	public void killPiece() {
		isAlive = false;
	}
	
	/** Gets the color of this bishop */
	@Override
	public String getColor() {
		return color;
	}

	/**
	 * checks if the square contains a bishop
	 * @param board the board
	 * @param square the square to check
	 */
	public static boolean isBishopOnSquare(Square square) {
		/* testing if square actually contains a king */
		boolean result = false;
		if (square != null && square.getPiece() != null &&
				square.getPiece() instanceof Bishop) {
			result = true;
		}
		return result;
	}

	/**
	 * gets the bishops that are in the board
	 * @param board the whole board
	 * @return the bishops that are in the board
	 */
	public static ArrayList<Square> getBishops(Square[][] board, String color) {
		ArrayList<Square> bishops = new ArrayList<Square>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (isBishopOnSquare(board[row][col]) &&
						board[row][col].getPiece().getColor().equals(color)) {
					bishops.add(board[row][col]);
				}
			}
		}
		return bishops;
	}
	
	/**
	 * Checks if a move from 'start' to 'end' is a valid 
	 * move for a bishop
	 * @param board the rest of the pieces
	 * @param start the starting place of the bishop
	 * @param end the ending place of the bishop
	 */
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		if (Math.abs(start.getRow() - end.getRow()) 
				== Math.abs(start.getColNum() - end.getColNum())) {
			Square smaller = start;
			Square larger = end;
			if (smaller.getRow() > larger.getRow()) {
				smaller = end; larger = start;
			}
			/* bottom left to top right */
			if ((start.getRow()-end.getRow() == start.getColNum()-end.getColNum())) {
				int row = smaller.getRow()+1;
				int col = smaller.getColNum()+1;
				while (row < larger.getRow() && col < larger.getColNum()) {
					if (board[row][col].getPiece() != null) {
						//result = false;
						return false;
					}
					row++;
					col++;
				}
			}
			/* top left to bottom right */
			else if (start.getRow()-end.getRow() == -1*(start.getColNum()-end.getColNum())) {
				int row = smaller.getRow()+1;
				int col = smaller.getColNum()-1;
				while (row < larger.getRow() && col > larger.getColNum()) {
					if (board[row][col].getPiece() != null) {
						//result = false;
						return false;
					}
					row++;
					col--;
				}
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
		ArrayList<Square> possible = Square.getAllDiagonalsAway(board, currentPos, 
				ChessBoard.getOpponentColor(currentPos.getPiece().getColor()));
		ArrayList<Square> moveable = new ArrayList<Square>();
		for (int i = 0; i < possible.size(); i++) {
			if (isValidMove(board, currentPos, possible.get(i))) {
				moveable.add(possible.get(i));
			}
		}
		return moveable;
	}

}
