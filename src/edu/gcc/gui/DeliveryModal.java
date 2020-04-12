package edu.gcc.gui;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeliveryModal extends Modal {
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();

	private Campus campus;

	@FXML
	private TextField latitudeTextField;
	@FXML
	private TextField longitudeTextField;

	@FXML
	protected void onCancelButtonClicked() {
		clearTextFields();
		hide();
	}

	@FXML
	protected void onAddButtonClicked() {
		locationXml.insert(new MapLocation(Double.parseDouble(latitudeTextField.getText()),
				Double.parseDouble(longitudeTextField.getText()), MapLocation.DROPOFF, "name", campus.getName()));

		clearTextFields();

		hide();
	}
	
	private void clearTextFields() {
		latitudeTextField.clear();
		longitudeTextField.clear();
	}

	public void show(final Campus campus) {
		super.show();
		this.campus = campus;
	}
}
