package simulator;

/**
 * @author Taha Salman
 *
 */

public class Cell {
	private String name;
	private char token = ' ';
	private int x;
	private int y;
	protected char type = 'e';
	
	
	public char getToken() {
		return this.token;
	}
	
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setToken(char token) {
		this.token = token;
	}
	
	public void setType(char type) {
		this.type = type;
	}
	
	public int [] getPosition() {
		int [] position = {x,y};
		return position;
	}
	
	public char getType() {
		return this.type;
	}
	
	public int [] bump(int [] bumperCoord) {
		return this.getPosition();
	}
	
}
