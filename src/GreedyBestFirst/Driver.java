package GreedyBestFirst;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Driver {

	static ArrayList<Node> GBFS(Graph g, Node start) {

		ArrayList<Node> path = new ArrayList<Node>();
		
		Node current = start;
		boolean isBeginning = true;
		boolean isDone = false;
		
		int counter = 0;
		
		path.add(current);
		
		Node closest;

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

	}

	public static void main(String[] args) {

		//		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(); 
		//		
		//		pq.add(17);
		//		pq.add(1);
		//		pq.add(13);
		//		pq.add(-1);
		//		pq.add(5);
		//		
		//		System.out.println(pq.peek());

		Node start = new Node(0, 0);
		Node n1 = new Node(1,1);
		Node n2 = new Node(2,0);
		Node n3 = new Node(2,2);
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Node> path;
		
		nodes.add(start);
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
		
		Graph g = new Graph(nodes);
		
		path = GBFS(g, start);
		
		for (int i = 0; i < path.size(); i++) {
			System.out.println("(" + path.get(i).x + ", " + path.get(i).y + ")");
		}
	}

}
