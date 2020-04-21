package edu.gcc.gui.modal;

import edu.gcc.gui.Modal;
import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * This modal handles the editing of a campus and its main pickup location. Any
 * changes to the campus name will be propagated to all of the locations that
 * are a part of that campus.
 * 
 * @author Luke Donmoyer
 */
public class EditCampusModal extends Modal {
	// XML DAOs
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	private Campus currentCampus;

	// Text fields
	@FXML
	private TextField campusNameField;
	@FXML
	private TextField pickupLocationNameField;
	@FXML
	private TextField latitudeField;
	@FXML
	private TextField longitudeField;

	/**
	 * Event handler for when the delete button is clicked. Deltes the campus,
	 * and any locations that were a part of that campus.
	 */
	@FXML
	protected void onDeleteButtonClicked() {
		campusXml.delete(currentCampus);

		locationXml.delete(
			locationXml.getPickupLocationForCampus(currentCampus)
		);

		locationXml.getDropoffListForCampus(currentCampus)
				.stream()
				.forEach(locationXml::delete);

		removeOnHideListeners();
		
		clearFields();
		hide();
	}

	/**
	 * Event handler for when the cancel button is clicked. Hides the modal
	 * without saving anything.
	 */
	@FXML
	protected void onCancelButtonClicked() {
		clearFields();
		hide();
	}

	/**
	 * Event handler for the save button. This modifies all the fields for the
	 * current campus and pickup location. If the campus name is changed, all
	 * locations that were part of that campus will be updated as well.
	 */
	@FXML
	protected void onSaveButtonClicked() {
		// If the campus name is new, update all map locations that are part of
		// that campus and the campus itself.
		if (!campusNameField.getText().equals(currentCampus.getName()))
			setNewCampusName(campusNameField.getText());

		// Update the pickup location.
		MapLocation pickupLocation = locationXml.getPickupLocationForCampus(
			currentCampus
		);
		pickupLocation.setName(pickupLocationNameField.getText());
		pickupLocation.setxCoord(Double.parseDouble(latitudeField.getText()));
		pickupLocation.setyCoord(Double.parseDouble(longitudeField.getText()));
		locationXml.update(pickupLocation);

		clearFields();
		hide();
	}

	public void show(final Campus campus) {
		currentCampus = campus;

		campusNameField.setText(campus.getName());

		MapLocation pickupLocation = locationXml.getPickupLocationForCampus(
			currentCampus
		);
		pickupLocationNameField.setText(pickupLocation.getName());
		latitudeField.setText(Double.toString(pickupLocation.getxCoord()));
		longitudeField.setText(Double.toString(pickupLocation.getyCoord()));

		show();
	}

	/**
	 * This method updates the drop off and pickup locations, and the current
	 * campus itself to be a new name.
	 * 
	 * @param newName The new name to update to.
	 */
	private void setNewCampusName(final String newName) {
		locationXml.getDropoffListForCampus(currentCampus)
				.stream()
				.forEach(location -> {
					location.setCampus(newName);
					locationXml.update(location);
				});

		MapLocation pickupLocation = locationXml.getPickupLocationForCampus(
			currentCampus
		);
		pickupLocation.setCampus(newName);
		locationXml.update(pickupLocation);

		currentCampus.setName(newName);
		campusXml.update(currentCampus);
	}

	/**
	 * Clears all text fields.
	 */
	private void clearFields() {
		campusNameField.clear();
		pickupLocationNameField.clear();
		latitudeField.clear();
		longitudeField.clear();
	}
}
