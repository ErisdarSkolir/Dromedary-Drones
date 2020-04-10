package edu.gcc.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;
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
	private long simulation_time;
	ArrayList<Long> time_statistics = new ArrayList<Long>();

	// Orders
	// Algorithms
	// Run algorithms on orders
	public Simulation(List<Meal> meals, List<MapLocation> dropoffLocations, PackingAlgorithm packingAlgorithm,
			int traveling) {
		this.packingAlgorithm = packingAlgorithm;
		this.traveling = traveling;

		List<String> customers = new ArrayList<>();
		customers.add("Bob");
		customers.add("John");
		customers.add("Jane");
		customers.add("That random guy over there");

		this.orderGen = new OrderGenerator(meals, customers, dropoffLocations);
		orders.addAll(orderGen.getOrdersInInterval(10, 0, 3_600_000));

		System.out.println(orders);
	}

	public void runSimulation() {
		// Orders filled
		List<Order> filled = new ArrayList<>();
		// Order path
		List<Order> path = new ArrayList<>();
		// Distance from order to next order
		double distance_to_next;
		// Feet per second drone speed
		double drone_speed = 0.02933;
		//
		// TODO: we need to sort orders by time
		this.simulation_time = orders.get(0).getTime();
		ArrayList<Long> delivery_times = new ArrayList<Long>();

		// Order temp
		Order temp;

		while (!this.orders.isEmpty()) {
			// FIFO
			filled.add(new Order(new MapLocation(0, 0, 0x1)));
			for (int i = 0; i < orders.size(); i++) {
				temp = packingAlgorithm.nextFit(orders, filled, CAPACITY_WEIGHT);
				orders.remove(temp);
				if (temp != null) {
					System.out.println(temp.dropoffLocation.getxCoord() + " " + temp.dropoffLocation.getyCoord());
					filled.add(temp);
				}
			}
			
		

			// Greedy
			if (traveling == 1) {
				Graph g = new Graph(filled);
				path = GBFS(g, filled.get(0));
			}
			
			//System.out.println("First thing in path: " + path.get(0).dropoffLocation.getxCoord());
			System.out.println("******************************************************");
			//print everything in path
			for (int i = 0 ; i < path.size(); i++) {
				System.out.println(path.get(i).dropoffLocation.getxCoord() + " " + path.get(i).dropoffLocation.getyCoord());
			}

			// Set times
			for (int i = 0; i < path.size() - 1; i++) {
				//print the trip
				System.out.println("The drone is flying from (" + path.get(i).dropoffLocation.getxCoord() + "," + path.get(i).dropoffLocation.getyCoord() + ") to (" + path.get(i + 1).dropoffLocation.getxCoord() + "," + path.get(i + 1).dropoffLocation.getyCoord() + ")");
				
				distance_to_next = path.get(i).getDistanceTo(path.get(i + 1));				
				simulation_time += (distance_to_next / drone_speed);
				delivery_times.add(simulation_time);
				//time_statistics.add(simulation_time - path.get(i).getTime());
				this.time_statistics.add(delivery_times.get(i) - path.get(i).getTime());
				//System.out.println(time_statistics.get(i));
			}
			//System.out.println(simulation_time);
			simulation_time += 180_000;
			//System.out.println(simulation_time);
			
			
			for (int ind = filled.size() - 1; ind >= 0; ind--) {
				filled.remove(ind);
			}
		}
	}

	public List<Long> getTimeStatistics() {
		return this.time_statistics;
	}

	public List<Order> GBFS(Graph g, Order start) {
		List<Order> path = new ArrayList<>();

		Order current = start;
		boolean isBeginning = true;
		boolean isDone = false;

		path.add(current);

		Order closest;

		while (!isDone) {
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
					System.out.println("******************************************************\nFound a path...\n");
					isDone = true;
				}

				isBeginning = false;
			}

		}

		return path;

	}
}
