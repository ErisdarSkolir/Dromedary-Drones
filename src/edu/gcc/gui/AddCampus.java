package edu.gcc.gui;

import static edu.gcc.gui.UiText.CANCEL_TEXT;
import static edu.gcc.gui.UiText.SUBMIT_TEXT;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXmlDao;
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

public class AddCampus {
	private GridPane add_campus;
	private Scene add_campus_scene;

	private HBox campus_name_form;
	private HBox campus_latitude_form;
	private HBox campus_longitude_form;
	private HBox campus_button_form;
	
	private TextField name;
	private TextField campus_latitude;
	private TextField campus_longitude;
	
	private Button campus_cancel_button;
	private Button campus_submit_button;
	private Button newCampusButton;
	
	public AddCampus(CampusMap campusMap, CampusXmlDao campusXml,
			CampusDropdown campusDropdown,MapLocationXmlDao locationXml, Stage modal ) {
		
		/*
		 * Init GridPane & Scene
		 */
		add_campus = new GridPane();
		add_campus.setId("modal");
		add_campus_scene = new Scene(add_campus);
		
		/*
		 * Init Forms
		 */
		campus_name_form = new HBox(10);
		campus_latitude_form = new HBox(10);
		campus_longitude_form = new HBox(10);
		campus_button_form = new HBox(10);
		
		/*
		 * Add forms to GridPane
		 */
		add_campus.add(campus_name_form, 0, 0);
		add_campus.add(campus_latitude_form, 0, 1);
		add_campus.add(campus_longitude_form, 0, 2);
		add_campus.add(campus_button_form, 0, 3);
		campus_name_form.setId("form");
		campus_latitude_form.setId("form");
		campus_longitude_form.setId("form");
		campus_button_form.setId("form");
		
		/*
		 * Add Labels to form HBoxes
		 */
		campus_name_form.getChildren().add(new Label("Pickup Name:"));
		campus_latitude_form.getChildren().add(new Label("Latitude:"));
		campus_longitude_form.getChildren().add(new Label("Longitude:"));
		
		/*
		 * Init TextFields
		 */
		name = new TextField();
		campus_latitude = new TextField();
		campus_longitude = new TextField();
		
		/*
		 * Add TextFields to GridPane
		 */
		campus_name_form.getChildren().add(name);
		campus_latitude_form.getChildren().add(campus_latitude);
		campus_longitude_form.getChildren().add(campus_longitude);
		
		/*
		 * Init Buttons
		 */
		campus_cancel_button = new Button(CANCEL_TEXT);
		campus_submit_button = new Button(SUBMIT_TEXT); // $NON-NLS-1$
		
		/*
		 * Set Button Actions
		 */
		
		campus_cancel_button.setOnAction(event -> modal.close());
		
		campus_submit_button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				Campus campus = new Campus(name.getText());

				// TODO: separate the pickuplocation name from the campus name
				// Create a PickupLocation from filled form
				MapLocation temp = new MapLocation(
						Integer.parseInt(campus_latitude.getText()),
						Integer.parseInt(campus_longitude.getText()),
						MapLocation.PICKUP,
						name.getText(),
						campus.getName());
				
				// Set map to newly created campus
				campusMap.setMapLocationData(campus);

				campusXml.insert(campus);
				campusDropdown.setValue(campus);
				locationXml.insert(temp);

				// Clear text boxes
				name.setText("");
				campus_latitude.setText("");
				campus_longitude.setText("");
				// Close
				modal.close();
			}
		});
		
		/*
		 * Add Buttons to GridPane
		 */
		campus_button_form.getChildren().add(campus_cancel_button);
		campus_button_form.getChildren().add(campus_submit_button);
	
	}
	public Scene getScene() {
		return add_campus_scene;
	}
	
}
