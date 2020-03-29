package edu.gcc.order;

import java.util.List;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("id")
public class Meal {
	private int id;

	private String name;
	private float probability;

	private transient List<MealItem> items;

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

	@Override
	public String toString() {
		return "Meal [name=" + name + ", probability=" + probability + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(probability);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Meal other = (Meal) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Float.floatToIntBits(probability) != Float.floatToIntBits(other.probability))
			return false;
		return true;
	}
}
