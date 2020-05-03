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

public class Statistics implements Initializable {
	// TODO: add xml dao for run history stuff here

	/*
	 * Line Charts
	 */

	// Chart Axes
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
	private LineChart<Number, Number> chart_one = new LineChart<>(
			xAxis1,
			yAxis1
		
	);
	@FXML
	private LineChart<Number, Number> chart_two = new LineChart<>(
			xAxis2,
			yAxis2
	);
	@FXML
	private LineChart<Number, Number> chart_three = new LineChart<>(
			xAxis3,
			yAxis3
	);
	
	

	private XYChart.Series fifoSeries1 = new XYChart.Series();
	private XYChart.Series knapsackSeries1 = new XYChart.Series();
	private XYChart.Series fifoSeries2 = new XYChart.Series();
	private XYChart.Series knapsackSeries2 = new XYChart.Series();
	private XYChart.Series fifoSeries3 = new XYChart.Series();
	private XYChart.Series knapsackSeries3 = new XYChart.Series();
	
	private int fifoCounter1 = 0;
	private int knapsackCounter1 = 0;
	private int fifoCounter2 = 0;
	private int knapsackCounter2 = 0;
	private int fifoCounter3 = 0;
	private int knapsackCounter3 = 0;
	
	
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

	public void sendToAllCharts(Results localResults) {

		sendToFirstChart(localResults);
			sendToSecondChart(localResults);
			sendToThirdChart(localResults);

	}

	private void sendToFirstChart(Results results) {
		xAxis1.setLabel("Sim Number");
		yAxis1.setLabel("Average Delivery Time (minutes/order)"); // creating the chart final
		chart_one.setTitle("Average Delivery Time");
		fifoSeries1.setName("FIFO");
		knapsackSeries1.setName("Knapsack");
		
		if(results.getSimType().equals("FIFO")) {
			fifoSeries1.getData().add(new XYChart.Data(fifoCounter1++,results.getAverageTimePerOrder()));
			
		}
		else {
			knapsackSeries1.getData().add(new XYChart.Data(knapsackCounter1++,results.getAverageTimePerOrder()));
					}
		
		

	}

	private void sendToSecondChart(Results results) {


		xAxis2.setLabel("Sim Number");
		yAxis2.setLabel("Average Orders (Orders/Trip)"); // creating the chart final
		chart_two.setTitle("Orders per Trip");
		
		fifoSeries2.setName("FIFO");
		knapsackSeries2.setName("Knapsack");
		
		if(results.getSimType().equals("FIFO")) {
			fifoSeries2.getData().add(new XYChart.Data(fifoCounter2++,results.getAverageTimePerOrder()));
			
		}
		else {
			knapsackSeries2.getData().add(new XYChart.Data(knapsackCounter2++,results.getAverageTimePerOrder()));
			
		}
	}

	private void sendToThirdChart(Results results) {


		xAxis3.setLabel("Sim Number");
		yAxis3.setLabel("Average Distance (Distance/trip)"); // creating the chart final

		chart_three.setTitle("Distance per Trip");

		fifoSeries3.setName("FIFO");
		knapsackSeries3.setName("Knapsack");
		
		if(results.getSimType().equals("FIFO")) {
			fifoSeries3.getData().add(new XYChart.Data(fifoCounter3++,results.getAverageTimePerOrder()));
			
		}
		else {
			knapsackSeries3.getData().add(new XYChart.Data(knapsackCounter3++,results.getAverageTimePerOrder()));
					}

	}

	//Finalize the chart series by adding to the chart.
	public void finishCharts() {
		chart_one.getData().add(fifoSeries1);
		chart_one.getData().add(knapsackSeries1);
		chart_two.getData().add(fifoSeries2);
		chart_two.getData().add(knapsackSeries2);
		chart_three.getData().add(fifoSeries3);
		chart_three.getData().add(knapsackSeries3);
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
		windowBarController.backButtonEnableAndAction(
			event -> Gui.getInstance().navigateTo("overview")
		);
	}
	
	
}
