package edu.gcc.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import edu.gcc.results.Results;
import edu.gcc.util.Executor;
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
	private XYChart.Series<Number, Number> knapsackSeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries3 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries3 = new XYChart.Series<>();

	private List<CompletableFuture<Results>> futures = new ArrayList<>();

	/**
	 * Event handler for when the export button is clicked. This will open a
	 * file chooser dialog and then export the current results if a file was
	 * actually chosen.
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
	}

	/**
	 * This method opens a system file chooser and returns the selected file as
	 * an optional.
	 * 
	 * @return an Optional containing a file if the user did indeed choose a
	 *         file.
	 */
	private Optional<File> askForFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

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
	public void addSimulationFuture(
			int index,
			CompletableFuture<Results> simulationFuture
	) {
		futures.add(simulationFuture);
		simulationFuture.exceptionally(e -> {
			e.printStackTrace();
			return null;
		}).thenAcceptAsync(results -> {
			if (results == null)
				return;

			Platform.runLater(
				() -> sendToChart(results.getSimType(), index, results)
			);
		}, Executor.getService());
	}

	/**
	 * Sends the given results to the correct charts. The x value of the result
	 * data will be the given index value.
	 * 
	 * @param simulationType Either FIFO or Knapsack. This tells the method
	 *                       which chart to put the data in.
	 * @param index          The iteration number of the simulation. This tells
	 *                       the method the x value for the result data.
	 * @param results        The actual result data of the simulation.
	 */
	private void sendToChart(
			String simulationType,
			int index,
			Results results
	) {
		sendSeriesData(
			simulationType.equals("FIFO") ? fifoSeries1 : knapsackSeries1,
			index,
			results.getAverageTimePerOrder()
		);
		sendSeriesData(
			simulationType.equals("FIFO") ? fifoSeries2 : knapsackSeries2,
			index,
			results.getAverageOrdersPerTrip()
		);
		sendSeriesData(
			simulationType.equals("FIFO") ? fifoSeries3 : knapsackSeries3,
			index,
			results.getAverageDistancePerTrip()
		);
	}

	/**
	 * This method actually adds the given data to the given series.
	 * 
	 * @param series The series to add the data to.
	 * @param index  The x value of the data point.
	 * @param d      The y value of the data point.
	 */
	private void sendSeriesData(
			XYChart.Series<Number, Number> series,
			int index,
			double d
	) {
		series.getData().add(new Data<>(index, d));
	}

	/**
	 * Clears all data out of the charts.
	 */
	private void clearSeries() {
		fifoSeries1.getData().clear();
		knapsackSeries1.getData().clear();
		fifoSeries2.getData().clear();
		knapsackSeries2.getData().clear();
		fifoSeries3.getData().clear();
		knapsackSeries3.getData().clear();
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

		chartOne.getData().add(fifoSeries1);
		chartOne.getData().add(knapsackSeries1);
		chartTwo.getData().add(fifoSeries2);
		chartTwo.getData().add(knapsackSeries2);
		chartThree.getData().add(fifoSeries3);
		chartThree.getData().add(knapsackSeries3);

		fifoSeries1.setName("FIFO");
		fifoSeries2.setName("FIFO");
		fifoSeries3.setName("FIFO");
		knapsackSeries1.setName("Knapsack");
		knapsackSeries2.setName("Knapsack");
		knapsackSeries3.setName("Knapsack");
	}
}
