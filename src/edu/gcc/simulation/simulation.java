package edu.gcc.simulation;

import java.util.ArrayList;
import GreedyBestFirst.Graph;
import GreedyBestFirst.Order;

public class simulation {
	
	private ArrayList<Order> orders;
	private int packing;
	private int traveling;
	final private double capacity_weight = 12;

	// Orders
	// Algorithms
	// Run algorithms on orders
	public simulation(ArrayList<Order> orders, int packing, int traveling) {
		this.orders = orders;
		this.packing = packing;
		this.traveling = traveling;
	}
	
	public void runSimulation() {
		//
		ArrayList<Order> filled = new ArrayList<Order>();
		ArrayList<Order> orders_knapsack = new ArrayList<Order>();

		// Order temp
		Order temp;
		
		while(this.orders != null) {
			
			// FIFO
			if (packing == 1) {
				for (int i = 0; i < orders_knapsack.size(); i ++) {
					temp = nextFit(orders, filled, capacity_weight);
					orders_knapsack.remove(temp);
					if (temp != null) {
						filled.add(temp);
					}
				}
			}
			// Greedy
			if (traveling == 1) {
				ArrayList<Order> path = new ArrayList<Order>();
				Graph g = new Graph(filled);
				path = GBFS(g, filled.get(0));
			}
		}
	}
	
	public Order nextFit(ArrayList<Order> ords, ArrayList<Order> filled, double capWeight){ 
		double totalFilledWeight = 0;
		Order result = null;

		//calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}
		
		for (int i = 0; i < ords.size(); i++) {
			if (ords.get(i).getWeight() + totalFilledWeight <= capWeight) {
				result = ords.get(i);
				break;
			}
		}

		if (filled.size() == 0) {
			result = ords.get(0);
		}

		return result;
	}
	
	public ArrayList<Order> GBFS(Graph g, Order start) {

		ArrayList<Order> path = new ArrayList<Order>();
		
		Order current = start;
		boolean isBeginning = true;
		boolean isDone = false;
		
		int counter = 0;
		
		path.add(current);
		
		Order closest;

		while(!isDone && counter < 10) {
			counter++;
			
			closest = g.getClosestNeighbor(current);

			if (closest == null) {
				isDone = true;
			}

			if (!isDone) {
				current = closest;
				//System.out.println("(" + current.x + ", " + current.y + ")");
				
				current.setExamined(true);
				path.add(current);
				
				if (current == start && !isBeginning) {
					System.out.println("Found the goal");
					isDone = true;
				}

				isBeginning = false;
			}

		}
		
		return path;

	}
}
