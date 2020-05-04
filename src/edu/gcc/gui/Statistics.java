package edu.gcc.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

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

public class Statistics implements Initializable {
	// TODO: add xml dao for run history stuff here

	/*
	 * LineCharts
	 */
	@FXML
	private LineChart<Number, Number> chartOne;
	@FXML
	private LineChart<Number, Number> chartTwo;
	@FXML
	private LineChart<Number, Number> chartThree;

	private XYChart.Series<Number, Number> fifoSeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries3 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries3 = new XYChart.Series<>();

	@FXML
	private WindowBar windowBarController;

	private List<CompletableFuture<Results>> futures = new ArrayList<>();

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

	public void addSimulationFuture(
			int index,
			CompletableFuture<Results> simulationFuture
	) {
		System.out.println(index);

		futures.add(simulationFuture);
		simulationFuture.exceptionally(e -> {
			System.err.println(e);
			return null;
		}).thenAcceptAsync(results -> {
			System.out.println("Adding ---- " + index);
			Platform.runLater(
				() -> sendToChart(results.getSimType(), index, results)
			);
		}, Executor.getService());
	}

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

	private void sendSeriesData(
			XYChart.Series<Number, Number> series,
			int index,
			long result
	) {
		series.getData().add(new Data<>(index, result));
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
