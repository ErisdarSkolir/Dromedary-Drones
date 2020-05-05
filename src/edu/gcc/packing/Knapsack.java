package edu.gcc.packing;

import java.util.List;
import edu.gcc.order.Order;

/**
 * A Greedy Knapsack Packing Algorithm
 * 
 * @author Zack Orlaski
 */
public class Knapsack implements PackingAlgorithm {
	/**
	 * Return the next order available to fit
	 */
	@Override
	public Order nextFit(
			List<Order> ords,
			List<Order> filled,
			double capWeight
	) {
		int totalFilledWeight = 0;
		Order result = null;

		// order by size
		int n = ords.size();
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (ords.get(j).getWeight() < ords.get(j + 1).getWeight()) {
					Order temp = ords.get(j);
					ords.set(j, ords.get(j + 1));
					ords.set(j + 1, temp);
				}
			}
		}
		// calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}

		// look over items that were passed over last time and try to fit them
		// first
		for (int i = 0; i < ords.size(); i++) {
			if (ords.get(i).getTimesPassed() > 0 &&
					ords.get(i).getWeight() + totalFilledWeight <= capWeight) {
				result = ords.get(i);
				return result;
			}
		}

		// look for the largest item that will fit and set result to that order
		for (int i = 0; i < ords.size(); i++) {
			if (ords.get(i).getWeight() + totalFilledWeight <= capWeight) {
				result = ords.get(i);
				return result;
			}
		}

		return null;
	}
}
