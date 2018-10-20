package chessPieces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import chessStructure.ChessComponent;
import chessStructure.Move;
import chessStructure.Square;

/**
 * This is the class for the Pawn piece in chess.
 * @author serena
 *
 */
public class Pawn extends Piece 
{
	private final String color;
	private boolean isAlive = true;
	private ImageIcon image;
	private ImageIcon miniIcon;
	
	/** Creates a Pawn with the color as parameter
	 * @param color the color of the piece(black or white)*/
	public Pawn(String color) 
	{
		if (color.toLowerCase().equals("black") 
				|| color.toLowerCase().equals("white"))
			this.color = color;
		else
			this.color = "white";

		this.image = this.generateImageIcon(this.color);
		this.miniIcon = this.generateMiniImageIcon(this.color);
	}
	
	private ImageIcon generateMiniImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Pawn.png");
		Image img = null;
		try {
			img = ImageIO.read(file);
		}
		catch (IOException e){}
		img = img.getScaledInstance(ChessComponent.UNIT/2, ChessComponent.UNIT/2, 0);
		
		ImageIcon icon = new ImageIcon(img);
		return icon;
	}
	@Override
	public ImageIcon getMiniImageIcon() {
		return miniIcon;
	}
	
	private ImageIcon generateImageIcon(String color) {
		File file = new File(FILE_PATH_BEGINNING + color + "Pawn.png");
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
	
	/** Gets if the pawn is still alive or not */
	@Override
	public boolean isAlive() {
		return isAlive;
	}

	/** Kills the pawn if it isn't dead already  
	 * i.e. it changes isAlive to false */
	@Override
	public void killPiece() {
		isAlive = false;
	}
	
	/** Gets the color of this pawn */
	@Override
	public String getColor() {
		return color;
	}

	/**
	 * checks if the square contains a Pawn
	 * @param board the board
	 * @param square the square to check
	 */
	public static boolean isPawnOnSquare(Square square) {
		/* testing if square actually contains a Pawn */
		boolean result = false;
		if (square != null && square.getPiece() != null &&
				square.getPiece() instanceof Pawn) {
			result = true;
		}
		return result;
	}
	/**
	 * gets the Pawns that are in the board
	 * @param board the whole board
	 * @return the Pawns that are in the board
	 */
	public static ArrayList<Square> getPawns(Square[][] board, String color) {
		ArrayList<Square> pawns = new ArrayList<Square>();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col].getPiece() != null && 
						board[row][col].getPiece() instanceof Pawn &&
						board[row][col].getPiece().getColor().equals(color)) {
					pawns.add(board[row][col]);
				}
			}
		}
		return pawns;
	}
	
	/**
	 * Checks if a move from 'start' to 'end' is a valid 
	 * move for a pawn
	 * @param board the rest of the pieces
	 * @param start the starting place of the pawn
	 * @param end the ending place of the pawn
	 */
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		if (Pawn.isPawnOnSquare(start)) {
			boolean result = false;
			//if the move is move two forward(beginning) move (white)
			if ((start.getRow() == 1 && end.getRow() == 3) 
					&& start.getColNum() == end.getColNum()
							&& start.getPiece().getColor().equals("white")) {
				if (board[2][start.getColNum()].getPiece() == null 
						&& board[3][start.getColNum()].getPiece() == null) 
					result = true;
			} 
			//if the move is move two forward(beginning) move (black)
			else if (start.getRow() == 6 && end.getRow() == 4 
					&& start.getColNum() == end.getColNum() 
					&& start.getPiece().getColor().equals("black")) {
				if (board[5][start.getColNum()].getPiece() == null 
						&& board[4][start.getColNum()].getPiece() == null)
					result = true;
			}
			//if the move is move one forward(white)
			else if (end.getRow()-start.getRow() == 1 && end.getColNum() == start.getColNum() 
					&& end.getPiece() == null 
					&& start.getPiece().getColor().equals("white")) {
				result = true;
			} 
			//if the move is move one forward(black)
			else if (end.getRow()-start.getRow() == -1 && end.getColNum() == start.getColNum() 
					&& end.getPiece() == null 
					&& start.getPiece().getColor().equals("black")) {
				result = true;
			}
			//if the move is one move forward and a move to the side (white)
			else if (start.getPiece().getColor().equals("white") && end.getRow()-start.getRow() == 1
					&& Math.abs(start.getColNum()-end.getColNum()) == 1 && end.getPiece() != null
					&& end.getPiece().getColor().equals("black")) {
				result = true;
			} 
			//if the move is one move forward and a move to the side (black)
			else if (start.getPiece().getColor().equals("black") && end.getRow()-start.getRow() == -1
					&& Math.abs(start.getColNum()-end.getColNum()) == 1 && end.getPiece() != null
					&& end.getPiece().getColor().equals("white")) {
				result = true;
			}
			
			//checking end square validity
			if (end.getPiece() != null && 
					end.getPiece().getColor().equals(start.getPiece().getColor())) {
				return false;
			}
			if (result) {				
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
		}
		return false;
	}
	
	/**
	 * checks if a move from 'capturingPiece' to 'landingSquare' 
	 * is legal(en passant)
	 * @param board the board
	 * @param previousMove the previous move
	 * @param capturingPiece the piece that will capture the other pawn
	 * @param landingSquare the square that the capturing piece will 
	 * be in after the en passant
	 * @param enemyPawn the enemy pawn that will be captured
	 * @return
	 */
	public static boolean isEnPassant(Square[][] board, Move previousMove, Square capturingPiece, 
			Square landingSquare, Square enemyPawn) {
		/* if there is a pawn on capturingPiece and enemyPawn and if there is not a pawn on
		 * landingSquare and if the previously moved piece is on enemyPawn */
		if (isPawnOnSquare(capturingPiece) && isPawnOnSquare(enemyPawn) && 
				landingSquare.getPiece() == null && 
				previousMove.getMovedPiece().equals(enemyPawn.getPiece())) {
			
			// white attacking
			/* if the capturing pawn is white and the enemy pawn is black */
			if (capturingPiece.getPiece().getColor().equals("white") 
					&& enemyPawn.getPiece().getColor().equals("black")) {
				if (capturingPiece.getRow() != 4 || enemyPawn.getRow() != 4 || landingSquare.getRow() != 5
						|| Math.abs(capturingPiece.getColNum()-enemyPawn.getColNum()) != 1 
						|| landingSquare.getColNum() != enemyPawn.getColNum()) {
					return false;
				}
			}
			//black attacking
			/* if the capturing pawn is black and the enemy pawn is white */
			else if (capturingPiece.getPiece().getColor().equals("black")
					&& enemyPawn.getPiece().getColor().equals("white")) {
				if (capturingPiece.getRow() != 3 || enemyPawn.getRow() != 3 || landingSquare.getRow() != 2
						|| Math.abs(capturingPiece.getColNum()-enemyPawn.getColNum()) != 1 
						|| landingSquare.getColNum() != enemyPawn.getColNum()) {
					return false;
				}
			} else {
				return false;
			}
			
			boolean result = true;
			Pawn formerEnemyPawn = (Pawn) enemyPawn.getPiece();
			enemyPawn.setPiece(null);
			String color = capturingPiece.getPiece().getColor();
			landingSquare.setPiece(capturingPiece.getPiece());
			capturingPiece.setPiece(null);
			
			if (King.isKingInCheck(board, King.getKing(board, color))) {
				result = false;
			}
			capturingPiece.setPiece(landingSquare.getPiece());
			enemyPawn.setPiece(formerEnemyPawn);
			landingSquare.setPiece(null);
			return result;
			
			
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if a move makes the pawn able to promote
	 * @param board the board
	 * @param start the starting place
	 * @param end the ending place
	 * @return
	 */
	public boolean isPromotion(Square[][] board, Square start, Square end) {
		if (isPawnOnSquare(start) && isValidMove(board, start, end) && (end.getRow() == 7 || end.getRow() == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ArrayList<Square> getMoveableSquares(Square[][] board, 
			Square currentPos, LinkedList<Move> previousMoves) {
		ArrayList<Square> possible = new ArrayList<Square>();
		ArrayList<Square> possibleEnPassants = new ArrayList<Square>();
		/* if the piece is black, it goes toward the 1st rank */
		if (currentPos.getPiece().getColor().equals("black")) {
			if (currentPos.getRow() > 0) {
				possible.add(board[currentPos.getRow()-1][currentPos.getColNum()]);
				if (currentPos.getColNum() > 0) {
					possible.add(board[currentPos.getRow()-1][currentPos.getColNum()-1]);
				}
				if (currentPos.getColNum() < 7) {
					possible.add(board[currentPos.getRow()-1][currentPos.getColNum()+1]);
				}
			}
			if (currentPos.getRow() == 6) {
				possible.add(board[currentPos.getRow()-2][currentPos.getColNum()]);
			}
			
			/** getting the en passant possiblities */
			if (currentPos.getRow() == 3) {
				if (currentPos.getColNum() > 0) {
					possibleEnPassants.add(board[currentPos.getRow()-1][currentPos.getColNum()-1]);
				} 
				if (currentPos.getColNum() < 7) {
					possibleEnPassants.add(board[currentPos.getRow()-1][currentPos.getColNum()+1]);
				}
			}
		}
		/* if the piece is white, it goes toward the 8th rank */
		else {
			if (currentPos.getRow() < 7) {
				possible.add(board[currentPos.getRow()+1][currentPos.getColNum()]);
				
				if (currentPos.getColNum() > 0) {
					possible.add(board[currentPos.getRow()+1][currentPos.getColNum()-1]);
				}
				if (currentPos.getColNum() < 7) {
					possible.add(board[currentPos.getRow()+1][currentPos.getColNum()+1]);
				}
			}
			if (currentPos.getRow() == 1) {
				possible.add(board[currentPos.getRow()+2][currentPos.getColNum()]);
			}
			
			/** getting the en passant possiblities */
			if (currentPos.getRow() == 4) {
				if (currentPos.getColNum() > 0) {
					possibleEnPassants.add(board[currentPos.getRow()+1][currentPos.getColNum()-1]);
				} 
				if (currentPos.getColNum() < 7) {
					possibleEnPassants.add(board[currentPos.getRow()+1][currentPos.getColNum()+1]);
				}
			}
		}
		
		ArrayList<Square> moveable = new ArrayList<Square>();
		for (int i = 0; i < possible.size(); i++) {
			if (isValidMove(board, currentPos, possible.get(i))) {
				moveable.add(possible.get(i));
			}
		}
		
		int goUpAdder = 1;
		if (currentPos.getPiece().getColor().equals("black")) {goUpAdder = -1;}
		
		for (int i = 0; i < possibleEnPassants.size(); i++) {
			if (isEnPassant(board, previousMoves.getLast(), currentPos, 
					possibleEnPassants.get(i), 
					board[possibleEnPassants.get(i).getRow()-goUpAdder][possibleEnPassants.get(i).getColNum()]))
				moveable.add(possibleEnPassants.get(i));
		}
		
		return moveable;
	}

}
