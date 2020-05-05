package edu.gcc.order;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;

/**
 * This class represents an order and data needed to deliver it.
 * 
 * @author Ethan Harvey, Lake Pry, Luke Donmoyer
 */
public class Order {
	private long timestamp;

	private int timesPassed;

	private double distance;
	private double weight;

	private boolean examined = false;

	private MapLocation dropoffLocation;
	private List<Order> neighbors = new ArrayList<>();

	public Order(
			final MapLocation dropoffLocation,
			final Meal meal,
			final long timestamp
	) {
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
