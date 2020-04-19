package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class MealConfigurationModal extends Modal {
	private MealXmlDao mealXml = MealXml.getInstance();
	
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
		mealXml.insert(new Meal("New meal", 0.0f));
	}
	
	@FXML
	protected void removeMealClicked() {
		mealXml.delete(mealList.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	protected void okButtonClicked() {
		hide();
		clearFields();
	}
	
	@FXML
	protected void cancelButtonClicked() {
		hide();
		clearFields();
	}
	
	private void setFields(final Meal meal) {
		nameField.setText(meal.getName());
		burgerSpinner.getValueFactory().setValue(1);
		friesSpinner.getValueFactory().setValue(1);
		drinkSpinner.getValueFactory().setValue(1);
		percentSlider.setValue(meal.getProbability() * 100.0);
	}
	
	private void clearFields() {
		burgerSpinner.getValue();
		burgerSpinner.getValueFactory().setValue(0);
		friesSpinner.getValueFactory().setValue(0);
		drinkSpinner.getValueFactory().setValue(0);
		percentSlider.setValue(0.0);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		mealList.setItems(mealXml.getAllObservable());
		
		mealList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println(newValue);
			setFields(newValue);
		});
	}
}
