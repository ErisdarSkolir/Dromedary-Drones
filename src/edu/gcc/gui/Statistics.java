package edu.gcc.gui;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.swing.JFileChooser;

import edu.gcc.simulation.Simulation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Statistics extends GridPane {
	private Button backButton = new Button("Back");

	public Statistics() {
		setId(UiText.STATISTICS_ID);

		backButton.setOnAction(e -> Gui.getInstance().navigateTo(UiText.OVERVIEW_ID));

		// Export button
		Button button = new Button("Export");
		button.setOnAction(event -> {
			String sb = "TEST CONTENT";
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("/home/me/Desktop"));
			int retrival = chooser.showSaveDialog(null);
			if (retrival == JFileChooser.APPROVE_OPTION) {
				try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".csv")) {
					fw.write(sb);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		add(backButton, 0, 0);
		add(button, 0, 2);
	}

	public void setSimulation(Simulation sim) {
		add(createChart(sim.getTimeStatistics()), 0, 1);
	}

	public LineChart<Number, Number> createChart(List<Long> times) { // defining the axes
		final NumberAxis xAxis2 = new NumberAxis();
		final NumberAxis yAxis2 = new NumberAxis();
		xAxis2.setLabel("Drone Trip");
		yAxis2.setLabel("Time (seconds)"); // creating the chart final
		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis2, yAxis2);

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
}
