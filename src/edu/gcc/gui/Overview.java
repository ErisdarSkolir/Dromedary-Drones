package edu.gcc.gui;

import static edu.gcc.gui.UiText.CANCEL_TEXT;
import static edu.gcc.gui.UiText.CSS;
import static edu.gcc.gui.UiText.SUBMIT_TEXT;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import edu.gcc.packing.Fifo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Overview extends GridPane {
	// XML
	private CampusXmlDao campusXml = CampusXml.getInstance();
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private MealXmlDao mealXml = MealXml.getInstance();

	// Meal form data
	private DecimalFormat probabiltyDecimalFormat = new DecimalFormat("0.##");
	private ObservableList<Meal> mealList = mealXml.getAllObservable();

	private HBox campusMenu = new HBox(10);
	private HBox simulationMenu = new HBox(10);

	private CampusMap campusMap = new CampusMap();
	private CampusDropdown campusDropdown = new CampusDropdown(campusMap);

	private Stage modal = new Stage();

	public Overview() {
		setId(UiText.OVERVIEW_ID);

		modal.initModality(Modality.APPLICATION_MODAL);

		createCampusMenu();
		createSimulationMenu();

		campusMap.setMapLocationData(campusDropdown.getValue());

		add(campusMenu, 0, 0);
		add(campusMap.getElement(), 0, 1);
		add(simulationMenu, 0, 2);
	}

	public void createSimulationMenu() {
		simulationMenu.setId("simulation_menu");

		/* Add Delivery Location Modal */
		GridPane add_delivery = new GridPane();
		add_delivery.setId("modal");
		Scene add_delivery_scene = new Scene(add_delivery);

		// Form rows
		HBox delivery_name_form = new HBox(10);
		add_delivery.add(delivery_name_form, 0, 0);
		delivery_name_form.setId("form");
		HBox delivery_latitude_form = new HBox(10);
		add_delivery.add(delivery_latitude_form, 0, 1);
		delivery_latitude_form.setId("form");
		HBox delivery_longitude_form = new HBox(10);
		add_delivery.add(delivery_longitude_form, 0, 2);
		delivery_longitude_form.setId("form");
		HBox delivery_button_form = new HBox(10);
		add_delivery.add(delivery_button_form, 0, 3);
		delivery_button_form.setId("form");

		// Add text boxes
		delivery_name_form.getChildren().add(new Label("Dropoff Name:"));
		TextField delivery_name = new TextField();
		delivery_name_form.getChildren().add(delivery_name);

		delivery_latitude_form.getChildren().add(new Label("Latitude:"));
		TextField delivery_latitude = new TextField();
		delivery_latitude_form.getChildren().add(delivery_latitude);

		delivery_longitude_form.getChildren().add(new Label("Longitude:"));
		TextField delivery_longitude = new TextField();
		delivery_longitude_form.getChildren().add(delivery_longitude);

		// Button to get back from modal
		Button delivery_cancel_button = new Button(CANCEL_TEXT); // $NON-NLS-1$
		delivery_cancel_button.setOnAction(event -> modal.close());
		delivery_button_form.getChildren().add(delivery_cancel_button);

		// Submit Button
		Button delivery_submit_button = new Button(SUBMIT_TEXT); // $NON-NLS-1$
		delivery_submit_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO: Handle exceptions

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
		/* End Add Delivery Location Modal */

		/* Run Simulation Modal */
		GridPane run_simulation = new GridPane();
		run_simulation.setId("modal");
		Scene run_simulation_scene = new Scene(run_simulation);

		// Form rows
		HBox simulation_button_form = new HBox(10);
		run_simulation.add(simulation_button_form, 1, 4);
		simulation_button_form.setId("form");

		// Form rows
		HBox add_edit_form = new HBox(10);
		run_simulation.add(add_edit_form, 2, 2);
		add_edit_form.setId("form");

		// TODO: Get saved combo meals
		// For each combo meal
		VBox combo_form = new VBox(10);
		run_simulation.add(combo_form, 2, 1);
		combo_form.setId("form");

		// Labels
		VBox combo_labels = new VBox(10);
		run_simulation.add(combo_labels, 1, 1);
		combo_labels.setId("labels");

		Label title_label = new Label("Title:");
		title_label.setId("label");
		combo_labels.getChildren().add(title_label);
		Label burgers_label = new Label("Burgers:");
		burgers_label.setId("label");
		combo_labels.getChildren().add(burgers_label);
		Label fries_label = new Label("Fires:");
		fries_label.setId("label");
		combo_labels.getChildren().add(fries_label);
		Label drinks_label = new Label("Drinks:");
		drinks_label.setId("label");
		combo_labels.getChildren().add(drinks_label);
		Label percent_label = new Label("Percent:");
		percent_label.setId("label");
		combo_labels.getChildren().add(percent_label);

		TextField title_combo = new TextField();
		title_combo.setMaxWidth(100);
		combo_form.getChildren().add(title_combo);
		TextField burgers_combo = new TextField();
		burgers_combo.setMaxWidth(30);
		combo_form.getChildren().add(burgers_combo);
		TextField fries_combo = new TextField();
		fries_combo.setMaxWidth(30);
		combo_form.getChildren().add(fries_combo);
		TextField drinks_combo = new TextField();
		drinks_combo.setMaxWidth(30);
		combo_form.getChildren().add(drinks_combo);
		TextField percentage_combo = new TextField();
		percentage_combo.setMaxWidth(30);
		combo_form.getChildren().add(percentage_combo);

		Meal addNewMeal = new Meal("ADD NEW", 0.0f);
		mealList.add(addNewMeal);

		// Add edit combo button
		Button add_edit_button = new Button("Add/Edit");
		add_edit_button.setOnAction(event -> {
			Meal meal = new Meal(title_combo.getText(),
					Float.parseFloat(percentage_combo.getText().replace("%", "")) / 100.0f); //$NON-NLS-2$
			mealXml.insert(meal);
			mealList.remove(addNewMeal); // Bit of a hack to keep the ADD NEW element at the bottom
			mealList.add(addNewMeal);
		});
		add_edit_form.getChildren().add(add_edit_button);

		// Delete combo button
		Button delete_button = new Button("Delete");
		add_edit_form.getChildren().add(delete_button);

		// Button to get back from modal
		Button run_cancel_button = new Button(CANCEL_TEXT); // $NON-NLS-1$
		run_cancel_button.setOnAction(event -> modal.close());
		simulation_button_form.getChildren().add(run_cancel_button);

		// List view
		ListView<Meal> combo_list = new ListView<>();
		combo_list.setItems(mealList);
		combo_list.setPrefWidth(100);
		combo_list.setPrefHeight(70);
		combo_list.setOnMouseClicked(event -> {
			if (!combo_list.getSelectionModel().getSelectedItem().equals(addNewMeal)) {
				Meal currentMeal = combo_list.getSelectionModel().getSelectedItem();

				title_combo.setText(currentMeal.getName());
				percentage_combo.setText(
						String.format("%s%%", probabiltyDecimalFormat.format(currentMeal.getProbability() * 100.0f)));
				delete_button.setDisable(false);
			} else {
				title_combo.setText("");
				percentage_combo.setText("");
				delete_button.setDisable(true);
			}
		});
		run_simulation.add(combo_list, 0, 1);

		delete_button.setOnAction(event -> mealXml.delete(combo_list.getSelectionModel().getSelectedItem()));

		// Submit Button
		Button run_submit_button = new Button(SUBMIT_TEXT);
		run_submit_button.setOnAction(event -> {
			modal.close();
			Gui.getInstance().runSimulation(mealXml.getAll(),
					locationXml.getDropoffReactiveForCampus(campusDropdown.getValue().getName()), new Fifo());
		});
		simulation_button_form.getChildren().add(run_submit_button);

		/* End Run Simulation Modal */

		// New delivery location
		Button newDeliveryButon = new Button("New Delivery Location");
		newDeliveryButon.setOnAction(event -> {
			// Open modal
			createAndShowModal("Add Delivery Location", add_delivery_scene);
		});

		// Run Simulation Button
		Button runButton = new Button("Run Simulation");
		runButton.setOnAction(event -> {
			createAndShowModal("Run Simulation", run_simulation_scene);
		});

		simulationMenu.getChildren().addAll(newDeliveryButon, runButton);

		add_delivery_scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
		run_simulation_scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
	}

	public void createCampusMenu() {
		campusMenu.setId("campus_menu");
		campusMenu.getChildren().add(campusDropdown.getElement());

		/* Add Campus Modal */
		GridPane add_campus = new GridPane();
		add_campus.setId("modal");
		Scene add_campus_scene = new Scene(add_campus);

		// Form rows
		HBox campus_name_form = new HBox(10);
		add_campus.add(campus_name_form, 0, 0);
		campus_name_form.setId("form");
		HBox campus_latitude_form = new HBox(10);
		add_campus.add(campus_latitude_form, 0, 1);
		campus_latitude_form.setId("form");
		HBox campus_longitude_form = new HBox(10);
		add_campus.add(campus_longitude_form, 0, 2);
		campus_longitude_form.setId("form");
		HBox campus_button_form = new HBox(10);
		add_campus.add(campus_button_form, 0, 3);
		campus_button_form.setId("form");

		// Add text boxes
		campus_name_form.getChildren().add(new Label("Pickup Name:"));
		TextField name = new TextField();
		campus_name_form.getChildren().add(name);

		campus_latitude_form.getChildren().add(new Label("Latitude:"));
		TextField campus_latitude = new TextField();
		campus_latitude_form.getChildren().add(campus_latitude);

		campus_longitude_form.getChildren().add(new Label("Longitude:"));
		TextField campus_longitude = new TextField();
		campus_longitude_form.getChildren().add(campus_longitude);

		// Button to get back from modal
		Button campus_cancel_button = new Button(CANCEL_TEXT);
		campus_cancel_button.setOnAction(event -> modal.close());
		campus_button_form.getChildren().add(campus_cancel_button);

		// Submit Button
		Button campus_submit_button = new Button(SUBMIT_TEXT); // $NON-NLS-1$
		campus_submit_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO: Handle exceptions

				Campus campus = new Campus(name.getText());

				// TODO: separate the pickuplocation name from the campus name
				// Create a PickupLocation from filled form
				MapLocation temp = new MapLocation(Integer.parseInt(campus_latitude.getText()),
						Integer.parseInt(campus_longitude.getText()), MapLocation.PICKUP, name.getText(),
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
		campus_button_form.getChildren().add(campus_submit_button);

		// New campus
		Button newCampusButton = new Button("New Campus");
		newCampusButton.setOnAction(event -> createAndShowModal("Add Campus", add_campus_scene));
		campusMenu.getChildren().add(newCampusButton);

		add_campus_scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
	}

	private void createAndShowModal(final String title, final Scene scene) {
		modal.setTitle(title);
		modal.setScene(scene);
		modal.showAndWait();
	}
}
