package edu.gcc.salesman.greedy;

import java.util.ArrayList;
import java.util.PriorityQueue;

import edu.gcc.order.Order;

public class Driver {

	/*static ArrayList<Order> GBFS(Graph g, Order start) {

		ArrayList<Order> path = new ArrayList<Order>();
		
		Order current = start;
		boolean isBeginning = true;
		boolean isDone = false;
		
		int counter = 0;
		
		path.add(current);
		
		Order closest;

		while(!isDone && counter < 10) {
			counter++;
			
			closest = g.getClosestNeighbor(current);

			if (closest == null) {
				isDone = true;
			}

			if (!isDone) {
				current = closest;
				//System.out.println("(" + current.x + ", " + current.y + ")");
				
				current.setExamined(true);
				path.add(current);
				
				if (current == start && !isBeginning) {
					System.out.println("Found the goal");
					isDone = true;
				}

				isBeginning = false;
			}

		}
		
		return path;

	}*/

	/*public static void main(String[] args) {

		//		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(); 
		//		
		//		pq.add(17);
		//		pq.add(1);
		//		pq.add(13);
		//		pq.add(-1);
		//		pq.add(5);
		//		
		//		System.out.println(pq.peek());

		Order start = new Order(0, 0);
		Order n1 = new Order(1,1);
		Order n2 = new Order(2,0);
		Order n3 = new Order(2,2);
		
		ArrayList<Order> nodes = new ArrayList<Order>();
		ArrayList<Order> path;
		
		nodes.add(start);
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
		
		Graph g = new Graph(nodes);
		
		path = GBFS(g, start);
		
		for (int i = 0; i < path.size(); i++) {
			System.out.println("(" + path.get(i).x + ", " + path.get(i).y + ")");
		}
	}*/

}
