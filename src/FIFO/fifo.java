package FIFO;

import java.util.ArrayList;
import java.util.Random;

import edu.gcc.packing.order;

public class fifo {

	public order nextFit(ArrayList<order> ords, ArrayList<order> filled, double capWeight){ 
		double totalFilledWeight = 0;
		double totalFilledTime = 0;
		order result = null;

		//calculate totalFilledWeight and totalFilledTime
		for (int i = 0; i < filled.size(); i++) {
			totalFilledWeight += filled.get(i).getWeight();
		}
		
		for (int i = 0; i < ords.size(); i++) {
			if (ords.get(i).getWeight() + totalFilledWeight <= capWeight) {
				result = ords.get(i);
				break;
			}
		}

		if (filled.size() == 0) {
			result = ords.get(0);
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
			//order temp = nextFit(orders, filled, 12, 20);
			//orders.remove(temp);
			//if (temp != null) {
			//	filled.add(temp);
			//}
		}

		System.out.println("Filled size: " + filled.size());
		for (int i = 0; i < filled.size(); i++) {
			System.out.println("Weight: " + filled.get(i).getWeight() + " Time: " + filled.get(i).getTime());
		}
	}

}
