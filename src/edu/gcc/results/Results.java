package edu.gcc.results;

import java.util.List;
import java.util.ArrayList;

public class Results {
	// Chart 1
	private List<Long> timePerOrder;
	private long averageTimePerOrder;
	private long longestTime;
	// Chart 2
	private List<Integer> ordersPerTrip;
	private int averageOrdersPerTrip;
	// Chart 3
	private List<Long> distancePerTrip;
	private long averageDistancePerTrip;
	
	private String simType;
	
	public Results(
			List<Long> timesPerOrder,
			List<Integer> ordersPerTrip2,
			List<Long> distancePerTrip2,
			String simType
			) {
		this.timePerOrder = timesPerOrder;
		this.ordersPerTrip = ordersPerTrip2;
		this.distancePerTrip = distancePerTrip2;
		this.simType = simType;
		
		// Set average time per order
		this.averageTimePerOrder = 0;
		this.longestTime = 0;
		for(Long time : this.timePerOrder) {
			this.averageTimePerOrder += time;
			if(time > this.longestTime) {
				this.longestTime = time;
			}
		}
		this.averageTimePerOrder = this.averageTimePerOrder / this.timePerOrder.size();
		
		// Set average orders per trip
		this.averageOrdersPerTrip = 0;
		for(int orders : this.ordersPerTrip) {
			this.averageOrdersPerTrip += orders;
		}
		this.averageOrdersPerTrip = this.averageOrdersPerTrip / this.ordersPerTrip.size();
		
		// Set average distance traveled per trip
		this.averageDistancePerTrip = 0;
		for(Long distance : this.distancePerTrip) {
			this.averageDistancePerTrip += distance;
		}
		this.averageDistancePerTrip = this.averageDistancePerTrip / this.distancePerTrip.size();
	}

	public List<Long> getTimePerOrder() {
		return timePerOrder;
	}

	public void setTimePerOrder(ArrayList<Long> timePerOrder) {
		this.timePerOrder = timePerOrder;
	}

	public long getAverageTimePerOrder() {
		return averageTimePerOrder;
	}

	public void setAverageTimePerOrder(long averageTimePerOrder) {
		this.averageTimePerOrder = averageTimePerOrder;
	}

	public List<Integer> getordersPerTrip() {
		return ordersPerTrip;
	}

	public void setordersPerTrip(ArrayList<Integer> ordersPerTrip) {
		this.ordersPerTrip = ordersPerTrip;
	}

	public long getAverageOrdersPerTrip() {
		return averageOrdersPerTrip;
	}

	public void setAverageOrdersPerTrip(int averageOrdersPerTrip) {
		this.averageOrdersPerTrip = averageOrdersPerTrip;
	}

	public long getLongestTime() {
		return longestTime;
	}

	public void setLongestTime(long longestTime) {
		this.longestTime = longestTime;
	}

	public List<Long> getDistancePerTrip() {
		return distancePerTrip;
	}

	public void setDistancePerTrip(ArrayList<Long> distancePerTrip) {
		this.distancePerTrip = distancePerTrip;
	}

	public long getAverageDistancePerTrip() {
		return averageDistancePerTrip;
	}

	public void setAverageDistancePerTrip(long averageDistancePerTrip) {
		this.averageDistancePerTrip = averageDistancePerTrip;
	}
	
	public String getSimType() {
		return simType;
	}
	
	public void setSimType(String simType) {
		this.simType = simType;
	}
	
}
