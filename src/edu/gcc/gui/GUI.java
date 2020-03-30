package edu.gcc.gui;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import edu.gcc.maplocation.DropoffLocation;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.PickupLocation;
import edu.gcc.simulation.simulation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GUI extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			ArrayList<PickupLocation> Pickup = new ArrayList<PickupLocation>();
			PickupLocation Pick_one = new PickupLocation(2, 3, "pick_one");
			PickupLocation Pick_two = new PickupLocation(2, 3, "pick_two");
			Pickup.add(Pick_one);
			Pickup.add(Pick_two);
			
			List<MapLocation> Dropoff = new ArrayList<>();
			DropoffLocation Drop_one = new DropoffLocation(-2, 5, "drop_one");
			DropoffLocation Drop_two = new DropoffLocation(4, 5, "drop_two");
			DropoffLocation Drop_three = new DropoffLocation(6, -8, "drop_three");
			DropoffLocation Drop_four = new DropoffLocation(7, 8, "drop_four");
			Dropoff.add(Drop_one);
			Dropoff.add(Drop_two);
			Dropoff.add(Drop_three);

			// Data variables
			ObservableList<String> data = FXCollections.observableArrayList();
			for(MapLocation location : Pickup) {
				data.add(location.getName());
			}
			ComboBox<String> location_drop_down = new ComboBox<String>();

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
			campus_name_form.getChildren().add(new Label("Campus Name:"));
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
					data.add(name.getText());
					location_drop_down.setItems(data);
					System.out.println(name.getText());
					System.out.println(campus_latitude.getText());
					System.out.println(campus_longitude.getText());
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
					System.out.println(delivery_latitude.getText());
					System.out.println(delivery_longitude.getText());
					modal.close();
				}
			});
			delivery_button_form.getChildren().add(delivery_submit_button);
			/* End Add Delivery Location Modal */

			/*
			 * Run Simulation Modal GridPane run_simulation = new GridPane();
			 * run_simulation.setId("modal"); Scene run_simulation_scene = new
			 * Scene(run_simulation);
			 * 
			 * // Form rows HBox simulation_button_form = new HBox(10);
			 * run_simulation.add(simulation_button_form,0,4);
			 * simulation_button_form.setId("form");
			 * 
			 * // TODO: Get saved combo meals // For each combo meal for (int i = 0; i < 3;
			 * i ++) { HBox combo_form = new HBox(10); run_simulation.add(combo_form,0,i);
			 * delivery_name_form.setId("form");
			 * 
			 * combo_form.getChildren().add(new Label("Burgers:")); TextField burgers_combo
			 * = new TextField(); burgers_combo.setMaxWidth(30);
			 * combo_form.getChildren().add(burgers_combo); combo_form.getChildren().add(new
			 * Label("Fries:")); TextField fries_combo = new TextField();
			 * fries_combo.setMaxWidth(30); combo_form.getChildren().add(fries_combo);
			 * combo_form.getChildren().add(new Label("Drinks:")); TextField drinks_combo =
			 * new TextField(); drinks_combo.setMaxWidth(30); drinks_combo.setText("10");
			 * combo_form.getChildren().add(drinks_combo); combo_form.getChildren().add(new
			 * Label("Percentage:")); TextField percentage_combo = new TextField();
			 * percentage_combo.setMaxWidth(30);
			 * combo_form.getChildren().add(percentage_combo); }
			 * 
			 * // Button to get back from modal Button run_cancel_button = new
			 * Button("Cancel"); run_cancel_button.setOnAction(new
			 * EventHandler<ActionEvent>() {
			 * 
			 * @Override public void handle(ActionEvent event) { modal.close(); } });
			 * simulation_button_form.getChildren().add(run_cancel_button);
			 * 
			 * // Submit Button Button run_submit_button = new Button("Submit");
			 * run_submit_button.setOnAction(new EventHandler<ActionEvent>() {
			 * 
			 * @Override public void handle(ActionEvent event) { System.out.println("Ran");
			 * modal.close(); primaryStage.setScene(statistics_scene); } });
			 * simulation_button_form.getChildren().add(run_submit_button); End Run
			 * Simulation Modal
			 */

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

			// Drop down
			location_drop_down.setItems(data);
			location_drop_down.setMinWidth(200);
			location_drop_down.getSelectionModel().select(0);
			location_drop_down.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Open modal
					System.out.println(location_drop_down.getValue());
				}
			});
			campus_menu.getChildren().add(location_drop_down);

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

			// TODO: Get data to put into campus map
			// double[][] list = {{2,3},{10,4},{6,9},{-4,3},{-4,-5}};
			ScatterChart<Number,Number> map = createMap(Dropoff);
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
					System.out.println("Ran");
					// simulation ran = new simulation(, 0, 0);
					primaryStage.setScene(statistics_scene);
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

			// Chart
			int[] points = { 3, 4, 5, 6, 3, 2, 2, 4, 5, 6, 5 };

			statistics.add(createChart(points), 0, 1);
			/* End Statistics Page */

			add_campus_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			add_delivery_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// run_simulation_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			overview_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			statistics_scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(overview_scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ScatterChart<Number, Number> createMap(List<MapLocation> locations) {
		// Map
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final ScatterChart<Number, Number> sc = new ScatterChart<Number, Number>(xAxis, yAxis);
		sc.getXAxis().setTickLabelsVisible(false);
		sc.getYAxis().setTickLabelsVisible(false);
		sc.setTitle("Campus Map");
		sc.setMaxWidth(500);
		sc.setMaxHeight(400);

		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Shop Location");
		series1.getData().add(new XYChart.Data(0, 0));

		XYChart.Series series2 = new XYChart.Series();
		series2.setName("Delivery Points");
		for (MapLocation location : locations) {
			series2.getData().add(new XYChart.Data(location.getxCoord(), location.getyCoord()));
		}

		sc.getData().addAll(series1, series2);
		return sc;
	}

	public LineChart<Number, Number> createChart(int[] array) {
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
		for (int i = 0; i < 11; i++) {
			series.getData().add(new XYChart.Data(i, array[i]));
		}
		lineChart.getData().add(series);
		return lineChart;
	}
}