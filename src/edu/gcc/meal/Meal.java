package edu.gcc.meal;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable(value = "id", autogenerate = true)
public class Meal {
	private long id;

	private String name;

	private int burgers;
	private int fries;
	private int drinks;
	private float probability;

	private boolean loaded;

	public Meal(
			final String name, final int burgers, final int fries,
			final int drinks, final float probability
	) {
		this.name = name;
		this.burgers = burgers;
		this.fries = fries;
		this.drinks= drinks;
		this.probability = probability;
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

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public int getBurgers() {
		return burgers;
	}
	
	public int getDrinks() {
		return drinks;
	}
	
	public int getFries() {
		return fries;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(probability);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Meal))
			return false;

		Meal meal = (Meal) obj;
		return meal.id == this.id &&
				meal.name.equals(this.name) &&
				Float.floatToIntBits(meal.probability) == Float.floatToIntBits(
					this.probability
				);
	}
}
