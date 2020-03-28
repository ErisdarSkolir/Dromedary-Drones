package edu.gcc.order;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("id")
public class Meal {
	private int id;

	private String name;
	private float probability;

	public Meal(final String name, final float probability) {
		this.name = name;
		this.probability = probability;
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

	public float getProbability() {
		return probability;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}
}
