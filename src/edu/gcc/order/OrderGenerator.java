package edu.gcc.order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;

/**
 * This class generates random orders from a given meal and dropoff location
 * list.
 * 
 * @author Luke Donmoyer
 */
public class OrderGenerator {
	private final ThreadLocalRandom random = ThreadLocalRandom.current();

	private final NavigableMap<Double, Meal> meals = new TreeMap<>();
	private final List<MapLocation> dropoffLocations;

	/**
	 * Main constructor.
	 * 
	 * @param meals            A list of meals to randomly choose from.
	 * @param dropoffLocations A list of locations to randomly deliver to.
	 * 
	 * @throws IllegalArgumentException If the total probability of the meals is
	 *                                  greater than 100.
	 */
	public OrderGenerator(
			final List<Meal> meals,
			final List<MapLocation> dropoffLocations
	) {
		this.dropoffLocations = new ArrayList<>(dropoffLocations);

		double totalProbability = 0.0;

		for (Meal meal : meals) {
			totalProbability += meal.getProbability();
			this.meals.put(totalProbability, meal);
		}

		// If the total probability of the meals is over 100, throw an
		// exception.
		if (totalProbability > 100.0)
			throw new IllegalArgumentException(
					"The total probability of all meals cannoot be over 100%"
			);
	}

	/**
	 * Gets a list of random orders in between the given start time and end time
	 * in millisecond epoch format.
	 * 
	 * @param numberOfOrders The number of orders to generate.
	 * @param startTimestamp The starting time in millisecond epoch format.
	 * @param endTimestamp   The ending time in millisecond epoch format.
	 * @return A list of orders in the given timestamp interval.
	 * 
	 * @throws IllegalArgumentException If the end timestmap is less than the
	 *                                  beginning timestamp.s
	 */
	public List<Order> getOrdersInInterval(
			final int numberOfOrders,
			final long startTimestamp,
			final long endTimestamp
	) {
		if (endTimestamp < startTimestamp)
			throw new IllegalArgumentException(
					"End timestamp cannot be less than beginning timestamp"
			);

		List<Order> result = new ArrayList<>(numberOfOrders);

		for (int i = 0; i < numberOfOrders; i++) {
			result.add(
				generateOrder(random.nextLong(startTimestamp, endTimestamp + 1))
			);
		}

		result.sort(Comparator.comparingLong(Order::getTimestamp));

		return result;
	}

	/**
	 * Generates an order for the given timestamp with a random meal and random
	 * delivery location.
	 * 
	 * @param timestamp The timestamp of the order.
	 * @return A newly generated order.
	 */
	private Order generateOrder(final long timestamp) {
		Meal meal = meals.ceilingEntry(random.nextDouble()).getValue();
		MapLocation dropoffLocation = dropoffLocations.get(
			random.nextInt(dropoffLocations.size())
		);

		return new Order(dropoffLocation, meal, timestamp);
	}
}
