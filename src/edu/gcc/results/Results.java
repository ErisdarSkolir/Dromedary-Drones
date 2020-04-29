package edu.gcc.results;

import java.util.List;
import java.util.ArrayList;

public class Results {
	// Chart 1
	private List<Long> timePerOrder;
	private long averageTimePerOrder;
	private long longestTime;
	// Chart 2
	private List<Long> odersPerTrip;
	private long averageOrdersPerTrip;
	// Chart 3
	private List<Long> distancePerTrip;
	private long averageDistancePerTrip;

	public Results(
			ArrayList<Long> timePerOrder,
			ArrayList<Long> odersPerTrip,
			ArrayList<Long> distancePerTrip
			) {
		this.timePerOrder = timePerOrder;
		this.odersPerTrip = odersPerTrip;
		this.distancePerTrip = distancePerTrip;
		
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
		for(Long orders : this.odersPerTrip) {
			this.averageOrdersPerTrip += orders;
		}
		this.averageOrdersPerTrip = this.averageOrdersPerTrip / this.odersPerTrip.size();
		
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

	public void setTimePerOrder(List<Long> timePerOrder) {
		this.timePerOrder = timePerOrder;
	}

	public long getAverageTimePerOrder() {
		return averageTimePerOrder;
	}

	public void setAverageTimePerOrder(long averageTimePerOrder) {
		this.averageTimePerOrder = averageTimePerOrder;
	}

	public List<Long> getOdersPerTrip() {
		return odersPerTrip;
	}

	public void setOdersPerTrip(List<Long> odersPerTrip) {
		this.odersPerTrip = odersPerTrip;
	}

	public long getAverageOrdersPerTrip() {
		return averageOrdersPerTrip;
	}

	public void setAverageOrdersPerTrip(long averageOrdersPerTrip) {
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

	public void setDistancePerTrip(List<Long> distancePerTrip) {
		this.distancePerTrip = distancePerTrip;
	}

	public long getAverageDistancePerTrip() {
		return averageDistancePerTrip;
	}

	public void setAverageDistancePerTrip(long averageDistancePerTrip) {
		this.averageDistancePerTrip = averageDistancePerTrip;
	}
	
}
