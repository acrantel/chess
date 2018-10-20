package chessStructure;

import chessPieces.Piece;

public class Move {
	/** the piece that was captured in this move */
	private Piece capturedPiece;
	/** the piece that was moved in this move */
	private Piece movingPiece;
	
	/** the Square that movingPiece moved from */
	private Square moveFrom;
	/** the Square that movingPiece movedTo */
	private Square moveTo;
	/** the Square that the opponent piece used to be on */
	private Square capturedSquare;
	/** the previous move */
	
	/* these are only used for castling */
	private Piece king;
	private Piece rook;
	private Square kingMoveFrom;
	private Square kingMoveTo;
	private Square rookMoveFrom;
	private Square rookMoveTo;
	
	/* this is used for promotion */
	private Piece changedTo;
	
	private boolean isEnPassant;
	private boolean isCastle;
	private boolean isPromotion;
	/**
	 * Creates a move from moveFrom to moveTo.  
	 * @param piece
	 * @param moveFrom
	 * @param moveTo
	 * @param previousMove the previous move
	 * @param capturedPiece 
	 */
	public Move(Piece movingPiece, Piece capturedPiece, Square moveFrom, Square moveTo) {
		this.movingPiece = movingPiece;
		this.capturedPiece = capturedPiece;
		this.moveFrom = moveFrom;
		this.moveTo = moveTo;
		this.isPromotion = false;
		this.isEnPassant = false;
		this.isCastle = false;
		this.capturedSquare = moveTo;
	}
	
	/**
	 * the constructor for en passants
	 */
	public Move(Piece capturingPawn, Piece capturedPawn, 
			Square moveFrom, Square moveTo, Square capturedSquare) {
		this.movingPiece = capturingPawn;
		this.capturedPiece = capturedPawn;
		this.moveFrom = moveFrom;
		this.moveTo = moveTo;
		this.isPromotion = false;
		this.isEnPassant = true;
		this.isCastle = false;
		this.capturedSquare = capturedSquare;
	}
	
	/**
	 * the constructor for castling
	 * there are no pieces captured
	 */
	public Move(Piece king, Piece rook,
			Square kingMoveFrom, Square kingLanding, 
			Square rookMoveFrom, Square rookLanding) {
		this.king = king;
		this.rook = rook;
		this.kingMoveFrom = kingMoveFrom;
		this.kingMoveTo = kingLanding;
		this.rookMoveFrom = rookMoveFrom;
		this.rookMoveTo = rookLanding;
		this.isPromotion = false;
		isCastle = true;
		isEnPassant = false;
	}
	
	/**
	 * The constructor for promotion, where there may be a piece captured
	 */
	public Move(Piece pawn, Piece capturedPiece, Piece pawnChangedTo,
			Square pawnMoveFrom, Square pawnMoveTo) {
		this.movingPiece = pawn;
		this.capturedPiece = capturedPiece;
		this.changedTo = pawnChangedTo;
		this.moveFrom = pawnMoveFrom;
		this.moveTo = pawnMoveTo;
	}
	
	public Piece getMovedPiece() {
		return movingPiece;
	}
	
	public Piece getCapturedPiece() {
		return capturedPiece;
	}
	
	public Square getMoveFrom() {
		return moveFrom;
	}
	public Square getMoveTo() {
		return moveTo;
	}
	
	
	public boolean isEnPassant() {
		return isEnPassant;
	}
	
	public boolean isCastle() {
		return isCastle;
	}
	
	/**
	 * the square where the enemy pawn was captured
	 * in en passant
	 * @return
	 */
	public Square getCapturedSquare() {
		return capturedSquare;
	}
	
	/**
	 * The square the king moved from, in castling*/
	public Square getKingMoveFrom() {
		return this.kingMoveFrom;
	}
	/**
	 * The square that the king moved to, in castling*/
	public Square getKingMoveTo() {
		return this.kingMoveTo;
	}
	/**
	 * The square that the rook moved from, in castling*/
	public Square getRookMoveFrom() {
		return this.rookMoveFrom;
	}
	/**
	 * The square that the rook moved to, in castling*/
	public Square getRookMoveTo() {
		return this.rookMoveTo;
	}
	
	/**
	 * The king that was moved in castling*/
	public Piece getKing() {
		return king;
	}
	
	/**
	 * The rook that was moved in castling*/
	public Piece getRook() {
		return rook;
	}
	
	/* PROMOTION */
	public Piece getMovedPawn() {
		return this.getMovedPiece();
	}
	
	public boolean isPromotion() {
		return this.isPromotion;
	}
	
	public Piece getChangedToPiece() {
		return this.changedTo;
	}
}
