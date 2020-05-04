package edu.gcc.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.drone.Drone;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.order.Order;
import edu.gcc.packing.PackingAlgorithm;
import edu.gcc.results.Results;
import edu.gcc.salesman.greedy.Graph;

public class Simulation {
	private List<Order> orders = new ArrayList<>();
	private PackingAlgorithm packingAlgorithm;
	private int traveling;
	private MapLocation shopLocation;
	private static List<Order> bestPath = new ArrayList<>();
	private static List<Order> btPath = new ArrayList<>();
	private String simType;
	private List<Drone> drones = new ArrayList<>();

	private List<Long> timesPerOrder = new ArrayList<>();
	private List<Integer> ordersPerTrip = new ArrayList<>();
	private List<Long> distancePerTrip = new ArrayList<>();

	// Receive List of orders
	public Simulation(
			List<Drone> drones,
			List<Order> orders,
			MapLocation shopLocation,
			PackingAlgorithm packingAlgorithm,
			int traveling
	) {
		this.drones = new ArrayList<>(drones);
		this.packingAlgorithm = packingAlgorithm;
		this.traveling = traveling;
		this.shopLocation = shopLocation;
		this.orders = new ArrayList<>(orders);
	}

	public Results runSimulation() {
		// Init simTime list
		List<Long> simTime = new ArrayList<>();
		for (int i = 0; i < this.drones.size(); i++) {
			simTime.add((long) 0);
		}
		long timeOfDrone = 0;

		// Distance of each trip
		long tripDistance;

		// Order path
		List<Order> path = new ArrayList<>();
		// Distance from order to next order
		double distanceToNext;

		// Delivery times to be exported to a Results obj
		ArrayList<Long> deliveryTimes = new ArrayList<Long>();

		while (!this.orders.isEmpty()) {

			// Init drone to use
			Drone droneUp = this.drones.get(0);

			// FInd drone with lowest time
			for (int indexX = 0; indexX < simTime.size(); indexX++) {
				for (int indexY = 0; indexY < simTime.size(); indexY++) {
					if (indexY < indexX) {
						// Drone with lowest time
						droneUp = this.drones.get(indexY);
						timeOfDrone = simTime.get(indexY);
					}
				}
			}

			// FIFO
			List<Order> filledOrders = runKnapsack(
				orders,
				droneUp.getMaxCapacity()
			);
			this.simType = "FIFO";

			// Greedy
			if (traveling == 1) {
				path = runGreedyTSP(filledOrders);
				this.simType = "Greedy";
			}

			// check that the drone can fly the entire path without dying
			/*
			 * while (!isSafe) { System.out.println("Is not safe!"); for (int i
			 * = 0; i < path.size() - 1; i++) { distanceChecker +=
			 * ConvertLatLongToFeet(path.get(i).getDistanceTo(path.get(i + 1)));
			 * }
			 * 
			 * if (distanceChecker < droneUpNext.getMaxDistance()) { isSafe =
			 * true; } else { if (path.size() > 2) { Order temp =
			 * path.remove(path.size() - 2); temp.incTimesPassed();
			 * orders.add(temp); } else { path.remove(path.size() - 2); } } }
			 */

			// if (isSafe) {
			if (true) {
				// Init trip distance
				tripDistance = 0;
				// Set times
				for (int i = 0; i < path.size() - 1; i++) {
					// Times per order
					distanceToNext = ConvertLatLongToFeet(
						path.get(i).getDistanceTo(path.get(i + 1))
					);
					timeOfDrone += (distanceToNext / ConvertMphToFps(
						droneUp.getSpeed()
					));
					deliveryTimes.add(timeOfDrone);
					this.timesPerOrder.add(
						deliveryTimes.get(i) - path.get(i).getTime()
					);
					// Distance per trip
					tripDistance += distanceToNext;
				}
				// Order per trip
				this.ordersPerTrip.add(filledOrders.size());
				// Distance per trip
				this.distancePerTrip.add(tripDistance);
				timeOfDrone += droneUp.getTurnAroundTime();

				for (int ind = filledOrders.size() - 1; ind >= 0; ind--) {
					filledOrders.remove(ind);
				}

			}
			// If the drone time is less than the next order set it equal to the
			// next order
			for (Long time : simTime) {
				if (this.orders.size() > 0) {
					if (time < this.orders.get(0).getTime()) {
						time = (long) this.orders.get(0).getTime();
					}
				}
			}
		}

		return new Results(
				this.timesPerOrder,
				this.ordersPerTrip,
				this.distancePerTrip,
				this.simType
		);
	}

	/*
	 * Method that runs FIFO on orders to be packed
	 */
	public List<Order> runKnapsack(List<Order> ords, double maxCapacity) {
		// Orders filled
		List<Order> filled = new ArrayList<>();

		// Order temp
		Order temp;

		// Add initial shop location to be used for path finding
		filled.add(new Order(shopLocation));

		// Fill orders in FIFO style
		for (int i = 0; i < orders.size(); i++) {
			temp = packingAlgorithm.nextFit(orders, filled, maxCapacity);
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
	public double tsp(
			double[][] graph,
			boolean[] v,
			int currPos,
			int n,
			int count,
			double cost,
			double ans,
			List<Order> filled
	) {
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
				ans = tsp(
					graph,
					v,
					i,
					n,
					count + 1,
					cost + graph[currPos][i],
					ans,
					filled
				);

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

	public double ConvertMphToFps(double mph) {
		return ((mph / 3600) * 5280);
	}

	public double ConvertLatLongToFeet(double latLong) {
		return (((latLong * 3_280.4) * 10_000) / 90);
	}
}
