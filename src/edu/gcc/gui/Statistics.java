package edu.gcc.gui;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import edu.gcc.results.Results;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

public class Statistics implements Initializable {
	// TODO: add xml dao for run history stuff here

	/*
	 * LineCharts
	 */
	@FXML
	private LineChart<Number, Number> chart_one;
	@FXML
	private LineChart<Number, Number> chart_two;
	@FXML
	private LineChart<Number, Number> chart_three;

	private XYChart.Series<Number, Number> fifoSeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries1 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries2 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> fifoSeries3 = new XYChart.Series<>();
	private XYChart.Series<Number, Number> knapsackSeries3 = new XYChart.Series<>();

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
		if (results.getSimType().equals("FIFO")) {
			fifoSeries1.getData()
					.add(
						new XYChart.Data(
								fifoCounter1++,
								results.getAverageTimePerOrder()
						)
					);

		} else {
			knapsackSeries1.getData()
					.add(
						new XYChart.Data(
								knapsackCounter1++,
								results.getAverageTimePerOrder()
						)
					);
		}

	}

	private void sendToSecondChart(Results results) {
		if (results.getSimType().equals("FIFO")) {
			fifoSeries2.getData()
					.add(
						new XYChart.Data(
								fifoCounter2++,
								results.getAverageTimePerOrder()
						)
					);

		} else {
			knapsackSeries2.getData()
					.add(
						new XYChart.Data(
								knapsackCounter2++,
								results.getAverageTimePerOrder()
						)
					);

		}
	}

	private void sendToThirdChart(Results results) {
		if (results.getSimType().equals("FIFO")) {
			fifoSeries3.getData()
					.add(
						new XYChart.Data(
								fifoCounter3++,
								results.getAverageTimePerOrder()
						)
					);

		} else {
			knapsackSeries3.getData()
					.add(
						new XYChart.Data(
								knapsackCounter3++,
								results.getAverageTimePerOrder()
						)
					);
		}

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
		
		chart_one.getData().add(fifoSeries1);
		chart_one.getData().add(knapsackSeries1);
		chart_two.getData().add(fifoSeries2);
		chart_two.getData().add(knapsackSeries2);
		chart_three.getData().add(fifoSeries3);
		chart_three.getData().add(knapsackSeries3);
	}

}
