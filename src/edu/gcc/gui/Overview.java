package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Overview implements Initializable{
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	
	@FXML
	private Button newCampusButton;
	
	@FXML
	private ComboBox<Campus> campusDropdown;
	
	@FXML
	private ScatterChart<Number, Number> campusMap;
	
	@FXML
	protected void newCampusButtonClicked() {
		System.out.println("New Campus button clicked");
	}
	
	@FXML
	protected void campusDropdownClicked() { 
		System.out.println("Dropdown clicked");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		campusMap.getData();
	}
}
