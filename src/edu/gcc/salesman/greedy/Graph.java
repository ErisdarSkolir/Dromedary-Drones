package edu.gcc.salesman.greedy;

import java.util.ArrayList;

import edu.gcc.order.Order;

public class Graph {

	private ArrayList<Order> nodes;

	private Order start;

	public Graph(ArrayList<Order> n) {
		nodes = n;

		for (int i = 0; i < nodes.size(); i++) {
			for(int j = 0 ; j < nodes.size(); j++) {
				if (i != j) {
					nodes.get(i).addNeighbor(nodes.get(j));
				}
			}
		}

		start = nodes.get(0);
		start.setExamined(true);
	}

	public ArrayList<Order> getNodes(){
		return nodes;
	}

	public Order getClosestNeighbor(Order current) {
		//iterate through all neighbors of the current node and grab the closest if it is unvisited
		Order closest = null;

		//check if all are examined
		boolean allExamined = false;
		int numExamined = 0;

		boolean okToEnd = false;
		//System.out.println();
		for (int i = 0 ; i < nodes.size(); i++) {

			//System.out.println("(" + nodes.get(i).x + ", " + nodes.get(i).y + ") examined: " + nodes.get(i).getExamined());

			if (nodes.get(i).getExamined()) {
				numExamined++;
			}
		}

		if (numExamined == nodes.size()) {
			allExamined = true;
		}

		if(numExamined == nodes.size() - 1) {
			okToEnd = true;
		}

		//System.out.println(okToEnd + " " + start.x + " " + start.y);
		
		for (int i = 1; i < nodes.size(); i++) {
			if (!nodes.get(i).getExamined() && current != nodes.get(i) && !allExamined) {
				closest = nodes.get(i);
				break;
			}
		}

		if (!allExamined) {
			double distance = current.getDistanceTo(closest);

			for (int i = 1; i < nodes.size(); i++) {
				//System.out.println("(" + nodes.get(i).x + ", " + nodes.get(i).y + "): " + nodes.get(i).getExamined());
				//System.out.println("Current distance: " + distance + " Testing distance: " + current.getDistanceTo(nodes.get(i)));
				if (current != nodes.get(i) && current.getDistanceTo(nodes.get(i)) < distance && !nodes.get(i).getExamined() &&!okToEnd) {
					closest = nodes.get(i);
					distance = current.getDistanceTo(nodes.get(i));
				}
				else if (okToEnd) {
					return start;
				}
			}
		}

		return closest;
	}

}
