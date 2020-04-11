package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.maplocation.Campus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Overview implements Initializable{
	@FXML
	public Button newCampusButton;
	
	@FXML
	public ComboBox<Campus> campusDropdown;
	
	@FXML
	public ScatterChart<Number, Number> campusMap;
	
	@FXML
	public void newCampusButtonClicked(ActionEvent event) {
		System.out.println("New Campus button clicked");
	}
	
	@FXML
	public void campusDropdownClicked(ActionEvent event) { 
		System.out.println("Dropdown clicked");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Initialized");
		System.out.println(newCampusButton);
	}
}
