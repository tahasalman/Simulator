/**
 * 
 */
package simulator;

/**
 * @author Taha Salman
 *
 */
public class Movable extends Cell {
	
	public Movable(int x, int y) {
		setPosition(x,y);
		setName("Movable Object");
		setToken('M');
		setType('m');
	}
	
	
	public int [] bump(int [] bumperCoord) {
		int [] currentCoord = this.getPosition();
		
		if(currentCoord[0] == bumperCoord[0] && bumperCoord[1] == currentCoord[1]-1) {  //bumped from left -> move right
			currentCoord[1]++;
			return currentCoord;
		}
		else if(currentCoord[0] == bumperCoord[0] && bumperCoord[1] == currentCoord[1]+1) { //bumped from right-> move left
			currentCoord[1]--;
			return currentCoord;
		}
		else if(currentCoord[1] == bumperCoord[1] && bumperCoord[0] == currentCoord[0]-1) { //bumped from bottom -> move up
			currentCoord[0]++;
			return currentCoord;
		}
		else if(currentCoord[1] == bumperCoord[1] && bumperCoord[0] == bumperCoord[0]+1) { //bumped from top -> move down
			currentCoord[0]--;
			return currentCoord;
		}
		
		return currentCoord;
	}
}
