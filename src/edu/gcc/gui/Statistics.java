package edu.gcc.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import edu.gcc.results.Results;
import edu.gcc.simulation.Simulation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

public class Statistics implements Initializable{
	// TODO: add xml dao for run history stuff here

	
	
	/*
	 * Line Charts
	 */

	//Chart Axes
	@FXML
	private NumberAxis xAxis1 = new NumberAxis();
	@FXML
	private NumberAxis yAxis1 = new NumberAxis();
	@FXML
	private NumberAxis xAxis2 = new NumberAxis();
	@FXML
	private NumberAxis yAxis2 = new NumberAxis();
	@FXML
	private NumberAxis xAxis3 = new NumberAxis();
	@FXML
	private NumberAxis yAxis3 = new NumberAxis();
	 
   /*
    * LineCharts
    */
	@FXML
	private LineChart<Number,Number> chart_one = new LineChart<Number, Number>(xAxis1, yAxis1);
	@FXML
	private LineChart<Number,Number> chart_two = new LineChart<Number, Number>(xAxis2, yAxis2);
	@FXML
	private LineChart<Number,Number> chart_three = new LineChart<Number, Number>(xAxis3, yAxis3);
	
	
	@FXML
	private WindowBar windowBarController;

	
	
	// TODO: change this to whatever run history object we come up with
	@FXML
	private ListView<String> runHistory;

	
	
	@FXML
	private void onDeleteButtonClicked() {
		// TODO: delete currenlty selected run history object from xml here
	}
	
	@FXML
	private void onExportButtonClicked() {
		askForFile().ifPresent(this::saveCsvFile);
	}

	private void saveCsvFile(final File file) {
		// TODO: save to csv file here
	}
	
	public void sendToAllCharts(CompletableFuture<Results> results) throws InterruptedException, ExecutionException{
		Results localResults = results.get();
		sendToFirstChart(localResults);
		sendToSecondChart(localResults);
		sendToThirdChart(localResults);
	}
	
	private void sendToFirstChart(Results results) throws InterruptedException, ExecutionException {
		xAxis1.setLabel("Order Number");
		yAxis1.setLabel("Time (minutes)"); // creating the chart final
		chart_one.setTitle("Time per Delivery");
		XYChart.Series series = new XYChart.Series();
		
		
		
		series.setName(results.getSimType());
		List<Long> deliveryTimes = results.getTimePerOrder();
		
		for (int i = 0; i < deliveryTimes.size(); i++) {
			// Dividing by 60 to get minutes
			series.getData().add(new XYChart.Data(i, deliveryTimes.get(i)/60));
		}
		
		chart_one.getData().add(series);
		
	}
	
	private void sendToSecondChart(Results results) throws InterruptedException, ExecutionException {
		
		//TODO Chart Labels
		
		xAxis2.setLabel("Trip Number");
		yAxis2.setLabel("Orders"); // creating the chart final
		
		chart_two.setTitle("Orders per Trip");
		XYChart.Series series = new XYChart.Series();
		
		
		
		series.setName(results.getSimType());
		List<Integer> ordersPerTrip = results.getordersPerTrip();

		//TODO change algorithm for chart stats
		for (int i = 0; i < ordersPerTrip.size(); i++) {
		
			series.getData().add(new XYChart.Data(i,ordersPerTrip.get(i)));
		}
		
		chart_two.getData().add(series);
		
	}
	
	private void sendToThirdChart(Results results) throws InterruptedException, ExecutionException {
		
		//TODO Chart Labels
		
		xAxis3.setLabel("Distance");
		yAxis3.setLabel("Trip Number"); // creating the chart final
		
		chart_three.setTitle("Distance per Trip");

		XYChart.Series series = new XYChart.Series();
	
		
		
		series.setName(results.getSimType());
		List<Long> tripDistances = results.getDistancePerTrip();
		
		//TODO change algorithm for chart stats
	for (int i = 0; i < tripDistances.size(); i++) {
		
		series.getData().add(new XYChart.Data(i, tripDistances.get(i)));
	}
		
		chart_three.getData().add(series);
		
	}

	private Optional<File> askForFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

		File file = fileChooser.showSaveDialog(Gui.getInstance().getStage());

		return Optional.ofNullable(file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
