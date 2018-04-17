/**
 * 
 */
package simulator;

/**
 * @author Taha Salman
 *
 */
public class Immovable extends Cell {
	
	public Immovable(int x, int y) {
		setPosition(x,y);
		setName("Immovable Object");
		setToken('I');
		setType('i');
	}
	
	public int [] bump(int [] bumperCoord) {
		return this.getPosition();
	}
	
}
