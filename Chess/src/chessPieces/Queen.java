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

public class Queen extends Piece 
{

	private final String color;
	private boolean isAlive = true;
	private ImageIcon image;
	private ImageIcon miniIcon;

	public Queen(String color)
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
		File file = new File(FILE_PATH_BEGINNING + color + "Queen.png");
		String a = file.getPath();
		Image img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		img = img.getScaledInstance(ChessComponent.PROMOTION_UNIT-ChessComponent.SIZE_MOD-2, 
				ChessComponent.PROMOTION_UNIT-ChessComponent.SIZE_MOD-2, 0);
		
		return new ImageIcon(img);
	}
	
	private ImageIcon generateMiniImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Queen.png");
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
		File file = new File(FILE_PATH_BEGINNING + color + "Queen.png");
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

	/** Gets if the queen is 
	 * alive or captured */
	@Override
	public boolean isAlive() {
		return isAlive;
	}
	
	/**
	 * Kills the queen if it isn't dead already
	 * i.e. changews isAive to false
	 */
	@Override
	public void killPiece() {
		isAlive = false;
	}
	
	/** Gets the color of the queen */
	@Override
	public String getColor() {
		return color;
	}
	
	/**
	 * checks if the square contains a Queen
	 * @param board the board
	 * @param square the square to check
	 */
	public static boolean isQueenOnSquare(Square square) {
		/* testing if square actually contains a Queen */
		boolean result = false;
		if (square.getPiece() != null && square.getPiece() instanceof Queen)
			result = true;
		return result;
	}
	
	/**
	 * gets the queen that are in the board
	 * @param board the whole board
	 * @return the queen that are in the board
	 */
	public static ArrayList<Square> getQueen(Square[][] board, String color) {
		ArrayList<Square> queen = new ArrayList<Square>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col].getPiece() != null && 
						board[row][col].getPiece() instanceof Queen &&
						board[row][col].getPiece().getColor().equals(color)) {
					queen.add(board[row][col]);
				}
			}
		}
		return queen;
	}
	
	/**
	 * Checks if a move from 'start' to 'end' is a valid 
	 * move for a queen
	 * @param board the rest of the pieces
	 * @param start the starting place of the queen
	 * @param end the ending place of the queen
	 */
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		if (isQueenOnSquare(start)) {
			Rook rook = new Rook(color);
			Bishop bishop = new Bishop(color);
			if (rook.isValidMove(board, start, end) || bishop.isValidMove(board, start, end)) {
				return true;
			} 
		}
		return false;
	}
	
	@Override
	public ArrayList<Square> getMoveableSquares(Square[][] board, 
			Square currentPos) {
		ArrayList<Square> possible = Square.getAllDiagonalsAway(board, currentPos, 
				ChessBoard.getOpponentColor(currentPos.getPiece().getColor()));
		possible.addAll(Square.getAllHorizontalAway(board, currentPos, 
				ChessBoard.getOpponentColor(currentPos.getPiece().getColor())));
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
