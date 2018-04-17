/**
 * 
 */
package simulator;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.JFrame.*;
import java.util.Scanner;

/**
 * @author Taha.Salman
 *
 */
public class World {
	
	private Cell [][] world;
	private ArrayList<Autonomous> autonomousObjects = new ArrayList<Autonomous>();
	private JFrame mainFrame;
	
	
	public World(int dimensions) {
		this.world = new Cell[dimensions][dimensions];
		for(int i=0; i<this.world.length; i++) {
			for(int j=0; j<this.world[i].length;j++) {
				this.world[i][j] = new Cell();
			}
		}
		prepareGUI();
	}
	
	public void prepareGUI() {
		int horizontal = this.world.length;
		int vertical = this.world.length;
		this.mainFrame = new JFrame("Simulator");
		mainFrame.setSize(50*horizontal,50*vertical);
	}
	
	public void display() {
		int horizontal = this.world.length;
		int vertical = this.world.length;
		this.mainFrame.dispose();
		this.mainFrame = new JFrame("Simulator");
		mainFrame.setSize(50*horizontal,50*vertical);
		mainFrame.setLayout(new GridLayout(horizontal,vertical));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel [][] cells = new JLabel[this.world.length][this.world.length];
		for(int i = 0; i < horizontal; i++) {
			for(int j = 0; j < vertical; j++) {
				String l = ""+this.world[i][j].getToken();
				JLabel label = new JLabel(l);
				label.setHorizontalAlignment(JLabel.CENTER);
				this.mainFrame.add(label);
			}
		}
		this.mainFrame.setVisible(true);
	}
	
