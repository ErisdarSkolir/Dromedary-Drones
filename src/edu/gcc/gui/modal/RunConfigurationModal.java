package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import edu.gcc.drone.Drone;
import edu.gcc.drone.DroneXml;
import edu.gcc.drone.DroneXmlDao;
import edu.gcc.gui.Gui;
import edu.gcc.gui.Statistics;
import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import edu.gcc.order.Order;
import edu.gcc.order.OrderGenerator;
import edu.gcc.packing.Fifo;
import edu.gcc.packing.Knapsack;
import edu.gcc.packing.PackingAlgorithm;
import edu.gcc.results.Results;
import edu.gcc.simulation.Simulation;
import edu.gcc.util.Executor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import jfxtras.scene.control.LocalTimeTextField;

/**
 * This is the Main Run Configuration Modal, where the settings for the
 * simulation are configured.
 * 
 * @author Zack Orlaski
 *
 */
public class RunConfigurationModal extends Modal {

	/*
	 * XML DAO's
	 */
	private MealXmlDao mealXml = MealXml.getInstance();
	private DroneXmlDao droneXml = DroneXml.getInstance();
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private ObservableList<Meal> loadedMeals = mealXml.getAllLoadedObservable(
		true
	);
	private ObservableList<Drone> loadedDrones = droneXml
			.getObservableLoadedDrones(true);
	/*
	 * Simulation args
	 */
	private Campus selectedCampus;
	private MapLocation pickupLocation;
	private List<MapLocation> dropoffLocations;

	@FXML
	private LocalTimeTextField timeField;

	@FXML
	private Spinner<Integer> numHoursSpinner;

	@FXML
	private Label mealNumber;
	@FXML
	private Label droneNumber;

	/*
	 * Modals
	 */
	private LoadedMealsModal loadedMealsModalController;
	private LoadedDronesModal loadedDronesModalController;

	/**
	 * Event Handler for the edit meals button. Opens the Loaded Meals modal
	 */
	@FXML
	private void editLoadedMealsClicked() {
		loadedMealsModalController.setOnHideListener(
			() -> mealNumber.setText(Integer.toString(loadedMeals.size()))
		);
		loadedMealsModalController.show();
	}

	/**
	 * Event Handler for the edit drones button. Opens the loaded drones modal
	 */
	@FXML
	private void editLoadedDronesClicked() {
		loadedDronesModalController.setOnHideListener(
			() -> droneNumber.setText(Integer.toString(loadedDrones.size()))
		);
		loadedDronesModalController.show();
	}

	/**
	 * Event Handler for the cancel button. Hides the modal
	 */
	@FXML
	private void cancelButtonClicked() {
		hide();
	}

	/**
	 * Event Handler for the Run Simulation button. This method is where the
	 * simulations are created and ran. Each simulation type is run and sent
	 * asynchronously to the statistics page using CompletableFutures, 10 times
	 * each. After sending all Results, the statistics page is opened.
	 */
	@FXML
	private void runButtonClicked() {
		List<Meal> meals = loadedMeals.stream().collect(Collectors.toList());

		Statistics statsController = Gui.getInstance()
				.getControllerForScene("statistics", Statistics.class);

		// Run both sims 10 times
		for (int iteration = 0; iteration < 10; iteration++) {
			// Generate Order for both packing algorithms
			List<Order> orders = new ArrayList<>();
			OrderGenerator orderGen = new OrderGenerator(
					meals,
					dropoffLocations
			);
			orders.addAll(orderGen.getOrdersInInterval(10, 0, 3_600_000));
			// end Generate Orders

			final int index = iteration;
			
			// FIFO Simulation
			CompletableFuture<Results> futureFIFO = CompletableFuture
					.supplyAsync(
						() -> runSimulation(
							loadedDrones,
							orders,
							pickupLocation,
							new Knapsack(),
							1
						),
						Executor.getService()
					);

			statsController.addSimulationFuture(index, futureFIFO);
			
			// Knapsack Sim
			CompletableFuture<Results> knapsackFIFO = CompletableFuture
					.supplyAsync(
						() -> runSimulation(
							loadedDrones,
							orders,
							pickupLocation,
							new Fifo(),
							1
						),
						Executor.getService()
					);

			statsController.addSimulationFuture(index, knapsackFIFO);
		}

		Gui.getInstance().navigateTo("statistics");
	}

	public Results runSimulation(
			List<Drone> loadedDrones,
			List<Order> orders,
			MapLocation pickupLocation,
			PackingAlgorithm algorithm,
			int traveling
	) {
		return new Simulation(
				loadedDrones,
				orders,
				pickupLocation,
				algorithm,
				traveling
		).runSimulation();
	}

	/**
	 * Initializes the Modal and its values
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		mealNumber.setText(Integer.toString(loadedMeals.size()));
		droneNumber.setText(Integer.toString(loadedDrones.size()));
	}

	/**
	 * Passes the controllers necessary for the modal
	 * 
	 * @param loadedMealsModal  a loaded meals modal
	 * @param editMealModal     an edit meal info modal
	 * @param loadedDronesModal a loaded drones modal
	 * @param editDroneModal    an edit drone info modal
	 */
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

	/**
	 * Opens the RunConfigurationModal based on the selected campus
	 * 
	 * @param selectedCampus the campus selected in the dropdown menu on the
	 *                       overview
	 */
	public void show(final Campus selectedCampus) {
		this.selectedCampus = selectedCampus;
		this.pickupLocation = locationXml.getPickupLocationForCampus(
			selectedCampus
		);
		this.dropoffLocations = locationXml.getDropoffReactiveForCampus(
			selectedCampus
		);
		super.show();
	}

}
