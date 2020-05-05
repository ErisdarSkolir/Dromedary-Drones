package edu.gcc.gui.modal;

import edu.gcc.drone.Drone;
import edu.gcc.drone.DroneXml;
import edu.gcc.drone.DroneXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class EditDroneModal extends Modal {
	private static final double MILLISECOND_PER_SECOND = 1000;
	private static final double SECOND_PER_MINUTE = 60;

	private DroneXmlDao droneXml = DroneXml.getInstance();

	@FXML
	private TextField nameField;

	@FXML
	private Spinner<Double> speedSpinner;
	@FXML
	private Spinner<Double> maxCapacitySpinner;
	@FXML
	private Spinner<Double> turnAroundTime;
	@FXML
	private Spinner<Double> deliveryTime;
	@FXML
	private Spinner<Double> maxFlightTime;

	private Drone drone;

	@FXML
	private void saveButtonClicked() {
		if (drone != null)
			updateDrone(drone);
		else
			droneXml.insert(createDrone());

		clearAndHide();
	}

	@FXML
	private void cancelButtonClicked() {
		clearAndHide();
	}

	private Drone createDrone() {
		return new Drone(
				nameField.getText(),
				speedSpinner.getValue(),
				maxCapacitySpinner.getValue(),
				turnAroundTime.getValue() * SECOND_PER_MINUTE
						* MILLISECOND_PER_SECOND,
				deliveryTime.getValue() * MILLISECOND_PER_SECOND,
				maxFlightTime.getValue() * SECOND_PER_MINUTE
						* MILLISECOND_PER_SECOND
		);
	}

	private void setFields(final Drone drone) {
		nameField.setText(drone.getName());
		speedSpinner.getValueFactory().setValue(drone.getSpeed());
		maxCapacitySpinner.getValueFactory().setValue(drone.getMaxCapacity());
		turnAroundTime.getValueFactory()
				.setValue(
					drone.getTurnAroundTime() / MILLISECOND_PER_SECOND
							/ SECOND_PER_MINUTE
				);
		deliveryTime.getValueFactory()
				.setValue(drone.getDeliveryTime() / MILLISECOND_PER_SECOND);
		maxFlightTime.getValueFactory()
				.setValue(
					drone.getMaxFlightTime() / MILLISECOND_PER_SECOND
							/ SECOND_PER_MINUTE
				);
	}

	private void clearFields() {
		drone = null;
		
		nameField.clear();
		speedSpinner.getValueFactory().setValue(0.0);
		maxCapacitySpinner.getValueFactory().setValue(0.0);
		turnAroundTime.getValueFactory().setValue(0.0);
		deliveryTime.getValueFactory().setValue(0.0);
		maxFlightTime.getValueFactory().setValue(0.0);
	}

	private void updateDrone(final Drone drone) {
		drone.setName(nameField.getText());
		drone.setSpeed(speedSpinner.getValue());
		drone.setMaxCapacity(maxCapacitySpinner.getValue());
		drone.setTurnAroundTime(
			turnAroundTime.getValue() * SECOND_PER_MINUTE
					* MILLISECOND_PER_SECOND
		);
		drone.setDeliveryTime(deliveryTime.getValue() * MILLISECOND_PER_SECOND);
		drone.setMaxFlightTime(
			maxFlightTime.getValue() * SECOND_PER_MINUTE
					* MILLISECOND_PER_SECOND
		);

		droneXml.update(drone);
	}

	private void clearAndHide() {
		hide();
		clearFields();
	}

	public void show(final Drone drone) {
		this.drone = drone;
		setFields(drone);
		
		super.show();
	}
}