	public void display2() {
		int horizontal = this.world.length;
		int vertical = this.world.length;
		mainFrame.setLayout(new GridLayout(horizontal,vertical));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void step() {
		int [] toMoveTo;
		ArrayList<Integer []> movablePositions;
		for(int i=0; i<autonomousObjects.size(); i++) {
			movablePositions = getMovablePositions(autonomousObjects.get(i));
			if(!movablePositions.isEmpty()) {			//check if object can actually move
				toMoveTo = autonomousObjects.get(i).step();
				
				while(!(movablePositions.contains(toMoveTo)))
					toMoveTo = autonomousObjects.get(i).step();
				
				move(autonomousObjects.get(i), toMoveTo);
			}
		}
	}
	
	
	public boolean move(Cell c, int [] toMoveTo) {
		if(toMoveTo[0] >0 && toMoveTo[0] < this.world.length && toMoveTo[1] >0 && toMoveTo[1] < this.world.length) { // only move if toMoveTo is a valid coordinate
			int [] bumpedPosition;
			int [] currentPos = c.getPosition();
			Cell bumpedObject;
			
			if(this.world[toMoveTo[0]][toMoveTo[1]].getType() == 'e') {		//if moving to empty cell
				this.add(c, toMoveTo[0],toMoveTo[1]);
				this.remove(currentPos[0],currentPos[1]);
				return true;
			}
			else {
				bumpedObject = this.world[toMoveTo[0]][toMoveTo[1]];
				bumpedPosition = bumpedObject.bump(currentPos);
				if(!(areCoordinatesEqual(bumpedPosition, toMoveTo))) { //Checks if object can move
					if(move(bumpedObject, bumpedPosition)) {
						this.add(bumpedObject, bumpedPosition[0], bumpedPosition[1]);
						this.add(c, currentPos[0], currentPos[1]);
						return true;
					}
					else
						return false;
				}
				else
					return false;
			}
		}
		else
			return false;
	}
	
	//add cell object to position (x,y)
	public void add(Cell obj, int x, int y) throws IllegalArgumentException{
		if(x < 0 || x > this.world.length || y < 0 || y > this.world.length)
			throw new IllegalArgumentException("Please enter valid coordinates within map limits and make sure the position is empty!");
		else {
			this.world[x][y] = obj;
			obj.setPosition(x, y);
		}
	}
	
	//Nullify the cell at coordinates (x,y)
	public void remove(int x, int y) throws IllegalArgumentException{
		if(x < 0 || x > this.world.length || y < 0 || y > this.world.length || this.world[x][y] == null)
			throw new IllegalArgumentException("Please enter valid coordinates within map limits and make sure the position is not empty!");
		else {
			this.world[x][y] = null;
		}
	}
	
	//return true if two coordinates/arrays are equal; false otherwise
	
	public ArrayList<Integer[]> getMovablePositions(Cell obj) {
		int [] currentPosition = obj.getPosition();
		int x = currentPosition[0]; 
		int y = currentPosition[1];
		
		ArrayList<Integer[]> movablePositions= new ArrayList<Integer[]>();
		HashMap<Cell, Integer[]> surroundingCells= new HashMap<Cell,Integer[]>();
		
		surroundingCells.put(this.world[x][y+1], new Integer[]{x,y+1}); //right
		surroundingCells.put(this.world[x][y-1] , new Integer[]{x, y-1}); //left
		surroundingCells.put(this.world[x+1][y], new Integer[] {x+1,y}); //top
		surroundingCells.put(this.world[x-1][y], new Integer[] {x-1,y}); //bottom
		
		for(Cell c : surroundingCells.keySet()) {
			if(!(c instanceof Immovable))
				movablePositions.add(surroundingCells.get(c));
		}
		
		return movablePositions;
	}
	
	
	public static boolean areCoordinatesEqual(int [] coord1, int [] coord2) {
		if(!(coord1.length == coord2.length))
			return false;
		for(int i=0; i<coord1.length; i++) {
			if(coord1[i] != coord2[i])
				return false;
		}
		return true;
	}
	private static World instanceOfWorld;
	private static void buildWorld(int autonomous, int movable, int immovable, int dimensions) throws IllegalArgumentException{
		int total = autonomous + movable + immovable;
		if(total > dimensions*dimensions)
			throw new IllegalArgumentException("You have more objects than cells in the map!");
		else {
			instanceOfWorld = new World(dimensions);
			
			//generate a list of random coordinates to populate with objects
			int [][] coordMap = new int[total][2];
			int [] tempCoord;
			for(int i=0; i<total; i++) {
				tempCoord = generateRandomCoords(dimensions,dimensions);
				while(checkIfCoordExist(coordMap,tempCoord, i))
					tempCoord = generateRandomCoords(dimensions,dimensions);
				coordMap[i] = tempCoord;
			}
			
			int count =0;
			
			for(; count<autonomous;count++) {
				Autonomous obj = new Autonomous(coordMap[count][0], coordMap[count][1]);
				instanceOfWorld.add(obj, coordMap[count][0],coordMap[count][1]);
			}
			
			for(;count<autonomous+movable; count++) {
				Movable obj = new Movable(coordMap[count][0], coordMap[count][1]);
				instanceOfWorld.add(obj, coordMap[count][0],coordMap[count][1]);
			}
			
			for(;count<autonomous+movable+immovable; count++) {
				Immovable obj = new Immovable(coordMap[count][0], coordMap[count][1]);
				instanceOfWorld.add(obj, coordMap[count][0],coordMap[count][1]);
			}
		}
		
	}
	
	public static World getInstanceOfWorld(){
		return instanceOfWorld;
	}
	
	public static int [] generateRandomCoords(int xAxis, int yAxis) {
		int x = (int)(Math.random()*xAxis); 
		int y = (int)(Math.random()*yAxis);
		return new int[]{x,y};
	} 
	
	public static boolean checkIfCoordExist(int [][] map, int [] coord, int indexTillSearch) {
		for(int i=0; i<indexTillSearch; i++) {
				if(areCoordinatesEqual(map[i], coord))
					return true;
			
		}
		return false;
	}
	
	public static void main(String args []) {
		buildWorld(2,3,5,10);
		World w = getInstanceOfWorld();
		w.display();
		Scanner scanner = new Scanner(System.in);
		
		boolean simulateAgain = true;
		while(simulateAgain) {
			for(int i=0; i<100;i++) {
				w.step();
				w.display();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			simulateAgain = false;
		}
	}
	
}
