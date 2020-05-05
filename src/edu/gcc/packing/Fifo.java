package edu.gcc.packing;

import java.util.List;

import edu.gcc.order.Order;

/**
 * First In, First Out Packing Algorithm
 * 
 * @author Lake Pry
 *
 */
public class Fifo implements PackingAlgorithm {

	/**
	 * Returns the next Order in the sequence
	 */
	@Override
	public Order nextFit(
			List<Order> ords,
			List<Order> filled,
			double capWeight,
			double maxDistance
	) {
		double totalFilledWeight = 0;
		Order result = null;
		double furthestDistance = 0.0;

		// iterate through filled to see the furthest order that has been
		// processed and record the max
		if (filled.isEmpty())
			for (int i = 1; i < filled.size(); i++) {
				if (filled.get(i)
						.getDistanceTo(filled.get(0)) > furthestDistance) {
					furthestDistance = filled.get(i)
							.getDistanceTo(filled.get(0));
				}
			}

		// calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}

		for (int i = 0; i < ords.size(); i++) {
			if (ords.get(i).getWeight() + totalFilledWeight <= capWeight &&
					filled.get(0).getDistanceTo(ords.get(i))
							+ furthestDistance < maxDistance) {
				result = ords.get(i);
				return result;
			}
		}

		if (filled.isEmpty()) {
			result = ords.get(0);
		}

		return result;
	}
}
