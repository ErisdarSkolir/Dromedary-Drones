package edu.gcc.gui;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditDeliveryModal extends Modal {
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();

	private MapLocation editingMapLocation;

	@FXML
	private TextField nameTextField;
	@FXML
	private TextField latitudeTextField;
	@FXML
	private TextField longitudeTextField;

	@FXML
	protected void deleteButtonClicked() {
		locationXml.delete(editingMapLocation);
		
		hide();
		clear();
	}

	@FXML
	protected void onSaveButtonClicked() {
		editingMapLocation.setName(nameTextField.getText());
		editingMapLocation.setxCoord(
			Double.parseDouble(latitudeTextField.getText())
		);
		editingMapLocation.setyCoord(
			Double.parseDouble(longitudeTextField.getText())
		);

		locationXml.update(editingMapLocation);

		hide();
		clear();
	}

	@FXML
	protected void onCancelButtonClicked() {
		hide();
		clear();
	}

	public void show(final MapLocation location) {
		editingMapLocation = location;

		nameTextField.setText(location.getName());
		latitudeTextField.setText(Double.toString(location.getxCoord()));
		longitudeTextField.setText(Double.toString(location.getyCoord()));

		show();
	}

	private void clear() {
		nameTextField.clear();
		latitudeTextField.clear();
		longitudeTextField.clear();
	}
}
