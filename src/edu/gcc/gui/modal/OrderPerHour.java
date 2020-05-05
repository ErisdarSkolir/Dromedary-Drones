package edu.gcc.gui.modal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

public class OrderPerHour {
	@FXML
	private Label hourLabel;
	@FXML
	private Spinner<Integer> orderSpinner;

	private int hourNum;
	
	public int getSpinnerValue() {
		return orderSpinner.getValue();
	}

	public void setSpinnerValue(int value) {
		orderSpinner.getValueFactory().setValue(value);
	}
	
	public void setHour(int hour) {
		hourNum = hour;
		hourLabel.setText(String.format("Hour %d", hour));
	}
	
	public int getHour() {
		return hourNum;
	}
}
