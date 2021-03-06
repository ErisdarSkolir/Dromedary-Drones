package edu.gcc.meal;

import java.text.DecimalFormat;

import edu.gcc.xml.annotation.XmlSerializable;

/**
 * This class is a container class for all the information about a meal. This
 * includes its name, the probability of the meal, and the components of the
 * meal. The weight is then calculated from the components based on static
 * pre-determined weights.
 * 
 * @author Luke Donmoyer, Ethan Harvey
 */
@XmlSerializable(value = "id", autogenerate = true)
public class Meal {
	private static final transient double BURGER_WEIGHT = 0.375;
	private static final transient double FRIES_WEIGHT = 0.25;
	private static final transient double DRINKS_WEIGHT = 0.875;

	private static final transient DecimalFormat decimalFormat = new DecimalFormat(
			"###.##"
	);

	// Serialized fields
	private long id;

	private String name;

	private int burgers;
	private int fries;
	private int drinks;
	private double probability;

	private boolean loaded;

	public Meal(
			final String name,
			final int burgers,
			final int fries,
			final int drinks,
			final double probability
	) {
		this.name = name;
		this.burgers = burgers;
		this.fries = fries;
		this.drinks = drinks;
		this.probability = probability;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setBurgers(int burgers) {
		this.burgers = burgers;
	}

	public int getBurgers() {
		return burgers;
	}

	public void setDrinks(int drinks) {
		this.drinks = drinks;
	}

	public int getDrinks() {
		return drinks;
	}

	public void setFries(int fries) {
		this.fries = fries;
	}

	public int getFries() {
		return fries;
	}

	@Override
	public String toString() {
		return String.format(
			"%s - %s%%",
			name,
			decimalFormat.format(probability)
		);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + burgers;
		result = prime * result + drinks;
		result = prime * result + fries;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (loaded ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(probability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (burgers != other.burgers)
			return false;
		if (drinks != other.drinks)
			return false;
		if (fries != other.fries)
			return false;
		if (id != other.id)
			return false;
		if (loaded != other.loaded)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(probability) != Double.doubleToLongBits(
			other.probability
		))
			return false;
		return true;
	}

	/**
	 * Returns the weight of the meal. This is based on the static
	 * pre-determined weights of each item multiplied by the number of each
	 * item.
	 * 
	 * @return the total weight of this meal.
	 */
	public double getWeight() {
		return (BURGER_WEIGHT * this.burgers) + (FRIES_WEIGHT * this.fries)
				+ (DRINKS_WEIGHT * this.drinks);
	}
}
