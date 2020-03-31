package edu.gcc.packing;

import java.util.List;

import edu.gcc.order.Order;

public class Knapsack implements PackingAlgorithm {
	// return the index of the best order to put in next
	@Override
	public Order nextFit(List<Order> ords, List<Order> filled, int capWeight) {
		int totalFilledWeight = 0;
		Order result = null;

		// order by times
		int n = ords.size();
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (ords.get(j).getTime() > ords.get(j + 1).getTime()) {
					Order temp = ords.get(j);
					ords.set(j, ords.get(j + 1));
					ords.set(j + 1, temp);
				}

		// calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}

		// look for the largest item that will fit and set result to that order
		for (int i = ords.size() - 1; i >= 0; i--) {
			if (ords.get(i).getWeight() + totalFilledWeight <= capWeight) {
				result = ords.get(i);
			}
		}

		if (filled.size() == 0) {
			result = ords.get(ords.size() - 1);
		}

		return result;
	}
}
