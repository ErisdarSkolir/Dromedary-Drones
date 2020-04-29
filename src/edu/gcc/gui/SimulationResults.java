/**
 * Container class for simulation statistics
 * @author Zack Orlaski
 * 
 */



package edu.gcc.gui;
import java.util.ArrayList;
import java.util.List;

public class SimulationResults {
	
	/*
	 * Note for Ethan: Add attributes here as needed for your statistics
	 */
	
	//delivery times
	private ArrayList<Long> simTimes;
	

	
	/**
	 * Main Constructor
	 * @param times delivery times for each order
	 */
		SimulationResults(ArrayList<Long> times){
		this.simTimes = times;
	}


	/*
	 * Getters and Setters
	 */
		
	
	/**
	 * Returns simulations delivery times
	 * @return the list of delivery times
	 */
	public List<Long> getSimTimes() {
		return simTimes;
	}

	/**
	 * sets the new list of delivery times
	 * @param simTimes the new delivery times list
	 */
	public void setSimTimes(ArrayList<Long> simTimes) {
		this.simTimes = simTimes;
	}
	
	
}
