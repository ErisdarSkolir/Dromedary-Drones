package edu.gcc.gui;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CampusModal extends Modal {
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	@FXML
	private TextField nameField;
	@FXML
	private TextField latField;
	@FXML
	private TextField longField;

	@FXML
	protected void onCancelButtonClicked() {
		clearFields();
		hide();
	}

	@FXML
	protected void onAddButtonClicked() {
		campusXml.insert(new Campus(nameField.getText()));
		locationXml.insert(new MapLocation(Double.parseDouble(latField.getText()),
				Double.parseDouble(longField.getText()), MapLocation.PICKUP, "", nameField.getText()));
		
		clearFields();
		hide();
	}
	
	private void clearFields() {
		nameField.clear();
		latField.clear();
		longField.clear();
	}
}
