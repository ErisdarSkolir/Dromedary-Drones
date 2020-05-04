package edu.gcc.packing;

import java.util.List;

import edu.gcc.order.Order;

public class Fifo implements PackingAlgorithm {
	@Override
	public Order nextFit(List<Order> ords, List<Order> filled, double capWeight) {
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

		if (filled.size() == 0) {
			result = ords.get(0);
		}

		return null;
	}
}
