package edu.gcc.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;
import edu.gcc.order.Order;
import edu.gcc.order.OrderGenerator;
import edu.gcc.packing.Fifo;
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

	private static List<Order> bestPath = new ArrayList<>();
	private static List<Order> btPath = new ArrayList<>();

	private String simType;

	ArrayList<Long> timesPerOrder = new ArrayList<Long>();
	ArrayList<Long> ordersPerTrip = new ArrayList<Long>();
	ArrayList<Long> distancePerTrip = new ArrayList<Long>();

	// Orders
	// Algorithms
	// Run algorithms on orders
	public Simulation(List<Meal> meals, MapLocation shopLocation, List<MapLocation> dropoffLocations,
			PackingAlgorithm packingAlgorithm, int traveling) {
		this.packingAlgorithm = packingAlgorithm;
		this.traveling = traveling;
		this.shopLocation = shopLocation;

		ArrayList<String> customers = new ArrayList<>();
		customers.add("Bob");
		customers.add("John");
		customers.add("Jane");
		customers.add("That random guy over there");

		this.orderGen = new OrderGenerator(meals, customers, dropoffLocations);
		orders.addAll(orderGen.getOrdersInInterval(10, 0, 3_600_000));

		//System.out.println(orders);
	}

	// Receive List of orders
	public Simulation(List<Meal> meals, List<Order> orders, MapLocation shopLocation,
			List<MapLocation> dropoffLocations, PackingAlgorithm packingAlgorithm, int traveling) {
		this.packingAlgorithm = packingAlgorithm;
		this.traveling = traveling;
		this.shopLocation = shopLocation;
		this.orders = orders;

		//System.out.println(orders);
	}

	public Results runSimulation() {
		// Results data to be filled
		ArrayList<Long> timesPerOrder = new ArrayList<Long>();
		ArrayList<Integer> ordersPerTrip = new ArrayList<Integer>();
		ArrayList<Long> distancePerTrip = new ArrayList<Long>();

		//
		long tripDistance;
		// Order path
		List<Order> path = new ArrayList<>();
		// Distance from order to next order
		double distanceToNext;
		// Feet per second drone speed
		double droneSpeed = 0.02933;
		
		if (packingAlgorithm instanceof Fifo) {
			simType = "FIFO";

		}
		else
			simType = "Knapsack";

		
		this.simulationTime = orders.get(0).getTime();
		ArrayList<Long> deliveryTimes = new ArrayList<Long>();

		while (!this.orders.isEmpty()) {
			// FIFO
			List<Order> filledOrders = runKnapsack(orders);

			

			// Greedy
			if (traveling == 1) {
				path = runGreedyTSP(filledOrders);
			}

			// Init trip distance
			tripDistance = 0;
			// Set times
			for (int i = 0; i < path.size() - 1; i++) {
				// Times per order
				distanceToNext = path.get(i).getDistanceTo(path.get(i + 1));
				simulationTime += (distanceToNext / droneSpeed);
				deliveryTimes.add(simulationTime);
				timesPerOrder.add(deliveryTimes.get(i) - path.get(i).getTime());
				// Distance per trip
				tripDistance += distanceToNext;
			}
			// Order per trip
			ordersPerTrip.add(filledOrders.size());
			// Distance per trip
			distancePerTrip.add(tripDistance);
			simulationTime += 180_000;

			for (int ind = filledOrders.size() - 1; ind >= 0; ind--) {
				filledOrders.remove(ind);
			}
		}

		return new Results(timesPerOrder, ordersPerTrip, distancePerTrip, simType);

	}

	/*
	 * Method that runs FIFO on orders to be packed
	 */
	public List<Order> runKnapsack(List<Order> ords) {

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
	 * Method that runs Backtracking algorithm for TSP
	 */
	public List<Order> runBT(List<Order> filled) {

		int n = filled.size() + 1;
		Order first = filled.get(0);

		double[][] graph = new double[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					graph[i][j] = filled.get(i).getDistanceTo(filled.get(j));
				} else {
					graph[i][j] = 0;
				}
			}
		}

		// Boolean array to check if a node
		// has been visited or not
		boolean[] v = new boolean[n];

		// Mark 0th node as visited
		v[0] = true;
		double ans = Double.MAX_VALUE;

		// Find the minimum weight Hamiltonian Cycle
		ans = tsp(graph, v, 0, n, 1, 0.0, ans, filled);

		// ans is the minimum weight Hamiltonian Cycle
		System.out.println(ans);

		bestPath.add(0, first);
		bestPath.add(first);

		return filled;

	}

	/*
	 * Method that performs backtracking dirty work This code is adapted from
	 * Rajput-Ji's implementation of backtracking
	 */
	public double tsp(double[][] graph, boolean[] v, int currPos, int n, int count, double cost, double ans,
			List<Order> filled) {

		// If last node is reached and it has a link
		// to the starting node i.e the source then
		// keep the minimum value out of the total cost
		// of traversal and "ans"
		// Finally return to check for more possible values
		if (count == n && graph[currPos][0] > 0) {
			// ans = Math.min(ans, cost + graph[currPos][0]);
			if (ans > cost + graph[currPos][0]) {
				ans = cost + graph[currPos][0];
				bestPath = new ArrayList<>(btPath);
			}
			return ans;
		}

		// BACKTRACKING STEP
		// Loop to traverse the adjacency list
		// of currPos node and increasing the count
		// by 1 and cost by graph[currPos,i] value
		for (int i = 0; i < n; i++) {
			if (v[i] == false && graph[currPos][i] > 0) {

				// Mark as visited
				v[i] = true;
				btPath.add(filled.get(i));
				ans = tsp(graph, v, i, n, count + 1, cost + graph[currPos][i], ans, filled);

				// Mark ith node as unvisited
				btPath.remove(filled.get(i));
				v[i] = false;
			}
		}
		return ans;
	}

	/*
	 * Method that actually runs the Greedy algorithm
	 */
	public List<Order> runGreedyTSP(List<Order> filled) {

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

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getOrders() {
		return orders;
	}
}
