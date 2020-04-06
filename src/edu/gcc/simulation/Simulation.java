package edu.gcc.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.order.Meal;
import edu.gcc.order.Order;
import edu.gcc.order.OrderGenerator;
import edu.gcc.packing.PackingAlgorithm;
import edu.gcc.salesman.greedy.Graph;

public class Simulation {

	private static final int CAPACITY_WEIGHT = 12;

	private OrderGenerator orderGen;
	private List<Order> orders = new ArrayList<>();
	private PackingAlgorithm packingAlgorithm;
	private int traveling;

	// Orders
	// Algorithms
	// Run algorithms on orders
	public Simulation(List<MapLocation> dropoffLocations, PackingAlgorithm packingAlgorithm, int traveling) {
		this.packingAlgorithm = packingAlgorithm;
		this.traveling = traveling;
		
		List<Meal> meals = new ArrayList<>();
		meals.add(new Meal("meal 1", 0.5f));
		meals.add(new Meal("meal 2", 0.25f));
		meals.add(new Meal("meal 3", 0.25f));
		
		List<String> customers = new ArrayList<>();
		customers.add("Bob");
		customers.add("John");
		customers.add("Jane");
		customers.add("That random guy over there");
		
		this.orderGen = new OrderGenerator(meals, customers, dropoffLocations);
		orders.addAll(orderGen.getOrdersInInterval(10, 0, 10));
		
		System.out.println(orders);
	}

	public void runSimulation() {
		//
		ArrayList<Order> filled = new ArrayList<Order>();

		// Order temp
		Order temp;

		while (this.orders.size() != 0) {

			// FIFO
			for (int i = 0; i < orders.size(); i++) {
				temp = packingAlgorithm.nextFit(orders, filled, CAPACITY_WEIGHT);
				orders.remove(temp);
				if (temp != null) {
					filled.add(temp);
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

	public ArrayList<Order> GBFS(Graph g, Order start) {

		ArrayList<Order> path = new ArrayList<Order>();

		Order current = start;
		boolean isBeginning = true;
		boolean isDone = false;

		int counter = 0;

		path.add(current);

		Order closest;

		while (!isDone && counter < 10) {
			counter++;

			closest = g.getClosestNeighbor(current);

			if (closest == null) {
				isDone = true;
			}

			if (!isDone) {
				current = closest;
				// System.out.println("(" + current.x + ", " + current.y + ")");

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
