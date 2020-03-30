/**
 * Pickup Location on the Map, with specific methods for this type
 */
package edu.gcc.maplocation;

import java.util.List;

/**
 * @author Zack Orlaski
 *
 */
public class PickupLocation extends MapLocation {
	
	private List<MapLocation> dropoff_locations;
	
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
	
	/**
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param n Location name
	 * @param 
	 */
	public PickupLocation(int x, int y, String n, List<MapLocation> dropoff_locations) {
		super(x, y, n);
		this.dropoff_locations = dropoff_locations;
	}
	
	public List<MapLocation> getDropoffLocations() {
		return this.dropoff_locations;
	}

	public void addDropoffLocation(DropoffLocation temp) {
		this.dropoff_locations.add(temp);		
	}
}
