package chessPieces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


import chessStructure.ChessBoard;
import chessStructure.ChessComponent;
import chessStructure.Move;
import chessStructure.Square;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class King extends Piece 
{
	private final String color;
	private boolean isAlive = true;
	private ImageIcon image;
	private ImageIcon miniIcon;
	
	/**
	 * creates a new King with the color as the parameter
	 * @param color default color is white
	 * 
	 */
	public King(String color)
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
		File file = new File(FILE_PATH_BEGINNING + color + "King.png");
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
		File file = new File(FILE_PATH_BEGINNING + color + "King.png");
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
		return this.image;
	}
	
	/** Gets if the king is still alive or not */
	@Override
	public boolean isAlive() {
		return this.isAlive;
	}
	
	/** Kills the king if it isn't dead already  
	 * i.e. it changes isAlive to false */
	public void killPiece() {
		isAlive = false;
	}
	
	/** Gets the color of this king */
	@Override
	public String getColor() {
		return this.color;
	}
	
	/**
	 * Checks if a move from 'start' to 'end' is a valid 
	 * move for a king. Includes checks for check, and if 
	 * there is checkmate, it returns false.
	 * @param board the rest of the pieces
	 * @param start the starting place of the king
	 * @param end the ending place of the king
	 */
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		boolean isValid = false;
		if (King.isKingOnSquare(start)) {
			String opponentColor = ChessBoard.getOpponentColor(start.getPiece().getColor());
			
			//check for if is 1 square away
			if (Math.abs(start.getRow()-end.getRow()) <= 1 
					&& Math.abs(start.getColNum()-end.getColNum()) <= 1
					&& !start.equals(end)) {
				if (!Square.isSquareBeingAttacked(board, end, opponentColor) 
						&& end.getPiece() == null) {
					isValid = true;
				} else if (!Square.isSquareBeingAttacked(board, end, opponentColor)
						&& end.getPiece() != null && end.getPiece().getColor().equals(opponentColor)) {
					isValid = true;
				}
			}
		}
		return isValid;
	}
	
	/**
	 * checks if a move is castling
	 * Castling is permissible if and only if all of the following conditions hold (Schiller 2003:19):
	 * 1.The king and the chosen rook are on the player's first rank.
	 * 2.Neither the king nor the chosen rook has previously moved.
	 * 3.There are no pieces between the king and the chosen rook.
	 * 4.The king is not currently in check.
	 * 5.The king does not pass through a square that is attacked by an enemy piece.
	 * 6.The king does not end up in check. (True of any legal move.)

	 * @param board
	 * @param kingStart
	 * @param kingEnd
	 * @param rookStart
	 * @param previousMove
	 * @return
	 */
	public static boolean isCastle(Square[][] board, Square kingStart, Square kingEnd, Square rookStart, LinkedList<Move> previousMoves) {
		/* if the king's color is equal to the rook's color
		 * and the rook and king are on the same row
		 * and the king is not in check */
		if (isKingOnSquare(kingStart) && Rook.isRookOnSquare(rookStart) && 
				kingStart.getPiece().getColor().equals(rookStart.getPiece().getColor())
				&& kingStart.getRow() == kingEnd.getRow() && kingEnd.getRow() == rookStart.getRow()
				&& !King.isKingInCheck(board, kingStart)) {
			
			//check if the the king or rook was moved before
			if (previousMoves.size() != 0) {
				
				int i = previousMoves.size()-1;
				Move beforeMove = previousMoves.get(i);
				
				while (i >= 0) {
					if (beforeMove.getMovedPiece().equals(kingStart.getPiece()) || 
							beforeMove.getMovedPiece().equals(rookStart.getPiece())) {
						return false;
					}
					i--;
					if (i >= 0) {
						beforeMove = previousMoves.get(i);
					}
				}
			}
			/* checking if there is anything in between the rook and king */
			Square largerCol = kingStart;
			Square smallerCol = rookStart;
			if (smallerCol.getColNum() > largerCol.getColNum()) {smallerCol = kingStart; largerCol = rookStart;}
			
			for (int i = smallerCol.getColNum()+1; i < largerCol.getColNum(); i++) {
				if (board[kingStart.getRow()][i].getPiece() != null) 
					return false;
			}
			
			King king = (King) kingStart.getPiece();
			//check if the king and rook are on the 1st rank, or row 0 
			//this is if the king and rook are white, so white castling
			if (kingStart.getRow() == 0 && rookStart.getRow() == 0 && king.getColor().equals("white")) {
				if (rookStart.getColNum() > kingStart.getColNum() 
						&& kingEnd.getColNum()-kingStart.getColNum() != 2) {
					return false;
				} else if (rookStart.getColNum() < kingStart.getColNum()
						&& kingEnd.getColNum()-kingStart.getColNum() != -2) {
					return false;
				}
			} 
			//check if the king and rook are on the 8th rank, or row 7
			//this is if the king and rook are black, black castling
			else if (kingStart.getRow() == 7 && rookStart.getRow() == 7 && king.getColor().equals("black")) {
				if (rookStart.getColNum() > kingStart.getColNum() 
						&& kingEnd.getColNum()-kingStart.getColNum() != 2) {
					return false;
				} else if (rookStart.getColNum() < kingStart.getColNum()
						&& kingEnd.getColNum()-kingStart.getColNum() != -2) {
					return false;
				}
			}
			
			/* if any of the pieces on the king's path are being attacked */
			if (rookStart.getColNum() > kingStart.getColNum() && 
				(Square.isSquareBeingAttacked(board, board[kingStart.getRow()][kingStart.getColNum()+1], 
						ChessBoard.getOpponentColor(kingStart.getPiece().getColor()))
				|| Square.isSquareBeingAttacked(board, board[kingStart.getRow()][kingStart.getColNum()+2],
						ChessBoard.getOpponentColor(kingStart.getPiece().getColor())))) {
				return false;
			}
			else if (rookStart.getColNum() < kingStart.getColNum() && 
					(Square.isSquareBeingAttacked(board, board[kingStart.getRow()][kingStart.getColNum()-1], 
							ChessBoard.getOpponentColor(kingStart.getPiece().getColor()))
					|| Square.isSquareBeingAttacked(board, board[kingStart.getRow()][kingStart.getColNum()-2],
							ChessBoard.getOpponentColor(kingStart.getPiece().getColor())))) {
					return false;
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * checks if the square contains a King
	 * @param board the board
	 * @param square the square to check
	 */
	public static boolean isKingOnSquare(Square square) {
		/* testing if square actually contains a King */
		boolean result = false;
		if (square != null && square.getPiece() != null &&
				square.getPiece() instanceof King) {
			result = true;
		}
		return result;
	}
	
	/**
	 * gets the king of a specific color that is in the board
	 * @param board the whole board
	 * @param color the color to check
	 * @return the king that is in the board for the color
	 */
	public static Square getKing(Square[][] board, String color) {
		Square kings = null;
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col].getPiece() != null && 
						board[row][col].getPiece() instanceof King && 
						board[row][col].getPiece().getColor().equals(color)) {
					kings = (board[row][col]);
				}
			}
		}
		return kings;
	}
	
	
	/**
	 * Checks if a king is in check
	 * @param board the board with the rest of the pieces
	 * @param square the square the king is in 
	 * @return is the king is in check or not
	 */
	public static boolean isKingInCheck(Square[][] board, Square square) {
		boolean result = false;
		if (square.getPiece() != null) {
			String opponentColor = ChessBoard.getOpponentColor(square.getPiece().getColor());
			boolean isSquareAttacked = Square.isSquareBeingAttacked(board, square, opponentColor);
			if (isKingOnSquare(square) && isSquareAttacked)
				result = true;
		}
		return result;
	}
	
	/**
	 * Checks if a king is in checkmate
	 * @param board the rest of the pieces
	 * @param square the square the king is in
	 * @return if the king is in checkmate or not
	 */
	public static boolean isKingInCheckmate(Square[][] board, Square square,
			LinkedList<Move> previousMoves) {
		if (isKingOnSquare(square) &&
				Square.isSquareBeingAttacked(board, square, 
				ChessBoard.getOpponentColor(square.getPiece().getColor()))) {
			
			//the surrounding squares
			ArrayList<Square> surroundings = Square.getOneAway(board, square);
			
			for (int i = 0; i < surroundings.size(); i++) {
				if (square.getPiece().isValidMove(board, square, surroundings.get(i))) {
					return false;
				}
			}
			String color = square.getPiece().getColor();
			
			/* other pieces can move into the line of fire */
			ArrayList<Square> otherPieces = Bishop.getBishops(board, color);
			otherPieces.addAll(Knight.getKnights(board, color));
			otherPieces.addAll(Pawn.getPawns(board, color));
			otherPieces.addAll(Rook.getRooks(board, color));
			otherPieces.addAll(Queen.getQueen(board, color));
			
			ArrayList<Square> moveFromSquares = new ArrayList<Square>();
			ArrayList<Square> moveToSquares = new ArrayList<Square>();
			for (int i = 0; i < otherPieces.size(); i++) {
				ArrayList<Square> moveableSquares;
				if (otherPieces.get(i).getPiece() instanceof Pawn) {
					moveableSquares = otherPieces.get(i).getPiece()
							.getMoveableSquares(board, otherPieces.get(i), previousMoves);
					moveToSquares.addAll(moveableSquares);
					
				} else {
					moveableSquares = otherPieces.get(i).getPiece()
							.getMoveableSquares(board, otherPieces.get(i));
					moveToSquares.addAll(otherPieces.get(i).getPiece()
							.getMoveableSquares(board, otherPieces.get(i)));
				}
				for (int j = 0; j < moveableSquares.size(); j++) {
					moveFromSquares.add(otherPieces.get(i));
				}
			}
			
			//testing all the possible moves
			Piece capturedPiece;
			Piece movedPiece;
			for (int i = 0; i < moveToSquares.size(); i++) {
				capturedPiece = moveToSquares.get(i).getPiece();
				movedPiece = moveFromSquares.get(i).getPiece();
				
				int colAdder = 0;
				if (movedPiece instanceof Pawn && moveToSquares.get(i).getColNum() > moveFromSquares.get(i).getColNum()) {
					colAdder = 1;
				} else if (movedPiece instanceof Pawn && moveToSquares.get(i).getColNum() < moveFromSquares.get(i).getColNum()) {
					colAdder = -1;
				}
				
				/* en passant with white capturing */
				if (movedPiece instanceof Pawn && moveFromSquares.get(i).getRow() == 4 
						&& movedPiece.getColor().equals("white") && 
						Pawn.isEnPassant(board, previousMoves.getLast(), 
								moveFromSquares.get(i), moveToSquares.get(i), 
								board[moveFromSquares.get(i).getRow()]
										[moveToSquares.get(i).getColNum()+colAdder])) {

					Square enemyPawn = board[moveFromSquares.get(i).getRow()+1]
							[moveToSquares.get(i).getColNum()+colAdder];
					Piece capturedPawn = enemyPawn.getPiece();
					
					moveToSquares.get(i).setPiece(movedPiece);
					moveFromSquares.get(i).setPiece(null);
					enemyPawn.setPiece(null);
					if (!King.isKingInCheck(board, square)) {
						moveToSquares.get(i).setPiece(null);
						moveFromSquares.get(i).setPiece(movedPiece);
						enemyPawn.setPiece(capturedPawn);
						return false;
					}
					moveToSquares.get(i).setPiece(capturedPiece);
					moveFromSquares.get(i).setPiece(movedPiece);
					enemyPawn.setPiece(capturedPawn);
					
				} /* en passant with black capturing */
				else if (movedPiece instanceof Pawn && moveFromSquares.get(i).getRow() == 3
						&& movedPiece.getColor().equals("black") && 
						Pawn.isEnPassant(board, previousMoves.getLast(), 
								moveFromSquares.get(i), moveToSquares.get(i), 
								board[moveFromSquares.get(i).getRow()]
										[moveToSquares.get(i).getColNum()+colAdder])) {
					
					Square enemyPawn = board[moveFromSquares.get(i).getRow()+1]
							[moveToSquares.get(i).getColNum()+colAdder];
					Piece capturedPawn = enemyPawn.getPiece();
					moveToSquares.get(i).setPiece(movedPiece);
					moveFromSquares.get(i).setPiece(null);
					enemyPawn.setPiece(null);
					if (!King.isKingInCheck(board, square)) {
						moveToSquares.get(i).setPiece(null);
						moveFromSquares.get(i).setPiece(movedPiece);
						enemyPawn.setPiece(capturedPawn);
						return false;
					}
					moveToSquares.get(i).setPiece(capturedPiece);
					moveFromSquares.get(i).setPiece(movedPiece);
					enemyPawn.setPiece(capturedPawn);
					
				} else {
					moveToSquares.get(i).setPiece(movedPiece);
					moveFromSquares.get(i).setPiece(null);
					if (!King.isKingInCheck(board, square)) {
						moveToSquares.get(i).setPiece(capturedPiece);
						moveFromSquares.get(i).setPiece(movedPiece);
						return false;
					}
					moveToSquares.get(i).setPiece(capturedPiece);
					moveFromSquares.get(i).setPiece(movedPiece);
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public ArrayList<Square> getMoveableSquares(Square[][] board, 
			Square currentPos, LinkedList<Move> previousMoves) {
		ArrayList<Square> possible = Square.getOneAway(board, currentPos);
		ArrayList<Square> possibleCastle = new ArrayList<Square>();
		
		if (currentPos.getColNum() <= 5) {
			possibleCastle.add(board[currentPos.getRow()][currentPos.getColNum()+2]);
		}
		if (currentPos.getColNum() >= 2) {
			possibleCastle.add(board[currentPos.getRow()][currentPos.getColNum()-2]);
		}
		
		ArrayList<Square> moveable = new ArrayList<Square>();
		for (int i = 0; i < possible.size(); i++) {
			if (isValidMove(board, currentPos, possible.get(i))) {
				moveable.add(possible.get(i));
			}
		}
		
		for (int i = 0; i < possibleCastle.size(); i++) {
			Square rookStart = board[possible.get(i).getRow()][0];
			if (possible.get(i).getColNum() > currentPos.getColNum()) {
				rookStart = board[possible.get(i).getRow()][7];
			}
			if (isCastle(board, currentPos, possible.get(i), rookStart, previousMoves)) {
				moveable.add(possible.get(i));
			}
		}
		return moveable;
	}

	
}
