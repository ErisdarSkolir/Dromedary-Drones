package edu.gcc.order;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;

public class Order {
	private long timestamp;
	private Meal meal;
	private String customerName;

	private double distance = 0;
	public MapLocation dropoffLocation;
	private boolean examined = false;
	private List<Order> neighbors = new ArrayList<>();
	private int weight;
	private int deliveryTime;

	public Order(final String customerName, final MapLocation dropoffLocation, final Meal meal, final long timestamp) {
		this.customerName = customerName;
		this.meal = meal;
		this.timestamp = timestamp;
		this.dropoffLocation = dropoffLocation;
	}

	public Order(int w, int d) {
		weight = w;
		deliveryTime = d;
	}
	
	public Order(final MapLocation shoplocation) {
		dropoffLocation = shoplocation;
	}
	
	public double getDistanceTo(Order n) {
		return this.dropoffLocation.distance(n.dropoffLocation);
	}

	public int getWeight() {
		return weight;
	}

	public int getTime() {
		return deliveryTime;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getDistance() {

		return distance;
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
