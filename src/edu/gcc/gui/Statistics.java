package edu.gcc.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import edu.gcc.results.Results;
import edu.gcc.util.HalfCoreExecutor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

/**
 * This class is the FXML controller for the statistics scene. This is where all
 * the simulation data is shown
 * 
 * @author Luke Donmoyer, Zack Orlaski
 */
public class Statistics implements Initializable {
	/*
	 * LineCharts
	 */
	@FXML
	private LineChart<Number, Number> chartOne;
	@FXML
	private LineChart<Number, Number> chartTwo;
	@FXML
	private LineChart<Number, Number> chartThree;

	@FXML
	private WindowBar windowBarController;

	private XYChart.Series<Number, Number> fifoSeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> greedySeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> greedySeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries3 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> greedySeries3 = new XYChart.Series<>();

	private List<CompletableFuture<Results>> futures = new ArrayList<>();

	/**
	 * Event handler for when the export button is clicked. This will open a file
	 * chooser dialog and then export the current results if a file was actually
	 * chosen.
	 */
	@FXML
	private void onExportButtonClicked() {
		askForFile().ifPresent(this::saveCsvFile);
	}

	/**
	 * This method does the actual exporting to a csv file.
	 * 
	 * @param file The file to write to.
	 */
	private void saveCsvFile(final File file) {
		// TODO: save to csv file here
		Number[][] twoD = new Number[10][6];
		String data = "";
		
		for (int index = 0; index < fifoSeries1.getData().size(); index++) {
			twoD[(int) fifoSeries1.getData().get(index).getXValue()][0] = 
					knapsackSeries3.getData().get(index).getYValue();
			twoD[(int) knapsackSeries1.getData().get(index).getXValue()][1] = 
					knapsackSeries3.getData().get(index).getYValue();
			twoD[(int) fifoSeries2.getData().get(index).getXValue()][2] = 
					knapsackSeries3.getData().get(index).getYValue();
			twoD[(int) knapsackSeries2.getData().get(index).getXValue()][3] = 
					knapsackSeries3.getData().get(index).getYValue();
			twoD[(int) fifoSeries3.getData().get(index).getXValue()][4] = 
					knapsackSeries3.getData().get(index).getYValue();
			twoD[(int) knapsackSeries3.getData().get(index).getXValue()][5] = 
					knapsackSeries3.getData().get(index).getYValue();
		}
		
		data += "Average Time/Delivery, Fifo with Greedy, Greedy with Backtracking, ";
		data += "Average Deliverys/Trip, Fifo with Greedy, Greedy with Backtracking, ";
		data += "Average Distance/Delivery, Fifo with Greedy, Greedy with Backtracking\n ";

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 6; col++) {
				if (col % 2 == 0) {
					data += row+1 + ",";
				}
				data += twoD[row][col] + ",";
			}
			data += "\n";
		}
		
		try {
			FileOutputStream is = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(is);    
	        Writer w = new BufferedWriter(osw);
			w.write(data);
			w.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * This method opens a system file chooser and returns the selected file as an
	 * optional.
	 * 
	 * @return an Optional containing a file if the user did indeed choose a file.
	 */
	private Optional<File> askForFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

		File file = fileChooser.showSaveDialog(Gui.getInstance().getStage());

		return Optional.ofNullable(file);
	}

	/**
	 * Adds the given simulation future to the statistics page and instructs the
	 * completable future to push its results to the correct charts when it
	 * completes.
	 * 
	 * @param index            The iteration of this completable future.
	 * @param simulationFuture The actual completable future which runs the
	 *                         simulation.
	 */
	public void addSimulationFuture(CompletableFuture<Results> simulationFuture) {
		futures.add(simulationFuture);
		simulationFuture.exceptionally(e -> {
			e.printStackTrace();
			return null;
		}).thenAcceptAsync(results -> {
			if (results == null)
				return;


			Platform.runLater(
				() -> sendToAllCharts(results)
			);
		}, HalfCoreExecutor.getService());

	}

	public void sendToAllCharts(Results localResults) {

		sendToFirstChart(localResults);
		sendToSecondChart(localResults);
		sendToThirdChart(localResults);

	}

	private void sendToFirstChart(Results results) {

		List<Long> deliveryTimes = results.getTimePerOrder();

		// Dividing by 60 to get minutes
		if (results.getSimType().equals("FIFO")) {
			for (int i = 0; i < deliveryTimes.size(); i++) {
				fifoSeries1.getData().add(new XYChart.Data<Number, Number>(i, deliveryTimes.get(i)));
				
			}
			chartOne.getData().add(fifoSeries1);
		} else {
			for (int i = 0; i < deliveryTimes.size(); i++)
				greedySeries1.getData().add(new XYChart.Data<Number, Number>(i, deliveryTimes.get(i)));
			chartOne.getData().add(greedySeries1);
		}

	}

	private void sendToSecondChart(Results results) {

		List<Integer> ordersPerTrip = results.getordersPerTrip();

		if (results.getSimType().equals("FIFO")) {
			for (int i = 0; i < ordersPerTrip.size(); i++)
				fifoSeries2.getData().add(new XYChart.Data<Number, Number>(i, ordersPerTrip.get(i)));
			chartTwo.getData().add(fifoSeries2);

		} else {
			for (int i = 0; i < ordersPerTrip.size(); i++)
				greedySeries2.getData().add(new XYChart.Data<Number, Number>(i, ordersPerTrip.get(i)));
			chartTwo.getData().add(greedySeries2);

		}

	}

	private void sendToThirdChart(Results results) {

		List<Long> tripDistances = results.getDistancePerTrip();

		if (results.getSimType().equals("FIFO")) {
			for (int i = 0; i < tripDistances.size(); i++)
				fifoSeries3.getData().add(new XYChart.Data<Number, Number>(i, tripDistances.get(i)));
			chartThree.getData().add(fifoSeries3);

		} else {
			for (int i = 0; i < tripDistances.size(); i++)
				greedySeries3.getData().add(new XYChart.Data<Number, Number>(i, tripDistances.get(i)));
			chartThree.getData().add(greedySeries3);
		}

	}

	/**
	 * Sends the given results to the correct charts. The x value of the result data
	 * will be the given index value.
	 * 
	 * @param simulationType Either FIFO or Knapsack. This tells the method which
	 *                       chart to put the data in.
	 * @param index          The iteration number of the simulation. This tells the
	 *                       method the x value for the result data.
	 * @param results        The actual result data of the simulation.
	 */
