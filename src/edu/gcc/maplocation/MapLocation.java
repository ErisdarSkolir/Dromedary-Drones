/**
 * Generic class for location on the map, with children of pickup and dropoff classes
 */
package edu.gcc.maplocation;

import edu.gcc.xml.annotation.XmlSerializable;

/**
 * @author zack
 *
 */
@XmlSerializable(value = "id", autogenerate = true)
public class MapLocation {
	public static final transient int INVALID = 0x0;
	public static final transient int DROPOFF = 0x1;
	public static final transient int PICKUP = 0x2;

	@SuppressWarnings("unused")
	private long id;

	private int type;
	private int xCoord;
	private int yCoord;

	private String name;
	private String campus;

	public MapLocation(int x, int y, int type, String n, String campus) {
		this.xCoord = x;
		this.yCoord = y;
		this.type = type;
		this.name = n;
		this.campus = campus;
	}

	// calculates the length of a vector between 2 points on Map
	public double distance(MapLocation l) {
		int xSum = Math.abs(xCoord - l.xCoord);
		int ySum = Math.abs(yCoord - l.yCoord);
		return Math.sqrt((double) (xSum * xSum) + (ySum * ySum));
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	@Override
	public String toString() {
		return "MapLocation [type=" + type + ", xCoord=" + xCoord + ", yCoord=" + yCoord + ", name=" + name
				+ ", campus=" + campus + "]";
	}
}
