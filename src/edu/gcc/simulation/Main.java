package edu.gcc.simulation;

import edu.gcc.maplocation.DropoffLocation;
import edu.gcc.maplocation.PickupLocation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import edu.gcc.gui.*;

public class Main {
	public static void main(String[] args) {

		ArrayList<PickupLocation> Pickup = new ArrayList<PickupLocation>();
		PickupLocation Pick_one = new PickupLocation(2,3,"pick_one");
		PickupLocation Pick_two = new PickupLocation(2,3,"pick_two");

		ArrayList<DropoffLocation> Dropoff = new ArrayList<DropoffLocation>();
		DropoffLocation Drop_one = new DropoffLocation(-2,5,"drop_one");
		DropoffLocation Drop_two = new DropoffLocation(4,5,"drop_two");
		DropoffLocation Drop_three = new DropoffLocation(6,-8,"drop_three");
		DropoffLocation Drop_four = new DropoffLocation(7,8,"drop_four");
		
		
		// Add Platform runable
		GUI window = new GUI(Pickup, Dropoff);
		window.start(new Stage());
		Application.launch(GUI.class,args);
		Platform.runLater(runnable);
	}

}
