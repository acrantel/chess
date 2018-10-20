package chessStructure;

import java.util.ArrayList;

import chessPieces.Bishop;
import chessPieces.King;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.Piece;
import chessPieces.Queen;
import chessPieces.Rook;

public class Square 
{
	private int colNum;
	private char col;
	private int row;
	private Piece piece;

	/**
	 * Creates a square with specified row and column
	 * @param row a number between 0 and 7 (the default is 0)
	 * @param col a character from a to h (the default is a)
	 */
	public Square(int row, char col, Piece piece) {
		if (isNumberInRange(row+""))
			this.row = row;;
		
		switch (col) {
			case 'a': colNum = 0; break;
			case 'b': colNum = 1; break;
			case 'c': colNum = 2; break;
			case 'd': colNum = 3; break;
			case 'e': colNum = 4; break;
			case 'f': colNum = 5; break;
			case 'g': colNum = 6; break;
			case 'h': colNum = 7; break;
		}
		this.piece = piece;
	}
	
	/**
	 * returns the piece held in this square
	 */
	public Piece getPiece() {
		return piece;
	}
	
	/**
	 * replaces the current piece with the new piece
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	/**
	 * sets the currentPiece to null
	 */
	public void moveCurrentPiece() {
		piece = null;
	}
	
	public char getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColNum() {
		return colNum;
	}
	
	/**
	 * gets the column number: a=1, b=2...h=8
	 * @param col the col, between a and g
	 * @return the column number
	 */
	public static int getColNum(char col) {
		int num = 1;
		switch (col) {
			case 'a': num = 0; break;
			case 'b': num = 1; break;
			case 'c': num = 2; break;
			case 'd': num = 3; break;
			case 'e': num = 4; break;
			case 'f': num = 5; break;
			case 'g': num = 6; break;
			case 'h': num = 7; break;
		}
		return num;
	}
	
	/**
	 * gets the string of col: 1=a, 2=b...8=h
	 * @param num the number to translate, between 1 and 8
	 * @return the column as a character
	 */
	public static char getCol(String colNum) {
		char col = 'a';
		switch (colNum) {
			case "0": col = 'a'; break;
			case "1": col = 'b'; break;
			case "2": col = 'c'; break;
			case "3": col = 'd'; break;
			case "4": col = 'e'; break;
			case "5": col = 'f'; break;
			case "6": col = 'g'; break;
			case "7": col = 'h'; break;
		}
		return col;
	}
	
	/**
	 * checks if the string is a number
	 * from 1 to 8
	 * @param number the string to check
	 */
	public static boolean isNumberInRange(String number) {
		if (number.length() == 1) {
			boolean isGood = false;
			switch (number) {
				case "0": isGood = true; break;
				case "1": isGood = true; break;
				case "2": isGood = true; break;
				case "3": isGood = true; break;
				case "4": isGood = true; break;
				case "5": isGood = true; break;
				case "6": isGood = true; break;
				case "7": isGood = true; break;
				default: isGood = false; break;
			}
			return isGood;
		}
		else
			return false;
	}
	
	/**
	 * checks if a char is a letter from a-h
	 * @param letter the char to check
	 */
	public static boolean isLetterInRange(char letter) {
		boolean isGood = false;
		switch (letter) {
			case 'a': isGood = true; break;
			case 'b': isGood = true; break;
			case 'c': isGood = true; break;
			case 'd': isGood = true; break;
			case 'e': isGood = true; break;
			case 'f': isGood = true; break;
			case 'g': isGood = true; break;
			case 'h': isGood = true; break;
			default: isGood = false; break;
		}
		return isGood;
	}
	
	
	/**
	 * checks if a string is a letter from a-h
	 * @param letter the string to check
	 */
	public static boolean isLetterInRange(String letter) {
		boolean isGood = false;
		switch (letter) {
			case "a": isGood = true; break;
			case "b": isGood = true; break;
			case "c": isGood = true; break;
			case "d": isGood = true; break;
			case "e": isGood = true; break;
			case "f": isGood = true; break;
			case "g": isGood = true; break;
			case "h": isGood = true; break;
			default: isGood = false; break;
		}
		return isGood;
	}
	
	public static boolean isPieceName(String name) {
		boolean isGood = false;
		switch (name) {
			case "K": isGood = true; break;
			case "Q": isGood = true; break;
			case "R": isGood = true; break;
			case "N": isGood = true; break;
			case "B": isGood = true; break;
			case "": isGood = true; break;
		}
		return isGood;
	}
	
