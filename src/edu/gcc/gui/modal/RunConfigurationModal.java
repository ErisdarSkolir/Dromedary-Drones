package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.drone.Drone;
import edu.gcc.drone.DroneXml;
import edu.gcc.drone.DroneXmlDao;
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
	private DroneXmlDao droneXml = DroneXml.getInstance();

	@FXML
	private LocalTimeTextField timeField;

	@FXML
	private Spinner<Integer> numHoursSpinner;

	@FXML
	private Label mealNumber;
	@FXML
	private Label droneNumber;

	private LoadedMealsModal loadedMealsModalController;
	private LoadedDronesModal loadedDronesModalController;

	private ObservableList<Meal> loadedMeals = mealXml.getAllLoadedObservable(
		true
	);
	private ObservableList<Drone> loadedDrones = droneXml
			.getObservableLoadedDrones(true);

	@FXML
	private void editLoadedMealsClicked() {
		loadedMealsModalController.setOnHideListener(
			() -> mealNumber.setText(Integer.toString(loadedMeals.size()))
		);
		loadedMealsModalController.show();
	}

	@FXML
	private void editLoadedDronesClicked() {
		loadedDronesModalController.setOnHideListener(
			() -> droneNumber.setText(Integer.toString(loadedDrones.size()))
		);
		loadedDronesModalController.show();
	}

	@FXML
	private void cancelButtonClicked() {
		hide();
	}

	@FXML
	private void runButtonClicked() {
		// List<Meal> meals = loadedMeals.stream().collect(Collectors.toList());

		/*
		 * CompletableFuture<Results> future = CompletableFuture.supplyAsync(()
		 * -> { Simulation simulation = new Simulation();
		 * 
		 * return null; });
		 */
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		mealNumber.setText(Integer.toString(loadedMeals.size()));
	}

	public void setControllers(
			final LoadedMealsModal loadedMealsModal,
			final EditMealModal editMealModal,
			final LoadedDronesModal loadedDronesModal,
			final EditDroneModal editDroneModal
	) {
		loadedMealsModalController = loadedMealsModal;
		loadedMealsModal.setEditMealModalController(editMealModal);

		loadedDronesModalController = loadedDronesModal;
		loadedDronesModalController.setEditDroneModalController(editDroneModal);
	}
}
