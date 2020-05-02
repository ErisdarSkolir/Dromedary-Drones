package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import jfxtras.scene.control.LocalTimeTextField;

public class RunConfigurationModal extends Modal {
	private MealXmlDao mealXml = MealXml.getInstance();

	@FXML
	private LocalTimeTextField timeField;

	@FXML
	private Spinner<Integer> numHoursSpinner;

	@FXML
	private Label mealNumber;
	@FXML
	private Label droneNumber;

	private LoadedMealsModal loadedMealsModalController;

	private ObservableList<Meal> loadedMeals = mealXml.getAllLoadedObservable(
		true
	);

	@FXML
	private void editLoadedMealsClicked() {
		loadedMealsModalController.setOnHideListener(
			() -> mealNumber.setText(Integer.toString(loadedMeals.size()))
		);
		loadedMealsModalController.show();
	}

	@FXML
	private void editLoadedDronesClicked() {

	}

	@FXML
	private void cancelButtonClicked() {
		hide();
	}

	@FXML
	private void runButtonClicked() {
		//List<Meal> meals = loadedMeals.stream().collect(Collectors.toList());
		
		/*CompletableFuture<Results> future = CompletableFuture.supplyAsync(() -> {
			Simulation simulation = new Simulation();
			
			return null;
		});*/
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		mealNumber.setText(Integer.toString(loadedMeals.size()));
	}

	public void setLoadedMealsModalController(
			final LoadedMealsModal loadedMealsModal,
			final EditMealModal editMealModal
	) {
		loadedMealsModalController = loadedMealsModal;
		loadedMealsModal.setEditMealModalController(editMealModal);
	}
}
