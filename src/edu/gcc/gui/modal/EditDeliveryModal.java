package edu.gcc.gui.modal;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * This modal edits a delivery location.
 * 
 * @author Luke Donmoyer
 */
public class EditDeliveryModal extends Modal {
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();

	private MapLocation editingMapLocation;

	@FXML
	private TextField nameTextField;
	@FXML
	private TextField latitudeTextField;
	@FXML
	private TextField longitudeTextField;

	/**
	 * Event handler for when the delete button is clicked. Will delete the
	 * editing map location and hides this modal.
	 */
	@FXML
	private void deleteButtonClicked() {
		locationXml.delete(editingMapLocation);

		clearAndHide();
	}

	/**
	 * Event handler for when the save button is clicked. Will save the changes
	 * to the currently editing map location and hides this modal.
	 */
	@FXML
	private void onSaveButtonClicked() {
		editingMapLocation.setName(nameTextField.getText());
		editingMapLocation.setxCoord(
			Double.parseDouble(latitudeTextField.getText())
		);
		editingMapLocation.setyCoord(
			Double.parseDouble(longitudeTextField.getText())
		);

		locationXml.update(editingMapLocation);

		clearAndHide();
	}

	/**
	 * Event handler for when the cancel button is clicked. Hides this modal
	 * without modifying the map location XML file.
	 */
	@FXML
	private void onCancelButtonClicked() {
		clearAndHide();
	}

	/**
	 * Clears all the text fields for this modal.
	 */
	private void clear() {
		nameTextField.clear();
		latitudeTextField.clear();
		longitudeTextField.clear();
	}

	/**
	 * Calls the EditDeliveryModal.clear() method and hides this modal.
	 */
	private void clearAndHide() {
		clear();
		hide();
	}

	/**
	 * Sets the currently editing map location to the given location, sets the
	 * fields to the values from the given map location and shows this modal.
	 * 
	 * @param location
	 */
	public void show(final MapLocation location) {
		editingMapLocation = location;

		nameTextField.setText(location.getName());
		latitudeTextField.setText(Double.toString(location.getxCoord()));
		longitudeTextField.setText(Double.toString(location.getyCoord()));

		show();
	}
}
