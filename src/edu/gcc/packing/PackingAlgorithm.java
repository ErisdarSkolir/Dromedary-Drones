package edu.gcc.packing;

import java.util.List;

import edu.gcc.order.Order;

@FunctionalInterface
public interface PackingAlgorithm {
	Order nextFit(List<Order> ords, List<Order> filled, int capWeight);
}
