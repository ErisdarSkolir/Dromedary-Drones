package edu.gcc.packing;

import java.util.List;

import edu.gcc.order.Order;

/**
 * First In, First Out Packing Algorithm
 * 
 * @author Zack Orlaski
 */
public class Fifo implements PackingAlgorithm {
	/**
	 * Returns the next Order in the sequence
	 */
	@Override
	public Order nextFit(
			List<Order> ords,
			List<Order> filled,
			double capWeight
	) {
		double totalFilledWeight = 0;
		Order result = null;

		// calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}

		for (int i = 0; i < ords.size(); i++) {
			if (ords.get(i).getWeight() + totalFilledWeight <= capWeight) {
				result = ords.get(i);
				return result;
			}
		}

		if (filled.isEmpty())
			result = ords.get(0);

		return result;
	}
}
