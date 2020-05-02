package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import edu.gcc.gui.Gui;
import edu.gcc.gui.Statistics;
import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import edu.gcc.results.Results;
import edu.gcc.simulation.Simulation;
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
//		Statistics statsController = 
//				Gui.getInstance().getControllerForScene("statistics", Statistics.class); 
//		
//		
//		CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> 
//		{
//			Simulation simulation = new Simulation();
//			return simulation.runSimulation();
//		}).thenAccept(result-> statsController.sendToAllCharts(result));
//			
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
