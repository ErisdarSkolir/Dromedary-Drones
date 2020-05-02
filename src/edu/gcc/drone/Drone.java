package edu.gcc.drone;

import edu.gcc.xml.annotation.XmlSerializable;

/**
 * Container class for storing information about drones.
 * 
 * @author Ethan Harvey, Luke Donmoyer
 */
@XmlSerializable(value = "id", autogenerate = true)
public class Drone {
	// ID
	private long id;
	// Name
	private String name;
	// In feet per second
	private double speed;
	// In lbs
	private double maxCapacity;
	private double turnAroundTime;
	private double deliveryTime;
	private double maxFlightTime;

	private boolean loaded;

	/**
	 * Main constructor.
	 * 
	 * @param name
	 * @param speed
	 * @param maxCapacity
	 * @param turnAroundTime
	 * @param deliveryTime
	 * @param maxFlightTime
	 * 
	 * @throws IllegalArgumentException if any of the given parameters are less
	 *                                  than 0.
	 */
	public Drone(
			String name, double speed, double maxCapacity,
			double turnAroundTime, double deliveryTime, double maxFlightTime
	) {
		validateGreaterThanZero(speed, "speed");
		validateGreaterThanZero(maxCapacity, "max capacity");
		validateGreaterThanZero(turnAroundTime, "turn around time");
		validateGreaterThanZero(deliveryTime, "deliery time");
		validateGreaterThanZero(maxFlightTime, "max flight time");

		this.name = name;
		this.speed = speed;
		this.maxCapacity = maxCapacity;
		this.turnAroundTime = turnAroundTime;
		this.deliveryTime = deliveryTime;
		this.maxFlightTime = maxFlightTime;
	}

	public final long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(double maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(final boolean loaded) {
		this.loaded = loaded;
	}

	public double getTurnAroundTime() {
		return turnAroundTime;
	}

	public void setTurnAroundTime(double turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	public double getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(double deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public double getMaxFlightTime() {
		return maxFlightTime;
	}

	public void setMaxFlightTime(double maxFlightTime) {
		this.maxFlightTime = maxFlightTime;
	}

	@Override
	public String toString() {
		return name;
	}

	private void validateGreaterThanZero(double value, String valueName) {
		if (value < 0.0)
			throw new IllegalArgumentException(
					String.format("Drone %s cannot be less than 0", valueName)
			);
	}
}
