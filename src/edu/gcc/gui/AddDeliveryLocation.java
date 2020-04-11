package edu.gcc.gui;

import static edu.gcc.gui.UiText.CANCEL_TEXT;
import static edu.gcc.gui.UiText.SUBMIT_TEXT;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class AddDeliveryLocation{
	
	
/*
 * GridPane and Scene for Modal
 */
private GridPane add_delivery;
private Scene add_delivery_scene;

/*
 * Form boxes for each value in new Delivery Location
 */
private HBox delivery_name_form;
private HBox delivery_button_form;
private HBox delivery_latitude_form;
private HBox delivery_longitude_form;

/*
 * Text fields for forms
 */
private TextField delivery_name;
private TextField delivery_latitude;
private TextField delivery_longitude;

/*
 * Submit and Cancel Buttons
 */
private Button delivery_submit_button;
private Button delivery_cancel_button;

public AddDeliveryLocation(CampusDropdown campusDropdown, MapLocationXmlDao locationXml, Stage modal) {
//	//Create GridPane as a part of a new modal scene
//	add_delivery = new GridPane();
//	add_delivery.setId("modal");
//	add_delivery_scene = new Scene(add_delivery);
//	
//	
//	/*
//	 * Init each form
//	 */
//	
//	//delivery name
//	delivery_name_form = new HBox(10);
//	delivery_name_form.setId("form");
//	
//	//latitude
//	delivery_latitude_form = new HBox(10);
//	delivery_latitude_form.setId("form");
//	
//	//longitude
//	delivery_longitude_form = new HBox(10);
//	delivery_longitude_form.setId("form");
//
//	//submit button
//	delivery_button_form = new HBox(10);
//	delivery_button_form.setId("form");
//	
//	//Init Buttons
//	delivery_cancel_button = new Button(CANCEL_TEXT); // $NON-NLS-1$
//	delivery_submit_button = new Button(SUBMIT_TEXT); // $NON-NLS-1$
//	
//	/*
//	 * add forms to Gridpane
//	 */
//	add_delivery.add(delivery_name_form, 0, 0);
//	add_delivery.add(delivery_latitude_form, 0, 1);
//	add_delivery.add(delivery_longitude_form, 0, 2);
//	add_delivery.add(delivery_button_form, 0, 3);
//	
//	delivery_name_form.getChildren().add(new Label("Dropoff Name:"));
//	delivery_latitude_form.getChildren().add(new Label("Latitude:"));
//	delivery_longitude_form.getChildren().add(new Label("Longitude:"));
//	
//	delivery_name_form.getChildren().add(delivery_name);
//	delivery_latitude_form.getChildren().add(delivery_latitude);
//	delivery_longitude_form.getChildren().add(delivery_longitude);
//	delivery_button_form.getChildren().add(delivery_submit_button);
//	delivery_button_form.getChildren().add(delivery_cancel_button);
//	
//	/*
//	 * Set actions for buttons
//	 */
//	
//	//Cancel
//	delivery_cancel_button.setOnAction(event -> modal.close());
//	
//	//Submit
//	delivery_submit_button.setOnAction(new EventHandler<ActionEvent>() {
//		@Override
//		public void handle(ActionEvent event) {
//
//			// Add location from fields
//			MapLocation temp = new MapLocation(
//					Integer.parseInt(delivery_latitude.getText()),
//					Integer.parseInt(delivery_longitude.getText()),
//					MapLocation.DROPOFF,
//					delivery_name.getText(),
//					campusDropdown.getValue().getName()
//					);
//
//			locationXml.insert(temp);
//
//			// Clear text boxes
//			delivery_name.setText("");
//			delivery_latitude.setText("");
//			delivery_longitude.setText("");
//			// Close
//			modal.close();
//		}
//	});//delivery_submit_button

	
	add_delivery = new GridPane();
	add_delivery.setId("modal");
	add_delivery_scene = new Scene(add_delivery);

	// Form rows
	delivery_name_form = new HBox(10);
	add_delivery.add(delivery_name_form, 0, 0);
	delivery_name_form.setId("form");
	 delivery_latitude_form = new HBox(10);
	add_delivery.add(delivery_latitude_form, 0, 1);
	delivery_latitude_form.setId("form");
	 delivery_longitude_form = new HBox(10);
	add_delivery.add(delivery_longitude_form, 0, 2);
	delivery_longitude_form.setId("form");
	delivery_button_form = new HBox(10);
	add_delivery.add(delivery_button_form, 0, 3);
	delivery_button_form.setId("form");

	// Add text boxes
	delivery_name_form.getChildren().add(new Label("Dropoff Name:"));
	 delivery_name = new TextField();
	delivery_name_form.getChildren().add(delivery_name);

	delivery_latitude_form.getChildren().add(new Label("Latitude:"));
	delivery_latitude = new TextField();
	delivery_latitude_form.getChildren().add(delivery_latitude);

	delivery_longitude_form.getChildren().add(new Label("Longitude:"));
	 delivery_longitude = new TextField();
	delivery_longitude_form.getChildren().add(delivery_longitude);

	// Button to get back from modal
	 delivery_cancel_button = new Button(CANCEL_TEXT); // $NON-NLS-1$
	delivery_cancel_button.setOnAction(event -> modal.close());
	delivery_button_form.getChildren().add(delivery_cancel_button);

	// Submit Button
	delivery_submit_button = new Button(SUBMIT_TEXT); // $NON-NLS-1$
	delivery_submit_button.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			// Add location from fields
			MapLocation temp = new MapLocation(Integer.parseInt(delivery_latitude.getText()),
					Integer.parseInt(delivery_longitude.getText()), MapLocation.DROPOFF, delivery_name.getText(),
					campusDropdown.getValue().getName());

			locationXml.insert(temp);

			// Clear text boxes
			delivery_name.setText("");
			delivery_latitude.setText("");
			delivery_longitude.setText("");
			// Close
			modal.close();
		}
	});
	delivery_button_form.getChildren().add(delivery_submit_button);
	
	
	
	
}

public Scene getScene() {
	return add_delivery_scene;
}

	
}
