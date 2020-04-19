package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class MealConfigurationModal extends Modal {
	@FXML
	private ListView<Meal> mealList;
	
	@FXML
	private TextField nameField;
	@FXML
	private Spinner<Integer> burgerSpinner;
	@FXML
	private Spinner<Integer> friesSpinner;
	@FXML
	private Spinner<Integer> drinkSpinner;
	
	@FXML
	private Slider percentSlider;
	
	@FXML
	protected void addNewMealClicked() {
		
	}
	
	@FXML
	protected void okButtonClicked() {
		hide();
	}
	
	@FXML
	protected void cancelButtonClicked() {
		hide();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
}
