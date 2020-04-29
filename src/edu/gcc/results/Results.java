package edu.gcc.results;

import java.util.List;
import java.util.ArrayList;

public class Results {
	private List<Long> timePerOrder;
	private long averageTimePerOrder;
	private List<Long> odersPerTrip;
	private long averageOrdersPerTrip;
	private List<Long> distanceTraveledPerTrip;
	private long averageDistanceTraveledPerTrip;

	public Results(
			ArrayList<Long> timePerOrder,
			long averageTimePerOrder,
			ArrayList<Long> odersPerTrip,
			long averageOrdersPerTrip,
			ArrayList<Long> distanceTraveledPerTrip,
			long averageDistanceTraveledPerTrip
			) {
		this.timePerOrder = timePerOrder;
		this.averageTimePerOrder = averageTimePerOrder;
		this.odersPerTrip = odersPerTrip;
		this.averageOrdersPerTrip = averageOrdersPerTrip;
		this.distanceTraveledPerTrip = distanceTraveledPerTrip;
		this.averageDistanceTraveledPerTrip = averageDistanceTraveledPerTrip;
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

	public List<Long> getDistanceTraveledPerTrip() {
		return distanceTraveledPerTrip;
	}

	public void setDistanceTraveledPerTrip(List<Long> distanceTraveledPerTrip) {
		this.distanceTraveledPerTrip = distanceTraveledPerTrip;
	}

	public long getAverageDistanceTraveledPerTrip() {
		return averageDistanceTraveledPerTrip;
	}

	public void setAverageDistanceTraveledPerTrip(long averageDistanceTraveledPerTrip) {
		this.averageDistanceTraveledPerTrip = averageDistanceTraveledPerTrip;
	}
}
