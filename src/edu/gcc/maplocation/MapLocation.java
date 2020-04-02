/**
 * Generic class for location on the map, with children of pickup and dropoff classes
 */
package edu.gcc.maplocation;

import edu.gcc.xml.annotation.XmlSerializable;

/**
 * @author zack
 *
 */
@XmlSerializable("id")
public abstract class MapLocation {

	public int id;
	
	protected int xCoord;
	protected int yCoord;
	protected String name;
	
	public MapLocation() {
		xCoord = 0;
		yCoord = 0;
		name = "n/a";
	}
	
	MapLocation(int x, int y, String n) {
		xCoord = x;
		yCoord = y;
		name = n;
	}
	
	//calculates the length of a vector between 2 points on Map
	public double distance(MapLocation l) {
		int xSum = Math.abs(xCoord - l.xCoord);
		int ySum = Math.abs(yCoord - l.yCoord);
		double pythagorean = Math.sqrt((xSum * xSum) + (ySum * ySum));
		
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
		return "xCoord: " + xCoord + "\nyCoord: " + yCoord + "\nname: " + name+ "\n";
	}
	
}
