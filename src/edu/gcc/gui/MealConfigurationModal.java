package edu.gcc.gui;

import edu.gcc.meal.Meal;
import javafx.fxml.FXML;
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
}
