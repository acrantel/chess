package chessPieces;

import javax.swing.ImageIcon;

import chessStructure.Square;

public class TestingPiece extends Piece {
	private String color;
	
	public TestingPiece(String color) {
		this.color = color;
	}
	
	@Override
	public boolean isValidMove(Square[][] board, Square start, Square end) {
		return false;
	}

	@Override
	public ImageIcon getMiniImageIcon() {
		return null;
	}

	@Override
	public ImageIcon getImageIcon() {
		return null;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public void killPiece() {
	}

	@Override
	public String getColor() {
		return color;
	}
	
	
}
