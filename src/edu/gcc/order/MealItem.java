package edu.gcc.order;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("id")
public class MealItem {
	private int id;
	
	private String name;
	private double weight;
	
	public MealItem(String name, double weight) {
		super();
		this.name = name;
		this.weight = weight;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
