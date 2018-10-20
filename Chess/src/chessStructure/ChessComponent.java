package chessStructure;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
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
 * castling is a king move
 * @author seren
 *
 */
public class ChessComponent extends JComponent{
	
	private static final long serialVersionUID = 1L;

	/** the ChessBoard */
	private final ChessBoard chess = new ChessBoard();
	
	/** the board */
	private final Square[][] board = chess.getBoard();
	
	/** the currently selected item */
	private Square selected = null;
	
	/** the color the player was  */
	private String player = "white";
	/** the captured white pieces */
	private ArrayList<Piece> capturedWhite = new ArrayList<Piece>();
	/** the captured black pieces */
	private ArrayList<Piece> capturedBlack = new ArrayList<Piece>();
	
	/** the previous move */
	private LinkedList<Move> previousMoves = new LinkedList<Move>();
	
	/** if the player can select the promotion or not */
	private boolean isPromotionSelectionPossible = false;
	
	/** if the game is over or not */
	private boolean gameOver = false;
	
	/** the undo button image */
	private ImageIcon img;
	
	public ChessComponent() 
	{
		File file = new File(Piece.FILE_PATH_BEGINNING+"undoArrow.png");
		Image image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {}
		image = image.getScaledInstance(UNDO_WIDTH-4, UNDO_HEIGHT-4, 0);
		img = new ImageIcon(image);
	}
	
	public void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		drawBoard(g2);
		
		//the undo button
		g2.setColor(Color.GRAY);
		g2.draw3DRect(UNDO_BUTTON_X, UNDO_BUTTON_Y, UNDO_WIDTH, UNDO_HEIGHT, true);
		img.paintIcon(this, g2, UNDO_BUTTON_X+3, UNDO_BUTTON_Y+2);
		
		
		//promotion selection
		if (this.isPromotionSelectionPossible) {
			g2.setStroke(new BasicStroke(2));
			
			for (int i = 0; i < 4; i++) {
				g2.setColor(Color.CYAN.darker());
				g2.fillRect(PROMOTION_X, BOARD_Y+i*PROMOTION_UNIT, PROMOTION_UNIT, PROMOTION_UNIT);
				g2.setColor(Color.BLACK);
				g2.drawRect(PROMOTION_X, BOARD_Y+i*PROMOTION_UNIT, PROMOTION_UNIT, PROMOTION_UNIT);
			}
			
			ImageIcon[] promotionIcons = null;
			//player is the opposite of the color to be drawn
			if (player.equals("black")) {
				promotionIcons = whitePromotionIcons;
			} else if (player.equals("white")) {
				promotionIcons = blackPromotionIcons;
			}
			
			for (int i = 0; i < 4; i++) {
				ChessBoard.drawChessPiece(PROMOTION_X, BOARD_Y+i*PROMOTION_UNIT, promotionIcons[i], g2, this);
			}
		}
		
		//draws the highlighted squares showing where the piece on selected can move
		if (selected != null && selected.getPiece() != null) {
			g2.setColor(new Color(204, 102, 0, 100));
			ArrayList<Square> moveable = new ArrayList<Square>();
			if (selected.getPiece() instanceof King || selected.getPiece() instanceof Pawn) {
				moveable = selected.getPiece().getMoveableSquares(board, selected, previousMoves);
			} else {
				moveable = selected.getPiece().getMoveableSquares(board, selected);
			}
			
			for (int i = 0; i < moveable.size(); i++) {
				Square square = moveable.get(i);
				g2.fillRect((square.getColNum())*UNIT + BOARD_X, (7-square.getRow())*UNIT + BOARD_Y, UNIT, UNIT);
			}
		}
		
