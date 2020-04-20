package edu.gcc.gui;

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
public class CampusModal extends Modal {
	// XML DAOs
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	// Text fields
	@FXML
	private TextField nameField;
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
		campusXml.insert(new Campus(nameField.getText()));
		locationXml.insert(
			new MapLocation(
					Double.parseDouble(latitudeField.getText()),
					Double.parseDouble(longitudeField.getText()),
					MapLocation.PICKUP,
					"",
					nameField.getText()
			)
		);

		clearFields();
		hide();
	}

	/**
	 * Clears all text fields.
	 */
	private void clearFields() {
		nameField.clear();
		latitudeField.clear();
		longitudeField.clear();
	}
}
