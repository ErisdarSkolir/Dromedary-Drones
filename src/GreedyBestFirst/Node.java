package GreedyBestFirst;

import java.util.ArrayList;

public class Node implements Comparable<Node>{

	private double distance = 0;
	public double x = 0;
	public double y = 0;
	private boolean examined = false;
	private ArrayList<Node> neighbors = new ArrayList<Node>();

	public Node (double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getDistance() {
		
		return distance;
	}
	
	public void setDistance(double d) {
		distance = d;
	}

	public double getDistanceTo(Node n) {
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

	public void addNeighbor(Node n) {
		neighbors.add(n);
	}

	public Node getNeighbor(int i) {
		return neighbors.get(i);
	}
	
	/*public double getFlightTime(int i) {
		
	}*/

	public int compareTo(Node n) {
		return (int)(distance - n.getDistance());
	}

}
