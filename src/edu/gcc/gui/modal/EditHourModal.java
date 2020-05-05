package edu.gcc.gui.modal;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class EditHourModal extends Modal {
	@FXML
	private Spinner<Integer> hourSpinner;
	@FXML
	private VBox scrollPaneContent;

	private Map<Integer, OrderPerHour> orderPerHourControllers = new HashMap<>();
	private Map<Integer, Parent> orderPerHourComponents = new HashMap<>();

	private int currentNumberOfHours;

	public void onOkButtonClicked() {
		hide();
	}

	public void onSpinnerValueChange(int value) {
		if (value == currentNumberOfHours)
			return;

		if (value < currentNumberOfHours) {
			for (int i = currentNumberOfHours; i > value; i--) {
				orderPerHourControllers.remove(i);
				Parent parent = orderPerHourComponents.remove(i);

				scrollPaneContent.getChildren().remove(parent);
			}

			currentNumberOfHours = value;
		} else {
			for (int i = currentNumberOfHours; i < value; i++) {
				createHourComponent(i + 1);
			}
		}
	}

	public void createHourComponent(int hour) {
		try {
			FXMLLoader loader = new FXMLLoader(
					this.getClass().getResource("order_per_hour.fxml")
			);

			Parent parent = loader.load();
			OrderPerHour controller = loader.getController();

			controller.setHour(hour);
			scrollPaneContent.getChildren().add(parent);

			orderPerHourComponents.put(hour, parent);
			orderPerHourControllers.put(hour, controller);

			currentNumberOfHours++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Integer> getOrdersForAllHours() {
		return orderPerHourControllers.values()
				.stream()
				.map(OrderPerHour::getSpinnerValue)
				.collect(Collectors.toList());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		createHourComponent(1);
		orderPerHourControllers.get(1).setSpinnerValue(38);
		createHourComponent(2);
		orderPerHourControllers.get(2).setSpinnerValue(45);
		createHourComponent(3);
		orderPerHourControllers.get(3).setSpinnerValue(60);
		createHourComponent(4);
		orderPerHourControllers.get(4).setSpinnerValue(30);

		hourSpinner.getValueFactory().setValue(4);
		hourSpinner.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					onSpinnerValueChange(newValue);
				});
	}
}
