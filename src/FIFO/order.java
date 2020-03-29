package FIFO;

public class order {
	
	private int weight;
	private int deliveryTime;
	
	public order(int w, int d) {
		weight = w;
		deliveryTime = d;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getTime() {
		return deliveryTime;
	}

}
