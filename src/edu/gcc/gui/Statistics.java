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
	private LineChart<Number, Number> line_chart;

	public Statistics() {
		setId(UiText.STATISTICS_ID);

		backButton.setOnAction(e -> {
			this.line_chart.setVisible(false);
			Gui.getInstance().navigateTo(UiText.OVERVIEW_ID);
		});

		// Export button
		Button export_button = new Button("Export");
		export_button.setOnAction(event -> {
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
		
		this.backButton.setId("back");
		export_button.setId("export");
		add(this.backButton, 0, 0);
		add(export_button, 0, 2);
	}

	public void setSimulation(Simulation sim) {
		this.line_chart = createChart(sim.getTimeStatistics());
		add(this.line_chart, 0, 1);
	}

	public LineChart<Number, Number> createChart(List<Long> times) { // defining the axes
		final NumberAxis xAxis2 = new NumberAxis();
		final NumberAxis yAxis2 = new NumberAxis();
		xAxis2.setLabel("Order Number");
		yAxis2.setLabel("Time (minutes)"); // creating the chart final
		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis2, yAxis2);

		lineChart.setTitle("Drone Data");
		lineChart.setMaxWidth(500);
		lineChart.setMaxHeight(300);

		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("Delivery time for each order");
		for (int i = 0; i < times.size(); i++) {
			// Dividing by 60 to get minutes
			series.getData().add(new XYChart.Data(i, times.get(i)/60));
		}
		lineChart.getData().add(series);
		return lineChart;
	}
}
