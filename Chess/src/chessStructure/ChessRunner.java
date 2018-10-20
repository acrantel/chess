package chessStructure;

import java.util.TimerTask;

public class ChessRunner extends TimerTask {
	
	private ChessComponent comp;
	
	public ChessRunner(ChessComponent comp) {
		this.comp = comp;
	}
	@Override
	public void run() {
		comp.repaint();
	}

}
