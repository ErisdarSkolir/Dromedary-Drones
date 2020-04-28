package edu.gcc.drone;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable(value = "id", autogenerate = true)
public class Drone {
	// ID
	private long id;
	// Campus ID
	private long campus_id;
	// Name
	private String name;
	// In feet per second
	private double speed;
	// In lbs
	private double max_capacity;
	
	public Drone(long campus_id, String name, double speed, double max_capacity) {
		this.campus_id = campus_id;
		this.name = name;
		this.speed = speed;
		this.max_capacity = max_capacity;
	}

	public final long getId() {
		return id;
	}

	public long getCampus_id() {
		return campus_id;
	}

	public void setCampus_id(long campus_id) {
		this.campus_id = campus_id;
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

	public double getMax_capacity() {
		return max_capacity;
	}

	public void setMax_capacity(double max_capacity) {
		this.max_capacity = max_capacity;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