	/**
	 * checks if the square is being attack, not taking into account 
	 * possible pins or check/checkmate, or en passant
	 * @param board the rest of the board
	 * @param square
	 * @return
	 */
	public static boolean isSquareBeingAttacked(Square[][] board,
			Square square, String opponentColor) {
		boolean result = false;

		//squares that a knight can attack from
		if (isAnythingSimilar(Knight.getKnights
				(board, opponentColor), getLAway(board, square)))
			result = true;

		//squares that a rook can attack from
		ArrayList<Square> rooks = getAllVerticalAway(board, square, opponentColor);
		rooks.addAll(getAllHorizontalAway(board, square, opponentColor));
		if (isAnythingSimilar(Rook.getRooks(board, opponentColor), rooks)) 
			result = true;
		
		//squares that a bishop can attack from
		ArrayList<Square> bishops = getAllDiagonalsAway(board, square, opponentColor);
		if (isAnythingSimilar(Bishop.getBishops(board, opponentColor), bishops))
			result = true;
		
		//queen can attack from
		bishops.addAll(rooks);
		if (isAnythingSimilar(Queen.getQueen(board, opponentColor), bishops))
			result = true;
		
		//squares that a king can attack from
		ArrayList<Square> kingAttack = getOneAway(board, square);
		ArrayList<Square> king = new ArrayList<Square>();
		king.add(King.getKing(board, opponentColor));
		if (isAnythingSimilar(king, kingAttack)) {
			result = true;
		}
		
		//check if a pawn is attacking
		ArrayList<Square> pawnAttack = getPossiblePawnAttack(board, square);
		ArrayList<Square> opponentPawnAttack = new ArrayList<Square>();
		for (int i = 0; i < pawnAttack.size(); i++) {
			if (pawnAttack.get(i).getRow() > square.getRow() && opponentColor.equals("black"))
				opponentPawnAttack.add(pawnAttack.get(i));
			else if (pawnAttack.get(i).getRow() < square.getRow() && opponentColor.equals("white"))
				opponentPawnAttack.add(pawnAttack.get(i));
		}
		if (isAnythingSimilar(opponentPawnAttack, Pawn.getPawns(board, opponentColor))) {
			result = true;
		}
		
		return result;
	}
	
	
	/**
	 * checks if there is any of the same squares in the two ArrayLists
	 * @param list1 the first list that will be used for comparing
	 * @param list2 the second list that will be used for comparing
	 */
	public static boolean isAnythingSimilar(ArrayList<Square> list1, ArrayList<Square> list2) {
		boolean result = false;
		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				if (list1.get(i).equals(list2.get(j))) {
					result = true;
				}
			}
		}
		return result;
	}
	
	public static ArrayList<Square> getPossiblePawnAttack(Square[][] board, Square square) {
		ArrayList<Square> attackSquares = new ArrayList<Square>();
		if (square.getRow()-1 >= 0 && square.getColNum()-1 >= 0) 
			attackSquares.add(board[square.getRow()-1][square.getColNum()-1]);
		if (square.getRow()-1 >= 0 && square.getColNum()+1 <= 7)
			attackSquares.add(board[square.getRow()-1][square.getColNum()+1]);
		if (square.getRow()+1 <= 7 && square.getColNum()-1 >= 0)
			attackSquares.add(board[square.getRow()+1][square.getColNum()-1]);
		if (square.getRow()+1 <= 7 && square.getColNum()+1 <= 7) {
			attackSquares.add(board[square.getRow()+1][square.getColNum()+1]);
		}
		
		return attackSquares;
	}
	
	/**
	 * gets the squares that are diagonally away from
	 * the square, that are before any obstructions, such
	 * as a piece of your color or opponent's color
	 * @param board the board
	 * @param square the square to start from
	 * @return
	 */
	public static ArrayList<Square> getAllDiagonalsAway(Square[][] board, 
			Square square, String opponentColor) {
		ArrayList<Square> squares = new ArrayList<Square>();
		
		//starts from 'square' and goes bottom left direction
		squares.addAll(getSquaresInDirection(board, square, -1, -1, opponentColor));
		
		//starts from 'square' and goes top right direction
		squares.addAll(getSquaresInDirection(board, square, 1, 1, opponentColor));
		
		//starts from 'square' and goes top left direction
		squares.addAll(getSquaresInDirection(board, square, 1, -1, opponentColor));
		
		//starts from 'square' and goes bottom right direction
		squares.addAll(getSquaresInDirection(board, square, -1, 1, opponentColor));
		
		return squares;
	}
	
	/**
	 * gets the squares in the direction of row + addRow, col + addCol
	 * @param board the board
	 * @param start the starting square
	 * @param addRow the amount to add to the row
	 * @param addCol
	 * @return
	 */
	private static ArrayList<Square> getSquaresInDirection(
			Square[][] board, Square start, int addRow, int addCol, String opponentColor) {
		ArrayList<Square> squares = new ArrayList<Square>();
		
		int currentRow = start.getRow() + addRow;
		int currentCol = start.getColNum() + addCol;
		
		while (currentRow >= 0 && currentRow <= 7 && currentCol >= 0 && currentCol <= 7) {
			if (board[currentRow][currentCol].getPiece() == null)
				squares.add(board[currentRow][currentCol]);
			else if (board[currentRow][currentCol].getPiece().getColor().equals(opponentColor)) {
				squares.add(board[currentRow][currentCol]);
				break;
			} else {
				break;
			}
			currentRow += addRow;
			currentCol += addCol;
		}
		return squares;
	}
	
	/**
	 * gets the squares that are horizontally away from
	 * the parameter
	 * @param square
	 * @return
	 */
	public static ArrayList<Square> getAllHorizontalAway(Square[][] board, Square square, String opponentColor) {
		ArrayList<Square> squares = new ArrayList<Square>();
		squares.addAll(getSquaresInDirection(board, square, 0, -1, opponentColor));
		squares.addAll(getSquaresInDirection(board, square, 0, 1, opponentColor));
		return squares;
	}
	
	/**
	 * gets the squares that are vertically away
	 * @param square
	 * @return
	 */
	public static ArrayList<Square> getAllVerticalAway(Square[][] board, Square square, String opponentColor) {
		ArrayList<Square> squares = new ArrayList<Square>();
		squares.addAll(getSquaresInDirection(board, square, 1, 0, opponentColor));
		squares.addAll(getSquaresInDirection(board, square, -1, 0, opponentColor));
		return squares;
	}
	
	/**
	 * gets the squares that are two squares 
	 * horizontally and one square vertically 
	 * away, or two squares vertically and one 
	 * square horizontally away.
	 * @param square
	 * @return
	 */
	public static ArrayList<Square> getLAway(Square[][] board, Square square) {
		ArrayList<Square> squares = new ArrayList<Square>();

		int[] rows = {square.getRow()+2, square.getRow()+1, 
				square.getRow()-1, square.getRow()-2};
		int[] cols = {square.getColNum() + 2, square.getColNum() + 1, 
				square.getColNum() - 1, square.getColNum() - 2};
		
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				if (rows[i] <= 7 && rows[i] >= 0 && cols[j] <= 7 && cols[j] >= 0
						&& Math.abs(square.getRow()-rows[i]) != Math.abs(square.getColNum()-cols[j])) {
					squares.add(board[rows[i]][cols[j]]);
				}
			}
		}
		return squares;
	}

	/**
	 * gets the squares that are one square horizontally or 
	 * one vertically or both away
	 * @param board the board
	 * @param square the square to start from
	 * @return
	 */
	public static ArrayList<Square> getOneAway(Square[][] board, Square square) {
		ArrayList<Square> squares = new ArrayList<Square>();
		
		int[] rows = {square.getRow()-1, square.getRow(), square.getRow()+1};
		int[] cols = {square.getColNum()-1, square.getColNum(), square.getColNum()+1};
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				if (rows[i] >= 0 && rows[i] <= 7 && cols[j] >= 0 && cols[j] <= 7
						&& !board[rows[i]][cols[j]].equals(square)) {
					squares.add(board[rows[i]][cols[j]]);
				}
			}
		}
		return squares;
	}

	@Override
	public Square clone() {
		Piece clonedPiece = null;
		if (piece != null) {
			if (piece instanceof King) 
				clonedPiece = new King(piece.getColor());
			else if (piece instanceof Bishop) 
				clonedPiece = new Bishop(piece.getColor());
			else if (piece instanceof Knight) 
				clonedPiece = new Knight(piece.getColor());
			else if (piece instanceof Pawn) 
				clonedPiece = new Pawn(piece.getColor());
			else if (piece instanceof Rook) 
				clonedPiece = new Rook(piece.getColor());
			else if (piece instanceof Queen) 
				clonedPiece = new Queen(piece.getColor());
		}
		return new Square(row, col, clonedPiece);
	}
}
