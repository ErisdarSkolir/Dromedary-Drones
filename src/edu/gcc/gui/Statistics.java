package edu.gcc.gui;

import java.io.BufferedWriter;
import java.io.File;
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
		Number[] row1 = new Number[fifoSeries1.getData().size()];
		Number[] row2 = new Number[fifoSeries2.getData().size()];
		Number[] row3 = new Number[fifoSeries3.getData().size()];
		Number[] row4 = new Number[greedySeries1.getData().size()];
		Number[] row5 = new Number[greedySeries2.getData().size()];
		Number[] row6 = new Number[greedySeries3.getData().size()];
		String data = "";

		for (int index = 0; index < fifoSeries1.getData().size(); index++) {
			row1[index] = fifoSeries1.getData().get(index).getYValue();
		}
		for (int index = 0; index < greedySeries1.getData().size(); index++) {
			row1[index] = greedySeries1.getData().get(index).getYValue();
		}
		for (int index = 0; index < fifoSeries2.getData().size(); index++) {
			row1[index] = fifoSeries2.getData().get(index).getYValue();
		}
		for (int index = 0; index < greedySeries2.getData().size(); index++) {
			row1[index] = greedySeries2.getData().get(index).getYValue();
		}
		for (int index = 0; index < fifoSeries3.getData().size(); index++) {
			row1[index] = fifoSeries3.getData().get(index).getYValue();
		}
		for (int index = 0; index < greedySeries3.getData().size(); index++) {
			row1[index] = greedySeries3.getData().get(index).getYValue();
		}

		data += "Average Time/Delivery, Fifo with Greedy, Greedy with Backtracking, ";
		data += "Average Deliverys/Trip, Fifo with Greedy, Greedy with Backtracking, ";
		data += "Average Distance/Delivery, Fifo with Greedy, Greedy with Backtracking\n ";

		int greatest = fifoSeries1.getData().size();
		if (greedySeries1.getData().size() > greatest) {
			greatest = greedySeries1.getData().size();
		} else if (fifoSeries2.getData().size() > greatest) {
			greatest = fifoSeries2.getData().size();
		} else if (greedySeries2.getData().size() > greatest) {
			greatest = greedySeries2.getData().size();
		} else if (fifoSeries3.getData().size() > greatest) {
			greatest = fifoSeries3.getData().size();
		} else if (greedySeries3.getData().size() > greatest) {
			greatest = greedySeries3.getData().size();
		}
		for (int item = 0; item < greatest; item++) {
			data += item + 1 + ",";
			if (item < fifoSeries1.getData().size()) {
				data += row1[item] + ",";
			} else {
				data += " ,";
			}
			if (item < greedySeries1.getData().size()) {
				data += row1[item] + ",";
			} else {
				data += " ,";
			}
			data += item + 1 + ",";
			if (item < fifoSeries2.getData().size()) {
				data += row1[item] + ",";
			} else {
				data += " ,";
			}
			if (item < greedySeries2.getData().size()) {
				data += row1[item] + ",";
			} else {
				data += " ,";
			}
			data += item + 1 + ",";
			if (item < fifoSeries3.getData().size()) {
				data += row1[item] + ",";
			} else {
				data += " ,";
			}
			if (item < greedySeries3.getData().size()) {
				data += row1[item] + ",";
			} else {
				data += " ,";
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
			e1.printStackTrace();
		}
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
			CompletableFuture<Results> simulationFuture
	) {
		futures.add(simulationFuture);
		simulationFuture.exceptionally(e -> {
			e.printStackTrace();
			return null;
		}).thenAcceptAsync(results -> {
			if (results == null)
				return;

			Platform.runLater(() -> sendToAllCharts(results));
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
				fifoSeries1.getData()
						.add(
							new XYChart.Data<Number, Number>(
									i,
									deliveryTimes.get(i)
							)
						);
			}
		} else {
			for (int i = 0; i < deliveryTimes.size(); i++) {
				greedySeries1.getData()
						.add(
							new XYChart.Data<Number, Number>(
									i,
									deliveryTimes.get(i)
							)
						);
			}
		}
	}

	private void sendToSecondChart(Results results) {
		List<Integer> ordersPerTrip = results.getordersPerTrip();

		if (results.getSimType().equals("FIFO")) {
			for (int i = 0; i < ordersPerTrip.size(); i++) {
				fifoSeries2.getData()
						.add(
							new XYChart.Data<Number, Number>(
									i,
									ordersPerTrip.get(i)
							)
						);
			}
		} else {
			for (int i = 0; i < ordersPerTrip.size(); i++) {
				greedySeries2.getData()
						.add(
							new XYChart.Data<Number, Number>(
									i,
									ordersPerTrip.get(i)
							)
						);
			}
		}
	}

	private void sendToThirdChart(Results results) {
		List<Long> tripDistances = results.getDistancePerTrip();

		if (results.getSimType().equals("FIFO")) {
			for (int i = 0; i < tripDistances.size(); i++) {
				fifoSeries3.getData()
						.add(
							new XYChart.Data<Number, Number>(
									i,
									tripDistances.get(i)
							)
						);
			}
		} else {
			for (int i = 0; i < tripDistances.size(); i++) {
				greedySeries3.getData()
						.add(
							new XYChart.Data<Number, Number>(
									i,
									tripDistances.get(i)
							)
						);
			}
		}
	}

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

		chartOne.getData().add(fifoSeries1);
		chartOne.getData().add(greedySeries1);
		chartTwo.getData().add(fifoSeries2);
		chartTwo.getData().add(greedySeries2);
		chartThree.getData().add(fifoSeries3);
		chartThree.getData().add(greedySeries3);
	}
}