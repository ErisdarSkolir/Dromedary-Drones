/**
 * Pickup Location on the Map, with specific methods for this type
 */
package edu.gcc.maplocation;

/**
 * @author Zack Orlaski
 *
 */
public class PickupLocation extends MapLocation {
	
	//default constructor
	public PickupLocation() {};

	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param n Location name
	 */
	public PickupLocation(int x, int y, String n) {
		super(x, y, n);
	}

}
