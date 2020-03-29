package edu.gcc.order;

import edu.gcc.maplocation.DropoffLocation;
import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("id")
public class Order {
	private int id;

	private long timestamp;
	
	private Meal meal;
	private String customerName;

	private transient DropoffLocation dropoffLocation;

	public Order(final String customerName, final DropoffLocation dropoffLocation, final Meal meal, final long timestamp) {
		this.customerName = customerName;
		this.dropoffLocation = dropoffLocation;
		this.meal = meal;
		this.timestamp = timestamp;
	}
}
