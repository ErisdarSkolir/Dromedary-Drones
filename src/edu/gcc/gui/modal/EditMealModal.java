package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

	@FXML
	private Label percentLabel;

	private Meal meal;

	@FXML
	protected void saveButtonClicked() {
		if (meal != null)
			updateMeal(meal);
		else
			mealXml.insert(createNewMeal());

		clearAndHide();
	}

	@FXML
	protected void cancelButtonClicked() {
		clearAndHide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		percentSlider.valueProperty()
				.addListener(
					(observable, oldValue, newValue) -> percentLabel.setText(
						String.format("Percent: %g", (double) newValue)
					)
				);

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
		burgerSpinner.getValueFactory().setValue(meal.getBurgers());
		friesSpinner.getValueFactory().setValue(meal.getFries());
		drinkSpinner.getValueFactory().setValue(meal.getDrinks());
		percentSlider.setValue(meal.getProbability());
	}

	private void clearFields() {
		meal = null;

		nameField.clear();
		burgerSpinner.getValueFactory().setValue(0);
		friesSpinner.getValueFactory().setValue(0);
		drinkSpinner.getValueFactory().setValue(0);
		percentSlider.setValue(0.0);
	}

	private void updateMeal(final Meal meal) {
		meal.setName(nameField.getText());
		meal.setBurgers(burgerSpinner.getValue());
		meal.setDrinks(drinkSpinner.getValue());
		meal.setFries(friesSpinner.getValue());
		meal.setProbability(percentSlider.getValue());

		mealXml.update(meal);
	}

	private void clearAndHide() {
		hide();
		clearFields();
	}

	public void show(final Meal meal) {
		setFields(meal);
		super.show();
	}
}
