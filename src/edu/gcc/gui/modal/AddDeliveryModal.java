package edu.gcc.gui.modal;

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
	 * Show the modal and sets the campus member variable.
	 * 
	 * @param campus The current campus to add to.
	 */
	public void show(final Campus campus) {
		super.show();
		this.campus = campus;
	}

	/**
	 * Show the modal and sets the campus member variable as well as auto-fills
	 * information from the given latitude and longitude.
	 * 
	 * @param campus    The current campus to add to.
	 * @param latitude  The latitude to auto-fill.
	 * @param longitude The longitude to auto-fill.
	 */
	public void show(final Campus campus, final double latitude,
			final double longitude) {
		super.show();
		this.campus = campus;

		latitudeTextField.setText(Double.toString(latitude));
		longitudeTextField.setText(Double.toString(longitude));
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
