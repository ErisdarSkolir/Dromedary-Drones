package edu.gcc.order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;

public class OrderGenerator {
	private final ThreadLocalRandom random = ThreadLocalRandom.current();

	private final NavigableMap<Float, Meal> meals = new TreeMap<>();
	private final List<String> customers;
	private final List<MapLocation> dropoffLocations;

	public OrderGenerator(final List<Meal> meals, final List<String> customers,
			final List<MapLocation> dropoffLocations) {
		this.customers = new ArrayList<>(customers);
		this.dropoffLocations = new ArrayList<>(dropoffLocations);

		float totalProbability = 0.0f;

		for (Meal meal : meals) {
			totalProbability += meal.getProbability();
			this.meals.put(totalProbability, meal);
		}

		if (totalProbability > 1.0f)
			throw new IllegalArgumentException("The total probability of all meals cannoot be over 100%");
	}

	public List<Order> getOrdersInInterval(final int numOrders, final long startTimestmp, final long endTimestamp) {
		List<Order> result = new ArrayList<>(numOrders);

		for (int i = 0; i < numOrders; i++) {
			result.add(generateOrder(random.nextLong(startTimestmp, endTimestamp + 1)));
		}

		result.sort(Comparator.comparingLong(Order::getTimestamp));
		
		return result;
	}

	private Order generateOrder(final long timestamp) {
		Meal meal = meals.ceilingEntry(random.nextFloat()).getValue();
		String customer = customers.get(random.nextInt(customers.size()));
		MapLocation dropoffLocation = dropoffLocations.get(random.nextInt(dropoffLocations.size()));

		return new Order(customer, dropoffLocation, meal, timestamp);
	}
}
