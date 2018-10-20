package chessStructure;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import chessPieces.Piece;

public class ChessGame {
	
	public static void main(String[] args) {
		ChessComponent comp = new ChessComponent();
		
		class ClickListener implements MouseListener {

			@Override
			public void mouseClicked(MouseEvent e) {
				comp.recieveInput(e.getX(), e.getY());
				comp.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		}
		comp.addMouseListener(new ClickListener());
		JFrame frame = new JFrame();
		frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chess");
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(Piece.FILE_PATH_BEGINNING + "\\blackQueen.png"));
		} catch (IOException e) {}
		
		frame.setIconImage(img);
		
		frame.add(comp);
		frame.setVisible(true);
	}

}
