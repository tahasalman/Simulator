package simulator;


/**
 * @author Taha Salman
 *
 */

public class Autonomous extends Cell {
	
	public Autonomous(int x, int y) {
		setPosition(x,y);
		setName("Autonomous Object");
		setToken('A');
		setType('a');
	}
	
	public int [] bump(int [] bumperCoord) {
		int [] currentCoord = this.getPosition();
		
		if(currentCoord[0] == bumperCoord[0] && bumperCoord[1] == currentCoord[1]-1)  //bumped from left -> move right
			currentCoord[1]++;
		else if(currentCoord[0] == bumperCoord[0] && bumperCoord[1] == currentCoord[1]+1) //bumped from right-> move left
			currentCoord[1]--;
		else if(currentCoord[1] == bumperCoord[1] && bumperCoord[0] == currentCoord[0]-1)  //bumped from bottom -> move up
			currentCoord[0]++;
		else if(currentCoord[1] == bumperCoord[1] && bumperCoord[0] == bumperCoord[0]+1) //bumped from top -> move down
			currentCoord[0]--;
		
		return currentCoord;
	}
	
	public int [] step() {
		int [] currentCoord = this.getPosition();
		int random = (int)(Math.random()*4);
		
		if(random==0)
			currentCoord[0]--; //move down
		else if(random == 1)
			currentCoord[0]++; //move up
		else if(random == 2)
			currentCoord[1]--; //move left
		else
			currentCoord[1]++; //move right
		
		return currentCoord;
	}
}
