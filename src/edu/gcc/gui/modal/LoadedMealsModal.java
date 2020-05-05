package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LoadedMealsModal extends Modal {
	private MealXmlDao mealXml = MealXml.getInstance();

	@FXML
	private ListView<Meal> loadedMealList;

	@FXML
	private ListView<Meal> unloadedMealList;

	@FXML
	private Label validateLabel;
	@FXML
	private Button okButton;

	private ObservableList<Meal> loadedMeals = mealXml.getAllLoadedObservable(
		true
	);

	private EditMealModal editMealModalController;

	private Meal selectedMeal;

	@FXML
	private void loadMealClicked() {
		updateSelectedLoaded(true);
		validateProbability();
	}

	@FXML
	private void unloadMealClicked() {
		updateSelectedLoaded(false);
		validateProbability();
	}

	@FXML
	private void addMealClicked() {
		editMealModalController.setOnHideListener(() -> validateProbability());
		editMealModalController.show();
	}

	@FXML
	private void removeMealClicked() {
		mealXml.delete(selectedMeal);
		validateProbability();
	}

	@FXML
	private void editMealClicked() {
		if (selectedMeal == null)
			return;

		editMealModalController.setOnHideListener(() -> validateProbability());
		editMealModalController.show(selectedMeal);
	}

	@FXML
	private void okButtonClicked() {
		hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		loadedMealList.setItems(mealXml.getAllLoadedObservable(true));
		unloadedMealList.setItems(mealXml.getAllLoadedObservable(false));

		ReadOnlyObjectProperty<Meal> loadedMealProperty = loadedMealList
				.getSelectionModel()
				.selectedItemProperty();
		ReadOnlyObjectProperty<Meal> unloadedMealProperty = unloadedMealList
				.getSelectionModel()
				.selectedItemProperty();

		loadedMealProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;

			selectedMeal = newValue;

			if (unloadedMealProperty.get() != null)
				unloadedMealList.getSelectionModel().clearSelection();
		});
		unloadedMealProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;

			selectedMeal = newValue;

			if (loadedMealProperty.get() != null)
				loadedMealList.getSelectionModel().clearSelection();
		});

		validateProbability();
	}

	private boolean validateProbability() {
		if (getProbabilitySum() != 100) {
			okButton.setDisable(true);
			validateLabel.setVisible(true);

			return false;
		} else {
			okButton.setDisable(false);
			validateLabel.setVisible(false);

			return true;
		}
	}

	private double getProbabilitySum() {
		return loadedMeals.stream()
				.map(Meal::getProbability)
				.reduce(0.0, (accumulator, value) -> accumulator + value);
	}

	private void updateSelectedLoaded(final boolean loaded) {
		if (selectedMeal == null || selectedMeal.isLoaded() == loaded)
			return;

		selectedMeal.setLoaded(loaded);
		mealXml.update(selectedMeal);
	}

	public void setEditMealModalController(
			final EditMealModal editMealModalController
	) {
		this.editMealModalController = editMealModalController;
	}
}
