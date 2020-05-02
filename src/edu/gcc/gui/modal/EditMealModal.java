package edu.gcc.gui.modal;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class EditMealModal extends Modal {
	private MealXmlDao mealXml = MealXml.getInstance();

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

	private Meal meal;

	@FXML
	protected void saveButtonClicked() {
		if (meal != null)
			mealXml.update(meal);
		else
			mealXml.insert(createNewMeal());

		hideAndClose();
	}

	@FXML
	protected void cancelButtonClicked() {
		hideAndClose();
	}

	private Meal createNewMeal() {
		return new Meal(
				nameField.getText(),
				burgerSpinner.getValue(),
				friesSpinner.getValue(),
				drinkSpinner.getValue(),
				(float) percentSlider.getValue()
		);
	}

	private void setFields(final Meal meal) {
		this.meal = meal;

		nameField.setText(meal.getName());
		burgerSpinner.getValueFactory().setValue(1);
		friesSpinner.getValueFactory().setValue(1);
		drinkSpinner.getValueFactory().setValue(1);
		percentSlider.setValue(meal.getProbability() * 100.0);
	}

	private void clearFields() {
		meal = null;

		burgerSpinner.getValue();
		burgerSpinner.getValueFactory().setValue(0);
		friesSpinner.getValueFactory().setValue(0);
		drinkSpinner.getValueFactory().setValue(0);
		percentSlider.setValue(0.0);
	}

	private void hideAndClose() {
		hide();
		clearFields();
	}

	public void show(final Meal meal) {
		setFields(meal);
		super.show();
	}
}
