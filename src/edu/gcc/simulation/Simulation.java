package edu.gcc.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;
import edu.gcc.order.Order;
import edu.gcc.order.OrderGenerator;
import edu.gcc.packing.PackingAlgorithm;
import edu.gcc.results.Results;
import edu.gcc.salesman.greedy.Graph;

public class Simulation {
	private static final int CAPACITY_WEIGHT = 12;

	private OrderGenerator orderGen;
	private List<Order> orders = new ArrayList<>();
	private PackingAlgorithm packingAlgorithm;
	private int traveling;
	private long simulationTime;
	private MapLocation shopLocation;
	private String simType;
	
	ArrayList<Long> timesPerOrder = new ArrayList<Long>();
	ArrayList<Long> ordersPerTrip = new ArrayList<Long>();
	ArrayList<Long> distancePerTrip = new ArrayList<Long>();

	// Orders
	// Algorithms
	// Run algorithms on orders
	public Simulation(List<Meal> meals, MapLocation shopLocation, List<MapLocation> dropoffLocations, PackingAlgorithm packingAlgorithm,
			int traveling) {
		this.packingAlgorithm = packingAlgorithm;
		this.traveling = traveling;
		this.shopLocation = shopLocation;
		
		List<String> customers = new ArrayList<>();
		customers.add("Bob");
		customers.add("John");
		customers.add("Jane");
		customers.add("That random guy over there");

		this.orderGen = new OrderGenerator(meals, customers, dropoffLocations);
		orders.addAll(orderGen.getOrdersInInterval(10, 0, 3_600_000));
		
		System.out.println(orders);
	}

	public Results runSimulation() {
		// Order path
		List<Order> path = new ArrayList<>();
		// Distance from order to next order
		double distance_to_next;
		// Feet per second drone speed
		double drone_speed = 0.02933;
		
		this.simulationTime = orders.get(0).getTime();
		ArrayList<Long> deliveryTimes = new ArrayList<Long>();

		while (!this.orders.isEmpty()) {
			// FIFO
			List<Order> filledOrders = runKnapsack(orders);
			simType = "FIFO";
					
			// Greedy
			if (traveling == 1) {
				path = runGreedyTSP(filledOrders);
				simType = "Greedy";
			}
			
			// Set times
			for (int i = 0; i < path.size() - 1; i++) {
				distance_to_next = path.get(i).getDistanceTo(path.get(i + 1));				
				simulationTime += (distance_to_next / drone_speed);
				deliveryTimes.add(simulationTime);
				this.timesPerOrder.add(deliveryTimes.get(i) - path.get(i).getTime());
				
			}
			simulationTime += 180_000;
			
			for (int ind = filledOrders.size() - 1; ind >= 0; ind--) {
				filledOrders.remove(ind);
			}
		}
		
		return new Results(timesPerOrder, ordersPerTrip, distancePerTrip, simType);
		
	}

	public List<Long> getTimeStatistics() {
		return this.timesPerOrder;
	}

	/*
	 * Method that runs FIFO on orders to be packed
	 */
	public List<Order> runKnapsack(List<Order> ords){

		// Orders filled
		List<Order> filled = new ArrayList<>();
		
		// Order temp
		Order temp;
		
		// Add initial shop location to be used for path finding
		filled.add(new Order(shopLocation));
		
		// Fill orders in FIFO style
		for (int i = 0; i < orders.size(); i++) {
			temp = packingAlgorithm.nextFit(orders, filled, CAPACITY_WEIGHT);
			orders.remove(temp);
			if (temp != null) {
				filled.add(temp);
			}
		}
		
		return filled;	
	}
	
	
	/*
	 * Method that actually runs the Greedy algorithm
	 */
	public List<Order> runGreedyTSP(List<Order> filled){
		
		Graph g = new Graph(filled);
		return GBFS(g, filled.get(0));
		
	}
	
	/*
	 * Method that does the bulk of the greedy algorithm's work
	 */
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
					isDone = true;
				}

				isBeginning = false;
			}

		}

		return path;

	}
}
