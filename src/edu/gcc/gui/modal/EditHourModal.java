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

/**
 * This modal allows you to edit the number of hours a simulation has and the
 * number of orders that are in each hour.
 * 
 * @author Luke Donmoyer
 */
public class EditHourModal extends Modal {
	@FXML
	private Spinner<Integer> hourSpinner;
	@FXML
	private VBox scrollPaneContent;

	private Map<Integer, OrderPerHour> orderPerHourControllers = new HashMap<>();
	private Map<Integer, Parent> orderPerHourComponents = new HashMap<>();

	private int currentNumberOfHours;

	/**
	 * Event listener for the ok button. Hides the modal.
	 */
	@FXML
	private void onOkButtonClicked() {
		hide();
	}

	/**
	 * This method adds and removes the hour components.
	 * 
	 * @param value the current number of hours that should be displayed.
	 */
	private void onSpinnerValueChange(int value) {
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

	/**
	 * This method crates an hour component for the given hour and adds it to
	 * the scrollpane.
	 * 
	 * @param hour The hour the component should be.
	 */
	private void createHourComponent(int hour) {
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

	/**
	 * Gets a list of the number of orders per hour. This should be in order by
	 * the number of the hour.
	 * 
	 * @return A list of ints representing the orders for that hour.
	 */
	public List<Integer> getOrdersForAllHours() {
		return orderPerHourControllers.values()
				.stream()
				.map(OrderPerHour::getNumberOfOrders)
				.collect(Collectors.toList());
	}

	/**
	 * Sets a standard four hours with 38, 45, 60, 30 orders for each hour.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		createHourComponent(1);
		orderPerHourControllers.get(1).setNumberOfOrders(38);
		createHourComponent(2);
		orderPerHourControllers.get(2).setNumberOfOrders(45);
		createHourComponent(3);
		orderPerHourControllers.get(3).setNumberOfOrders(60);
		createHourComponent(4);
		orderPerHourControllers.get(4).setNumberOfOrders(30);

		hourSpinner.getValueFactory().setValue(4);
		hourSpinner.valueProperty()
				.addListener((observable, oldValue, newValue) -> {
					onSpinnerValueChange(newValue);
				});
	}
}