//	private void sendToChart(
//			String simulationType,
//			int index,
//			Results results
//	) {
//		sendSeriesData(
//			simulationType.equals("FIFO") ? fifoSeries1 : greedySeries1,
//			index,
//			results.getAverageTimePerOrder()
//		);
//		sendSeriesData(
//			simulationType.equals("FIFO") ? fifoSeries2 : greedySeries2,
//			index,
//			results.getAverageOrdersPerTrip()
//		);
//		sendSeriesData(
//			simulationType.equals("FIFO") ? fifoSeries3 : greedySeries3,
//			index,
//			results.getAverageDistancePerTrip()
//		);
//	}
//
//	/**
//	 * This method actually adds the given data to the given series.
//	 * 
//	 * @param series The series to add the data to.
//	 * @param index  The x value of the data point.
//	 * @param d      The y value of the data point.
//	 */
//	private void sendSeriesData(
//			XYChart.Series<Number, Number> series,
//			int index,
//			double d
//	) {
//		series.getData().add(new Data<>(index, d));
//	}
//
	/**
	 * Clears all data out of the charts.
	 */
	private void clearSeries() {
		fifoSeries1.getData().clear();
		greedySeries1.getData().clear();
		fifoSeries2.getData().clear();
		greedySeries2.getData().clear();
		fifoSeries3.getData().clear();
		greedySeries3.getData().clear();
	}

	/**
	 * Sets up the back button and adds all the series to the charts.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		windowBarController.backButtonEnableAndAction(event -> {
			clearSeries();
			Gui.getInstance().navigateTo("overview");
		});

		fifoSeries1.setName("FIFO");
		greedySeries1.setName("Greedy");
		fifoSeries2.setName("FIFO");
		greedySeries2.setName("Greedy");
		fifoSeries3.setName("FIFO");
		greedySeries3.setName("Greedy");
	}
}
