package edu.gcc.gui;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Modal for adding delivery locations.
 * 
 * @author Luke Donmoyer
 */
public class AddDeliveryModal extends Modal {
	// XML DAO
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();

	// Current campus
	private Campus campus;

	// Text Fields
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField latitudeTextField;
	@FXML
	private TextField longitudeTextField;

	/**
	 * Event handler for when the cancel button is clicked. This clears the text
	 * fields and hides the modal.
	 */
	@FXML
	protected void onCancelButtonClicked() {
		clearTextFields();
		hide();
	}

	/**
	 * Event handler for when the add button is clicked. This adds the location
	 * to the current campus with the information provided in the text fields,
	 * clears the text fields, and then hides the modal.
	 */
	@FXML
	protected void onAddButtonClicked() {
		locationXml.insert(
			new MapLocation(
					Double.parseDouble(latitudeTextField.getText()),
					Double.parseDouble(longitudeTextField.getText()),
					MapLocation.DROPOFF,
					nameTextField.getText(),
					campus.getName()
			)
		);

		clearTextFields();

		hide();
	}

	/**
	 * Sets the text fields to the given map location position.
	 * 
	 * @param location The location to get the latitude and longitude from.
	 */
	public void setMapLocation(final MapLocation location) {
		nameTextField.setText(location.getName());
		latitudeTextField.setText(Double.toString(location.getxCoord()));
		longitudeTextField.setText(Double.toString(location.getyCoord()));
	}

	/**
	 * Show the modal and sets the campus member variable.
	 * 
	 * @param campus The current campus to add to.
	 */
	public void show(final Campus campus) {
		super.show();
		this.campus = campus;
	}

	/**
	 * Clears the text fields in this modal.
	 */
	private void clearTextFields() {
		nameTextField.clear();
		latitudeTextField.clear();
		longitudeTextField.clear();
	}
}
