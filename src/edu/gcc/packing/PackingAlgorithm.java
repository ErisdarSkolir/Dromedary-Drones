package edu.gcc.packing;

import java.util.List;

import edu.gcc.order.Order;

/**
 * Interface for Packing Algorithms
 * 
 * @author Luke Donmoyer, Lake Pry, Zack Orlaski
 */
@FunctionalInterface
public interface PackingAlgorithm {

	/**
	 * Get the next available Order in the list based on the algorithm
	 * 
	 * @param ords        available orders
	 * @param filled      orders filled
	 * @param maxCapacity capacity to stop at
	 * @return next order in sequence
	 */
	public Order nextFit(
			List<Order> ords,
			List<Order> filled,
			double maxCapacity,
			double maxDistance
	);
}
