package edu.gcc.gui.modal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

/**
 * This class represents an hour component with a spinner that contains the
 * number of orders for that hour.
 * 
 * @author Luke Donmoyer
 */
public class OrderPerHour {
	@FXML
	private Label hourLabel;
	@FXML
	private Spinner<Integer> orderSpinner;

	private int hourNum;

	/**
	 * Gets the number of orders in the order spinner.
	 * 
	 * @return The number of orders for this hour.
	 */
	public int getNumberOfOrders() {
		return orderSpinner.getValue();
	}

	/**
	 * Set the number of orders in the spinner.
	 * 
	 * @param value The number of orders.
	 */
	public void setNumberOfOrders(int value) {
		orderSpinner.getValueFactory().setValue(value);
	}

	/**
	 * Sets the current hour represented by this component This updates the
	 * label to reflect the hour number.
	 * 
	 * @param hour The hour to set to.
	 */
	public void setHour(int hour) {
		hourNum = hour;
		hourLabel.setText(String.format("Hour %d", hour));
	}

	/**
	 * Get the current hour represented by this component.
	 * 
	 * @return the current hour number.
	 */
	public int getHour() {
		return hourNum;
	}
}
