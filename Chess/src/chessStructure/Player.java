package chessStructure;

public class Player {
	
	private final String color;
	public Player(String color) {
		if (color.toLowerCase().equals("white"))
			this.color = "white";
		else
			this.color = "black";
	}
	
	public String getColor() {
		return color;
	}
}
