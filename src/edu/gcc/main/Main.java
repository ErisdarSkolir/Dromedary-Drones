package edu.gcc.main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import edu.gcc.maplocation.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	
	}

	public static void main(String[] args) {
		//launch(args);
		
		PickupLocation pick = new PickupLocation();
		PickupLocation pick2 = new PickupLocation(0, 10, "2");
		DropoffLocation drop = new DropoffLocation(24, 2, "3");
		DropoffLocation drop2 = new DropoffLocation();
		
		System.out.println(pick);
		System.out.println(pick2);
		System.out.println(pick.distance(pick2));
		System.out.println(pick.distance(drop));
		System.out.println(drop.distance(pick2));
		
	}
}
