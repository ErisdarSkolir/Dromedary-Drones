package edu.gcc.order;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;

public class Order {
	private long timestamp;
	private Meal meal;
	
	private double distance = 0;
	public MapLocation dropoffLocation;
	private boolean examined = false;
	private List<Order> neighbors = new ArrayList<>();
	private double weight;
	private int deliveryTime;
	private int timesPassed = 0;

	public Order(final MapLocation dropoffLocation, final Meal meal, final long timestamp) {
		this.meal = meal;
		this.timestamp = timestamp;
		this.dropoffLocation = dropoffLocation;
		this.weight = meal.getWeight();
	}
	
	public Order(final MapLocation shoplocation) {
		dropoffLocation = shoplocation;
	}

	public MapLocation getDropoffLocation() {
		return dropoffLocation;
	}

	public void setDropoffLocation(MapLocation dropoffLocation) {
		this.dropoffLocation = dropoffLocation;
	}
	
	public double getDistanceTo(Order n) {
		return this.dropoffLocation.distance(n.dropoffLocation);
	}

	public double getWeight() {
		return weight;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getDistance() {

		return distance;
	}
	
	public void incTimesPassed() {
		timesPassed++;
	}
	
	public int getTimesPassed() {
		return timesPassed;
	}

	public void setDistance(double d) {
		distance = d;
	}
	
	public void setExamined(boolean b) {
		examined = b;
	}

	public boolean getExamined() {
		return examined;
	}

	public int numNeighbors() {
		return 1;
	}

	public void addNeighbor(Order n) {
		neighbors.add(n);
	}

	public Order getNeighbor(int i) {
		return neighbors.get(i);
	}
}
