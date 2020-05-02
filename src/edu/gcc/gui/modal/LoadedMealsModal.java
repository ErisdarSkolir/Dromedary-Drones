package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class LoadedMealsModal extends Modal {
	private MealXmlDao mealXml = MealXml.getInstance();

	@FXML
	private ListView<Meal> loadedMealList;

	@FXML
	private ListView<Meal> unloadedMealList;

	private EditMealModal editMealModalController;

	private Meal selectedMeal;

	@FXML
	private void loadMealClicked() {
		updateSelectedLoaded(true);
	}

	@FXML
	private void unloadMealClicked() {
		updateSelectedLoaded(false);
	}

	@FXML
	private void addMealClicked() {
		editMealModalController.show();
	}

	@FXML
	private void removeMealClicked() {
		mealXml.delete(selectedMeal);
	}

	@FXML
	private void editMealClicked() {
		if (selectedMeal == null)
			return;

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
				loadedMealList.getSelectionModel().clearSelection();
		});
		unloadedMealProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;

			selectedMeal = newValue;

			if (loadedMealProperty.get() != null)
				loadedMealList.getSelectionModel().clearSelection();
		});
	}

	private void updateSelectedLoaded(final boolean loaded) {
		if (selectedMeal == null)
			return;

		selectedMeal.setLoaded(loaded);
		mealXml.update(selectedMeal);
	}

	public void setEditMealModalController(
			final EditMealModal editMealModalController
	) {
		this.editMealModalController = editMealModalController;
	}

	/*
	 * private void loadedMealListSelectionListener( Observable observable, Meal
	 * newValue, Meal oldValue ) { if (newValue == null) return;
	 * 
	 * selectedMeal = newValue;
	 * 
	 * if (unloadedMealList.getSelectionModel() .selectedItemProperty() .get()
	 * != null) loadedMealList.getSelectionModel().clearSelection(); }
	 */
}
