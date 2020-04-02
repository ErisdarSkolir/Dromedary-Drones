/**
 * Dropoff Location on the Map, with specific methods for this type
 */
package edu.gcc.maplocation;

import edu.gcc.xml.annotation.XmlSerializable;

/**
 * @author Zack Orlaski
 *
 */
@XmlSerializable("id")
public class DropoffLocation extends MapLocation {

	/**
	 * 
	 */
	public DropoffLocation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param x
	 * @param y
	 * @param n
	 */
	public DropoffLocation(int x, int y, String n) {
		super(x, y, n);
	}

}
