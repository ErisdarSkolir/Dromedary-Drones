package GreedyBestFirst;

import java.util.ArrayList;

public class Order implements Comparable<Order>{

	private double distance = 0;
	public double x = 0;
	public double y = 0;
	private boolean examined = false;
	private ArrayList<Order> neighbors = new ArrayList<Order>();
	private int weight;
	private int deliveryTime;
	
	public Order(int w, int d) {
		weight = w;
		deliveryTime = d;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getTime() {
		return deliveryTime;
	}

	public Order(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getDistance() {
		
		return distance;
	}
	
	public void setDistance(double d) {
		distance = d;
	}

	public double getDistanceTo(Order n) {
		return Math.sqrt((n.x-x)*(n.x-x) + (n.y-y)*(n.y-y));
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
	
	/*public double getFlightTime(int i) {
		
	}*/

	public int compareTo(Order n) {
		return (int)(distance - n.getDistance());
	}

}