		//if the game is over
		if (gameOver) {
			g2.setColor(Color.PINK);
			g2.setFont(new Font("Chiller", Font.ITALIC, UNIT));
			g2.drawString("GAME OVER :)", UNIT/2, UNIT*9+UNIT);
		}
	}
	
	public void drawBoard(Graphics2D g2) {
		Rectangle rect = new Rectangle(BOARD_X, BOARD_Y, UNIT, UNIT);
		Color dark = new Color(153, 200, 102);
		Color light = new Color(250, 250, 250);
		Color currentColor = light;
		/* colors in this order: 
		 * a8,b8,c8,d8,e8,f8,g8,a7,b7... */
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				g2.setColor(currentColor);
				g2.fill(rect);
				rect.translate(UNIT, 0);
				if (currentColor.equals(light) && col != 7)
					currentColor = dark;
				else if (col != 7)
					currentColor = light;
			}
			rect.translate(-8*UNIT, UNIT);
		}
		
		for (int row = board.length-1; row >= 0; row--) {
			for (int col = board[row].length-1; col >= 0; col--) {
				if (board[row][col].getPiece() != null) {
					ChessBoard.drawChessPiece(BOARD_X+UNIT*(col), BOARD_Y+UNIT*(board.length-1-row), 
							board[row][col].getPiece().getImageIcon(), g2, this);
				}
			}
		}
		if (selected != null) {
			g2.setColor(Color.MAGENTA);
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(BOARD_X+UNIT*selected.getColNum(), BOARD_Y+UNIT*(board.length-1-selected.getRow()), UNIT, UNIT);
		}
		
		/* drawing the captured pieces */
		for (int i = 0; i < capturedBlack.size(); i++) {
			ChessBoard.drawChessPiece(BOARD_X+(UNIT/2)*i, BOARD_Y+UNIT*8, capturedBlack.get(i).getMiniImageIcon(), g2, this);
		}
		
		for (int i = 0; i < capturedWhite.size(); i++) {
			ChessBoard.drawChessPiece(BOARD_X+(UNIT/2)*i, BOARD_Y-UNIT/2, capturedWhite.get(i).getMiniImageIcon(), g2, this);
		}
	}
	
	/**
	 * recieves the point that was clicked
	 * @param pointX
	 * @param pointY
	 */
	public void recieveInput(int pointX, int pointY) {
		
		if (pointX > BOARD_X && pointX < BOARD_X+UNIT*8 
				&& pointY > BOARD_Y && pointY < BOARD_Y+UNIT*8
				&& !isPromotionSelectionPossible
				&& ((!King.isKingInCheckmate(board, King.getKing(board, "white"), previousMoves))
				&& (!King.isKingInCheckmate(board, King.getKing(board, "black"), previousMoves)))
				&& !this.isStalemate()) {
			
			int x = pointX-BOARD_X;
			int y = pointY-BOARD_Y;
			x = x-(x%UNIT);
			y = y-(y%UNIT);
			
			Square square = board[board.length-1-y/UNIT][x/UNIT];
			if (isPromotionSelectionPossible) {
			}
			/* if the clicked square is a piece in the current player's color */
			else if (square.getPiece() != null && square.getPiece().getColor().equals(player)) {
				selected = board[board.length-1-y/UNIT][x/UNIT];
			} 
			/** if it is promotion */
			else if (selected != null && selected.getPiece() != null 
					&& Pawn.isPawnOnSquare(selected) && 
					((Pawn)selected.getPiece()).isPromotion(board, selected, square)) {
				
				if (square.getPiece() != null && square.getPiece().getColor().equals("black"))
					capturedBlack.add(square.getPiece());
				else if (square.getPiece() != null)
					capturedWhite.add(square.getPiece());
				
				//adding to the stack
				previousMoves.add(new Move(selected.getPiece(), square.getPiece(), selected, square));
				
				square.setPiece(selected.getPiece());
				selected.setPiece(null);
				this.player = ChessBoard.getOpponentColor(player);
				selected = null;
				this.isPromotionSelectionPossible = true;
			}
			/** possible regular move */
			else if (selected != null && selected.getPiece() != null && 
					(square.getPiece() == null || 
					!square.getPiece().getColor().equals(selected.getPiece().getColor()))
					&& selected.getPiece().isValidMove(board, selected, square)) {
				
				if (square.getPiece() != null && square.getPiece().getColor().equals("black"))
					capturedBlack.add(square.getPiece());
				else if (square.getPiece() != null)
					capturedWhite.add(square.getPiece());
				
				//adding to the stack
				previousMoves.add(new Move(selected.getPiece(), square.getPiece(), selected, square));
				
				square.setPiece(selected.getPiece());
				selected.setPiece(null);
				this.player = ChessBoard.getOpponentColor(player);
				selected = null;
			}
			/** possible en passant */
			else if (Pawn.isPawnOnSquare(selected) && square.getPiece() == null
					&& previousMoves.size() != 0) {
				
				Square enemyPawn = null;
				if (player.equals("white")) {
					enemyPawn = board[square.getRow()-1][square.getColNum()];
				} else {
					enemyPawn = board[square.getRow()+1][square.getColNum()];
				}
				
				/* if all the conditions are met and the move is an en passant */
				if (Pawn.isEnPassant(board, previousMoves.getLast(), selected, square, enemyPawn)) {
					if (player.equals("white")) {
						capturedBlack.add(enemyPawn.getPiece());
					} else {
						capturedWhite.add(enemyPawn.getPiece());
					}
					
					//adding to the stack
					previousMoves.add(new Move(selected.getPiece(), 
							enemyPawn.getPiece(), selected, square, enemyPawn));
					
					enemyPawn.setPiece(null);
					square.setPiece(selected.getPiece());
					selected.setPiece(null);
					this.player = ChessBoard.getOpponentColor(player);
					selected = null;
				}
			}
			/* possible castling */
			else if (King.isKingOnSquare(selected) && square.getPiece() == null) {
				Square rookSquare = null;
				Square rookLanding = null;
				if (selected.getColNum() < square.getColNum()) {
					for (int i = selected.getColNum()+1; i < board[selected.getRow()].length; i++) {
						if (Rook.isRookOnSquare(board[selected.getRow()][i])) {
							rookSquare = board[selected.getRow()][i];
							break;
						}
					}
					rookLanding = board[square.getRow()][square.getColNum()-1];
				}
				else if (selected.getColNum() > square.getColNum()) {
					for (int i = selected.getColNum()-1; i >= 0; i--) {
						if (Rook.isRookOnSquare(board[selected.getRow()][i])) {
							rookSquare = board[selected.getRow()][i];
							break;
						}
					}
					rookLanding = board[square.getRow()][square.getColNum()+1];
				}

				/* if it is a valid move */
				if (rookSquare != null &&
						(King.isCastle(board, selected, square, rookSquare, previousMoves))) {
					previousMoves.add(new Move(selected.getPiece(), rookSquare.getPiece(), 
							selected, square, rookSquare, rookLanding));
					
					//do the moves
					square.setPiece(selected.getPiece());
					selected.setPiece(null);
					rookLanding.setPiece(rookSquare.getPiece());
					rookSquare.setPiece(null);
					selected = null;
					player = ChessBoard.getOpponentColor(player);
				}
			}
		} 
		/* if the player clicked inside the promotion box */
		else if (pointX >= PROMOTION_X && pointX <= PROMOTION_X+PROMOTION_UNIT
				&& pointY >= BOARD_Y && pointY <= BOARD_Y+PROMOTION_UNIT*4
				&& this.isPromotionSelectionPossible) {
			int selectedPieceIndex = 0;
			if (pointY <= BOARD_Y+PROMOTION_UNIT) {
				selectedPieceIndex = 0;
			} else if (pointY <= BOARD_Y+PROMOTION_UNIT*2) {
				selectedPieceIndex = 1;
			} else if (pointY <= BOARD_Y+PROMOTION_UNIT*3) {
				selectedPieceIndex = 2;
			} else if (pointY <= BOARD_Y+PROMOTION_UNIT*4) {
				selectedPieceIndex = 3;
			}
			
			if (promotionListOrder[selectedPieceIndex].equals("queen")) {
				previousMoves.getLast().getMoveTo().setPiece(null);
				Queen queen = new Queen(previousMoves.getLast().getMovedPiece().getColor());
				previousMoves.getLast().getMoveTo().setPiece(queen);
				previousMoves.set(previousMoves.size()-1, 
						new Move(previousMoves.getLast().getMovedPiece(), 
								previousMoves.getLast().getCapturedPiece(), queen, 
								previousMoves.getLast().getMoveFrom(), previousMoves.getLast().getMoveTo()));
				
			} else if (promotionListOrder[selectedPieceIndex].equals("rook")) {
				previousMoves.getLast().getMoveTo().setPiece(null);
				Rook rook = new Rook(previousMoves.getLast().getMovedPiece().getColor());
				previousMoves.getLast().getMoveTo().setPiece(rook);
				previousMoves.set(previousMoves.size()-1, 
						new Move(previousMoves.getLast().getMovedPiece(), 
								previousMoves.getLast().getCapturedPiece(), rook, 
								previousMoves.getLast().getMoveFrom(), previousMoves.getLast().getMoveTo()));
				
			} else if (promotionListOrder[selectedPieceIndex].equals("bishop")) {
				previousMoves.getLast().getMoveTo().setPiece(null);
				Bishop bishop = new Bishop(previousMoves.getLast().getMovedPiece().getColor());
				previousMoves.getLast().getMoveTo().setPiece(bishop);
				previousMoves.set(previousMoves.size()-1, 
						new Move(previousMoves.getLast().getMovedPiece(), 
								previousMoves.getLast().getCapturedPiece(), bishop, 
								previousMoves.getLast().getMoveFrom(), previousMoves.getLast().getMoveTo()));
			} else if (promotionListOrder[selectedPieceIndex].equals("knight")) {
				previousMoves.getLast().getMoveTo().setPiece(null);
				Knight knight = new Knight(previousMoves.getLast().getMovedPiece().getColor());
				previousMoves.getLast().getMoveTo().setPiece(knight);
				previousMoves.set(previousMoves.size()-1, 
						new Move(previousMoves.getLast().getMovedPiece(), 
								previousMoves.getLast().getCapturedPiece(), knight, 
								previousMoves.getLast().getMoveFrom(), previousMoves.getLast().getMoveTo()));
				
			}
			
			this.isPromotionSelectionPossible = false;
		}
		/** if the player hits the undo button */
		else if (pointX >= UNDO_BUTTON_X  && pointY >= UNDO_BUTTON_Y 
				&& pointX <= UNDO_BUTTON_X+UNDO_WIDTH && pointY <= UNDO_BUTTON_Y+UNDO_HEIGHT) {
			/** if the previous move was a regular move */
			if (previousMoves.size() != 0 && !previousMoves.getLast().isEnPassant() 
					&& !previousMoves.getLast().isCastle() && !previousMoves.getLast().isPromotion()) {
				Square moveFrom = previousMoves.getLast().getMoveFrom();
				Square moveTo = previousMoves.getLast().getMoveTo();
				Piece capturedPiece = previousMoves.getLast().getCapturedPiece();
				Piece movingPiece = previousMoves.getLast().getMovedPiece();
				
				//set the squares to the old pieces
				moveFrom.setPiece(movingPiece);
				moveTo.setPiece(capturedPiece);
				previousMoves.removeLast();
				
				//undoing player move
				this.player = ChessBoard.getOpponentColor(player);
				selected = null;
				
				//removing the captured piece from the list of captured pieces
				if (capturedPiece != null) {
					if (capturedPiece.getColor().equals("black")) {
						for (int i = 0; i < capturedBlack.size(); i++) {
							if (capturedBlack.get(i).equals(capturedPiece)) {
								capturedBlack.remove(i);
								break;
							}
						}
					} else {
						for (int i = 0; i < capturedWhite.size(); i++) {
							if (capturedWhite.get(i).equals(capturedPiece)) {
								capturedWhite.remove(i);
								break;
							}
						}
					}
				}
			}
			/** if the previous move was en passant */
			else if (previousMoves.size() != 0 && previousMoves.getLast().isEnPassant() 
					&& !previousMoves.getLast().isCastle() && !previousMoves.getLast().isPromotion()) {
				Square moveFrom = previousMoves.getLast().getMoveFrom();
				Square moveTo = previousMoves.getLast().getMoveTo();
				Square capturedSquare = previousMoves.getLast().getCapturedSquare();
				Piece capturedPiece = previousMoves.getLast().getCapturedPiece();
				Piece movingPiece = previousMoves.getLast().getMovedPiece();
				
				capturedSquare.setPiece(capturedPiece);
				moveFrom.setPiece(movingPiece);
				moveTo.setPiece(null);
				previousMoves.removeLast();
				
				this.player = ChessBoard.getOpponentColor(player);
				selected = null;
				if (capturedPiece.getColor().equals("black")) {
					for (int i = 0; i < capturedBlack.size(); i++) {
						if (capturedBlack.get(i).equals(capturedPiece)) {
							capturedBlack.remove(i);
							break;
						}
					}
				} else {
					for (int i = 0; i < capturedWhite.size(); i++) {
						if (capturedWhite.get(i).equals(capturedPiece)) {
							capturedWhite.remove(i);
							break;
						}
					}
				}
			}
			/** if the previous move was castling */
			else if (previousMoves.size() != 0 && !previousMoves.getLast().isEnPassant() 
					&& previousMoves.getLast().isCastle() && !previousMoves.getLast().isPromotion()) {
				Piece king = previousMoves.getLast().getKing();
				Piece rook = previousMoves.getLast().getRook();
				Square kingMoveFrom = previousMoves.getLast().getKingMoveFrom();
				Square kingMoveTo = previousMoves.getLast().getKingMoveTo();
				Square rookMoveFrom = previousMoves.getLast().getRookMoveFrom();
				Square rookMoveTo = previousMoves.getLast().getRookMoveTo();
				kingMoveTo.setPiece(null);
				kingMoveFrom.setPiece(king);
				rookMoveTo.setPiece(null);
				rookMoveFrom.setPiece(rook);
				previousMoves.removeLast();
				player = ChessBoard.getOpponentColor(player);
			} 
			/** if the previous move was promotion */
			else if (previousMoves.size() != 0 && !previousMoves.getLast().isEnPassant()
					&& !previousMoves.getLast().isCastle() && previousMoves.getLast().isPromotion()) {
				Piece formerPawn = previousMoves.getLast().getMovedPiece();
				Piece capturedPiece = previousMoves.getLast().getCapturedPiece();
				Square moveFrom = previousMoves.getLast().getMoveFrom();
				Square moveTo = previousMoves.getLast().getMoveTo();
				
				moveFrom.setPiece(formerPawn);
				moveTo.setPiece(capturedPiece);
				
				previousMoves.removeLast();
				player = ChessBoard.getOpponentColor(player);
				selected = null;
				
				if (capturedPiece.getColor().equals("black")) {
					for (int i = 0; i < capturedBlack.size(); i++) {
						if (capturedBlack.get(i).equals(capturedPiece)) {
							capturedBlack.remove(i);
							break;
						}
					}
				} else {
					for (int i = 0; i < capturedWhite.size(); i++) {
						if (capturedWhite.get(i).equals(capturedPiece)) {
							capturedWhite.remove(i);
							break;
						}
					}
				}
			}
			gameOver = false;
			this.isPromotionSelectionPossible = false;
		} 
		if (King.isKingInCheckmate(board, King.getKing(board, "white"), previousMoves)
				|| King.isKingInCheckmate(board, King.getKing(board, "black"), previousMoves)
				|| this.isStalemate()) {
			gameOver = true;
		}
	}
	
	public boolean isStalemate() {
		if (!isPromotionSelectionPossible) {
			ArrayList<Square> pieces = new ArrayList<Square>();
			/* getting all the pieces of the current player's color */
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board[row].length; col++) {
					if (board[row][col].getPiece() != null 
							&& board[row][col].getPiece().getColor().equals(player)) {
						pieces.add(board[row][col]);
					}
				}
			}
			
			ArrayList<Square> moveable = new ArrayList<Square>();
			for (int i = 0; i < pieces.size(); i++) {
				if (pieces.get(i).getPiece() instanceof King 
						|| pieces.get(i).getPiece() instanceof Pawn) {
					moveable.addAll(pieces.get(i).getPiece()
							.getMoveableSquares(board, pieces.get(i), previousMoves));
				} else {
					moveable.addAll(pieces.get(i).getPiece()
							.getMoveableSquares(board, pieces.get(i)));
				}
			}
			
			if (moveable.size() != 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	
	public static final int BOARD_X = 30;
	public static final int BOARD_Y = 30;
	public static final int UNIT = 75;
	
	public static final int UNDO_BUTTON_X = BOARD_X+UNIT*8+UNIT;
	public static final int UNDO_BUTTON_Y = (int)(BOARD_Y+ UNIT*3.75);
	public static final int UNDO_WIDTH = (int) (UNIT*.75);
	public static final int UNDO_HEIGHT = (int) (UNIT*.5);
	
	public static final int PROMOTION_UNIT = (int) (UNIT*1.25);
	public static final int PROMOTION_X = UNDO_BUTTON_X+UNDO_WIDTH + 40;
	
	public static final String[] promotionListOrder = {"queen", "rook", "bishop", "knight"};
	
	public static final ImageIcon[] blackPromotionIcons = {Queen.generatePromotionIcon("black"),
			Rook.generatePromotionIcon("black"), Bishop.generatePromotionIcon("black"),
			Knight.generatePromotionIcon("black")};
	public static final ImageIcon[] whitePromotionIcons = {Queen.generatePromotionIcon("white"),
			Rook.generatePromotionIcon("white"), Bishop.generatePromotionIcon("white"),
			Knight.generatePromotionIcon("white")};
	
	/** the amount to subtract from the image width/height */
	public static final int SIZE_MOD = 3;
	
}
