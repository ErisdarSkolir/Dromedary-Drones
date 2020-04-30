package edu.gcc.gui.modal;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Modal for adding a new campus. Has a text field for the name, latitude, and
 * longitude.
 * 
 * @author Luke Donmoyer
 */
public class AddCampusModal extends Modal {
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	@FXML
	private TextField campusNameField;
	@FXML
	private TextField pickupLocationNameField;
	@FXML
	private TextField latitudeField;
	@FXML
	private TextField longitudeField;

	/**
	 * Event handler for when the cancel button is clicked. Hides the modal
	 * without saving anything.
	 */
	@FXML
	protected void onCancelButtonClicked() {
		clearFields();
		removeOnHideListeners();
		
		hide();
	}

	/**
	 * Event handler for the add button. This creates a new campus, saves it in
	 * the XML, creates a new pickup location at the same location as the
	 * campus, and saves that. Then the modal fields are cleared and the modal
	 * itself is hidden.
	 */
	@FXML
	protected void onAddButtonClicked() {
		campusXml.insert(new Campus(campusNameField.getText()));
		locationXml.insert(
			new MapLocation(
					Double.parseDouble(latitudeField.getText()),
					Double.parseDouble(longitudeField.getText()),
					MapLocation.PICKUP,
					pickupLocationNameField.getText(),
					campusNameField.getText()
			)
		);

		clearFields();
		hide();
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
