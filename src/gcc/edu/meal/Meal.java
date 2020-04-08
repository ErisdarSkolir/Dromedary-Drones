package gcc.edu.meal;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable(value = "id", autogenerate = true)
public class Meal {
	private long id;

	private String name;
	private float probability;

	public Meal(final String name, final float probability) {
		this.name = name;
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
		return meal.id == this.id && meal.name.equals(this.name)
				&& Float.floatToIntBits(meal.probability) == Float.floatToIntBits(this.probability);
	}
}
