package edu.gcc.location;

enum locationType{
	PICKUP,
	DROPOFF
}

public class Location {
	int xCoord;
	int yCoord;
	locationType type;
	
	
	public  Location(int x, int y, locationType t) {
		xCoord = x;
		yCoord = y;
		type = t;
	}
	public void test() {
		System.out.println("Testing 123");
	}
}
