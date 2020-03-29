package edu.gcc.packing;

import java.util.ArrayList;
import java.util.Random;

import edu.gcc.packing.order;

public class knapsack {
	//return the index of the best order to put in next
	static order nextFit(ArrayList<order> ords, ArrayList<order> filled, int capWeight) 
	{ 
		int totalFilledWeight = 0;
		order result = null;

		//order by times
		int n = ords.size(); 
		for (int i = 0; i < n-1; i++) 
			for (int j = 0; j < n-i-1; j++) 
				if (ords.get(j).getTime() > ords.get(j+1).getTime()) 
				{ 
					order temp = ords.get(j); 
					ords.set(j, ords.get(j + 1)); 
					ords.set(j+1, temp); 
				} 

		//calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}

		//look for the largest item that will fit and set result to that order
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

	public static void main(String[] args) {

		ArrayList<order> orders = new ArrayList<>();
		ArrayList<order> filled = new ArrayList<>();
		Random random = new Random();		

		for (int i = 0; i < 4; i++) {
			orders.add(new order(random.nextInt(4) + 2, random.nextInt(5)+ 5));
		}

		for (int i = 0; i < 4; i++) {
			System.out.println("Order weight: " + orders.get(i).getWeight() + " delivery time: " + orders.get(i).getTime());
		}

		//		System.out.println(nextFit(orders, filled, 12, 20).getTime());
		for (int i = 0; i < 3; i++) {
			order temp = nextFit(orders, filled, 12);
			orders.remove(temp);
			if (temp != null) {
				filled.add(temp);
			}
		}

		System.out.println("Filled size: " + filled.size());
		for (int i = 0; i < filled.size(); i++) {
			System.out.println("Weight: " + filled.get(i).getWeight() + " Time: " + filled.get(i).getTime());
		}


	}


}
