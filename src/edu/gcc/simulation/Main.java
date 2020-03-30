package edu.gcc.simulation;

import java.util.ArrayList;
import java.util.List;

import edu.gcc.gui.GUI;
import edu.gcc.maplocation.DropoffLocation;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.PickupLocation;
import javafx.application.Application;

public class Main {
	public static void main(String[] args) {

		ArrayList<PickupLocation> Pickup = new ArrayList<PickupLocation>();
		PickupLocation Pick_one = new PickupLocation(2,3,"pick_one");
		PickupLocation Pick_two = new PickupLocation(2,3,"pick_two");

		List<MapLocation> Dropoff = new ArrayList<>();
		DropoffLocation Drop_one = new DropoffLocation(-2,5,"drop_one");
		DropoffLocation Drop_two = new DropoffLocation(4,5,"drop_two");
		DropoffLocation Drop_three = new DropoffLocation(6,-8,"drop_three");
		DropoffLocation Drop_four = new DropoffLocation(7,8,"drop_four");
		
		
		// Add Platform runable
		Application.launch(GUI.class,args);
		GUI.setDropOffLocations(Dropoff);
	}

}
