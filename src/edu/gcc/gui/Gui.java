package edu.gcc.gui;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.order.Order;
import edu.gcc.packing.Fifo;
import edu.gcc.simulation.Simulation;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Gui extends Application {
	private static Gui instance;

	public Gui() {
		Gui.initInstance(this);
	}

	private static void initInstance(final Gui inst) {
		if (inst != null && Gui.instance == null)
			Gui.instance = inst;
	}

	public static Gui getInstance() {
		if (instance != null)
			return instance;

		throw new IllegalStateException(String.format("%s is not initialized", Gui.class));
	}

	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	private ObservableList<XYChart.Data<Number, Number>> mapDropoffLocations;
	private ObservableList<XYChart.Data<Number, Number>> mapPickupLocations;
	private ObservableList<Campus> campusList = CampusXml.getInstance().getAll();
	private ComboBox<Campus> campusDropdown = createLocationDropdown();
	
	public ComboBox<Campus> createLocationDropdown() {
		ComboBox<Campus> result = new ComboBox<>();

		result.setItems(campusList);
		result.setMinWidth(200);
		result.getSelectionModel().select(0);
		result.setOnAction(event -> setMapLocationData(campusDropdown.getValue()));

		return result;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Overview page
			GridPane overview = new GridPane();
			overview.setId("overview");

			// Statistics page
			GridPane statistics = new GridPane();
			overview.setId("statistics");

			// Scenes
			Scene overview_scene = new Scene(overview, 500, 500);
			Scene statistics_scene = new Scene(statistics, 500, 500);

			// Modal
			Stage modal = new Stage();
			modal.initModality(Modality.APPLICATION_MODAL);

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
			Button campus_cancel_button = new Button("Cancel");
			campus_cancel_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					modal.close();
				}
			});
			campus_button_form.getChildren().add(campus_cancel_button);

			// Submit Button
			Button campus_submit_button = new Button("Submit");
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
					// Set map to new location
					ArrayList<MapLocation> empty = new ArrayList<>();
					empty.add(temp);
					ScatterChart<Number, Number> map = createMap();
					overview.add(map, 0, 1);
					//

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
			/* End Add Campus Modal */

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
			Button delivery_cancel_button = new Button("Cancel");
			delivery_cancel_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					modal.close();
				}
			});
			delivery_button_form.getChildren().add(delivery_cancel_button);

			// Submit Button
			Button delivery_submit_button = new Button("Submit");
			delivery_submit_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// TODO: Handle exceptions

					// Add location from fields
					MapLocation temp = new MapLocation(Integer.parseInt(delivery_latitude.getText()),
							Integer.parseInt(delivery_longitude.getText()), MapLocation.DROPOFF,
							delivery_name.getText(), campusDropdown.getValue().getName());

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
			run_simulation.add(simulation_button_form,1,4);
			simulation_button_form.setId("form");
			
			// Form rows
			HBox add_edit_form = new HBox(10);
			run_simulation.add(add_edit_form,2,2);
			add_edit_form.setId("form");
			  
			// TODO: Get saved combo meals
			// For each combo meal
			VBox combo_form = new VBox(10); 
			run_simulation.add(combo_form,2,1);
			combo_form.setId("form");
			
			//Labels
			VBox combo_labels = new VBox(10); 
			run_simulation.add(combo_labels,1,1);
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
			
			// Add edit combo button
			Button add_edit_button = new Button("Add/Edit");
			add_edit_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					System.out.println("Add/Edit");
					}
				});
			add_edit_form.getChildren().add(add_edit_button);
			// Delete combo button
			Button delete_button = new Button("Delete");
			add_edit_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					System.out.println("Delete");
					}
				});
			add_edit_form.getChildren().add(delete_button);
			
			// Button to get back from modal
			Button run_cancel_button = new Button("Cancel");
			run_cancel_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					modal.close();
					}
				});
			simulation_button_form.getChildren().add(run_cancel_button);
			  
			// List view
			ListView<String> combo_list = new ListView<String>();
			ObservableList<String> items = FXCollections.observableArrayList (
			    "Single", "2 Drinks", "Combo", "ADD NEW");
			combo_list.setItems(items);
			combo_list.setPrefWidth(100);
			combo_list.setPrefHeight(70);
			combo_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					title_combo.setText("");
					delete_button.setDisable(true);
					if (combo_list.getSelectionModel().getSelectedItem()!="ADD NEW") {
						title_combo.setText(combo_list.getSelectionModel().getSelectedItem().toString());
						delete_button.setDisable(false);
					}
				}
			});
			run_simulation.add(combo_list,0,1);
			
			// Submit Button
			Button run_submit_button = new Button("Submit");
			run_submit_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) { 
					Simulation sim = runSimulation();
					statistics.add(createChart(sim.getTimeStatistics()), 0, 1);
					modal.close();
					primaryStage.setScene(statistics_scene);
				}
			});
			simulation_button_form.getChildren().add(run_submit_button);
			  	
			/* End Run Simulation Modal */
			 

			/* Main Menu */
			// Grids
			// Select/add new campus
			HBox campus_menu = new HBox(10);
			overview.add(campus_menu, 0, 0);
			campus_menu.setId("campus_menu");

			// Run simulation/add new delivery location
			HBox simulation_menu = new HBox(10);
			overview.add(simulation_menu, 0, 2);
			simulation_menu.setId("simulation_menu");

			campus_menu.getChildren().add(campusDropdown);

			// New campus
			Button new_campus_button = new Button("New Campus");
			new_campus_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Open modal
					modal.setTitle("Add Campus");
					modal.setScene(add_campus_scene);
					modal.showAndWait();
				}
			});
			campus_menu.getChildren().add(new_campus_button);

			ScatterChart<Number, Number> map = createMap();
			overview.add(map, 0, 1);

			// New delivery location
			Button new_delivery_button = new Button("New Delivery Location");
			new_delivery_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Open modal
					modal.setTitle("Add Delivery Location");
					modal.setScene(add_delivery_scene);
					modal.showAndWait();
				}
			});
			simulation_menu.getChildren().add(new_delivery_button);

			// Run Simulation Button
			Button run_button = new Button("Run Simulation");
			run_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					modal.setTitle("Run Simulation");
					modal.setScene(run_simulation_scene);
					modal.showAndWait();
				}
			});
			simulation_menu.getChildren().add(run_button);
			/* End Main Menu */

			/* Statistics Page */
			// Back Button
			Button back_button = new Button("Back");
			back_button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					primaryStage.setScene(overview_scene);
				}
			});
			statistics.add(back_button, 0, 0);

			// Export button
			Button button = new Button("Export");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					String sb = "TEST CONTENT";
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File("/home/me/Desktop"));
					int retrival = chooser.showSaveDialog(null);
					if (retrival == JFileChooser.APPROVE_OPTION) {
						try {
							FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".csv");
							fw.write(sb.toString());
							fw.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

				}
			});
			statistics.add(button, 0, 2);

			/* End Statistics Page */

			add_campus_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			add_delivery_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			run_simulation_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			overview_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			statistics_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(overview_scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Creates a map
	public ScatterChart<Number, Number> createMap() {
		// Map
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
		sc.getXAxis().setTickLabelsVisible(false);
		sc.getYAxis().setTickLabelsVisible(false);
		sc.setTitle("Campus Map");
		sc.setMaxWidth(500);
		sc.setMaxHeight(400);

		// Pickup Points
		Series<Number, Number> series1 = new Series<>();
		series1.setName("Shop Location");
		mapPickupLocations = series1.getData();

		// Dropoff Points
		Series<Number, Number> series2 = new Series<>();
		series2.setName("Delivery Points");
		mapDropoffLocations = series2.getData();

		setMapLocationData(campusDropdown.getValue());

		sc.getData().addAll(series1, series2);
		return sc;
	}

	public void setMapLocationData(final Campus campus) {
		if (campus == null)
			return;
		
		mapDropoffLocations.clear();
		mapPickupLocations.clear();
		
		ObservableList<MapLocation> dropoffLocations = locationXml.getDropoffReactiveForCampus(campus.getName());
		dropoffLocations.stream().map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
				.forEach(mapDropoffLocations::add);
		dropoffLocations.addListener(new ListChangeListener<MapLocation>() {
			@Override
			public void onChanged(Change<? extends MapLocation> c) {
				while (c.next()) {
					c.getRemoved().stream().map(location -> new Data<>(location.getxCoord(), location.getyCoord()))
							.forEach(mapDropoffLocations::remove);

					c.getAddedSubList().stream()
							.map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
							.forEach(mapDropoffLocations::add);
				}
			}
		});

		ObservableList<MapLocation> pickupLocations = locationXml.getPickupReactiveForCampus(campus.getName());
		pickupLocations.stream().map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
				.forEach(mapPickupLocations::add);
		pickupLocations.addListener(new ListChangeListener<MapLocation>() {
			@Override
			public void onChanged(Change<? extends MapLocation> c) {
				while (c.next()) {
					c.getRemoved().stream().map(location -> new Data<>(location.getxCoord(), location.getyCoord()))
							.forEach(mapPickupLocations::remove);

					c.getAddedSubList().stream()
							.map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
							.forEach(mapPickupLocations::add);
				}
			}
		});
	}

	public LineChart<Number, Number> createChart(ArrayList<Long> times) {
		// defining the axes
		final NumberAxis xAxis2 = new NumberAxis();
		final NumberAxis yAxis2 = new NumberAxis();
		xAxis2.setLabel("Drone Trip");
		yAxis2.setLabel("Time (seconds)");
		// creating the chart
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis2, yAxis2);

		lineChart.setTitle("Drone Data");
		lineChart.setMaxWidth(400);
		lineChart.setMaxHeight(300);

		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("Average Number of Something");
		for (int i = 0; i < times.size(); i++) {
			series.getData().add(new XYChart.Data(i, times.get(i)));
		}
		lineChart.getData().add(series);
		return lineChart;
	}

	public Simulation runSimulation() {
		// We (Lake and Ethan) believe that the shop location is being counted as a delivery point
		Simulation sim = new Simulation(locationXml.getDropoffReactiveForCampus(campusDropdown.getValue().getName()), new Fifo(), 1);
		sim.runSimulation();
		return sim;
	}
}