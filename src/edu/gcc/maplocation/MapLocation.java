/**
 * Generic class for location on the map, with children of pickup and dropoff classes
 */
package edu.gcc.maplocation;
import java.lang.Math;
/**
 * @author zack
 *
 */
public abstract class MapLocation {

	private int xCoord;
	private int yCoord;
	private String name;
	
	public double distance(MapLocation l) {
		int xSum = xCoord - l.xCoord;
		int ySum = yCoord - l.yCoord;
		double pythagorean = Math.sqrt(	Math.abs((xSum * xSum) - (ySum * ySum)));
		
		return pythagorean;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "xCoord: " + xCoord + "\n yCoord: " + yCoord + "\n name: " + name+ "\n";
	}
	
	
	
}
