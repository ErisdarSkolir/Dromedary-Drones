package edu.gcc.gui.modal;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

/**
 * FXML Modal for editing the contents of an individual Meal Object
 * 
 * @author Luke Donmoyer, Zack Orlaski
 */
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
	private DecimalFormat decimalFormat = new DecimalFormat("###.##");

	/**
	 * Event Handler for the saving the Meal in its current state. Adds the Meal
	 * to the list if not already existing
	 */
	@FXML
	protected void saveButtonClicked() {
		if (meal != null)
			updateMeal(meal);
		else
			mealXml.insert(createNewMeal());

		clearAndHide();
	}

	/**
	 * Event Handler for the cancel button. Closes the modal
	 */
	@FXML
	protected void cancelButtonClicked() {
		clearAndHide();
	}

	/**
	 * Event handler for when the mouse is released from the percent slider.
	 * This will round the value to the nearest whole number.
	 */
	@FXML
	private void onMouseReleased() {
		double currentValue = percentSlider.getValue();
		percentSlider.setValue(Math.round(currentValue));
	}

	/**
	 * Initializes the modal
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		percentSlider.valueProperty()
				.addListener(
					(observable, oldValue, newValue) -> percentLabel.setText(
						String.format(
							"Percent: %s%%",
							decimalFormat.format(Math.round((double) newValue))
						)
					)
				);
	}

	/**
	 * Creates a Meal based on the currently filled fields
	 */
	private Meal createNewMeal() {
		return new Meal(
				nameField.getText(),
				burgerSpinner.getValue(),
				friesSpinner.getValue(),
				drinkSpinner.getValue(),
				(float) percentSlider.getValue()
		);
	}

	/**
	 * Fills the modal fields with a values from the passed Meal
	 * 
	 * @param meal the meal to take fill the field data with
	 */
	private void setFields(final Meal meal) {
		this.meal = meal;

		nameField.setText(meal.getName());
		burgerSpinner.getValueFactory().setValue(meal.getBurgers());
		friesSpinner.getValueFactory().setValue(meal.getFries());
		drinkSpinner.getValueFactory().setValue(meal.getDrinks());
		percentSlider.setValue(meal.getProbability());
	}

	/**
	 * Clears field values
	 */
	private void clearFields() {
		meal = null;

		nameField.clear();
		burgerSpinner.getValueFactory().setValue(0);
		friesSpinner.getValueFactory().setValue(0);
		drinkSpinner.getValueFactory().setValue(0);
		percentSlider.setValue(0.0);
	}

	/**
	 * Updates the values in a meal with the values in the modal fields
	 * 
	 * @param meal this meal gets updated
	 */
	private void updateMeal(final Meal meal) {
		meal.setName(nameField.getText());
		meal.setBurgers(burgerSpinner.getValue());
		meal.setDrinks(drinkSpinner.getValue());
		meal.setFries(friesSpinner.getValue());
		meal.setProbability(percentSlider.getValue());

		mealXml.update(meal);
	}

	/**
	 * Close the Modal
	 */
	private void clearAndHide() {
		hide();
		clearFields();
	}

	/**
	 * Open the modal with the specified meal values in the fields
	 * 
	 * @param meal the meal with the values to fill the fields with
	 */
	public void show(final Meal meal) {
		setFields(meal);
		super.show();
	}
}
