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
import edu.gcc.util.HalfCoreExecutor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This is the Main Run Configuration Modal, where the settings for the
 * simulation are configured.
 * 
 * @author Luke Donmoyer, Zack Orlaski
 */
public class RunConfigurationModal extends Modal {
	private static final long MILLISECONDS_PER_HOUR = 3_600_000;

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

	// Simulation args
	private MapLocation pickupLocation;
	private List<MapLocation> dropoffLocations;

	@FXML
	private Label hourNumber;
	@FXML
	private Label mealNumber;
	@FXML
	private Label droneNumber;

	private List<Integer> ordersPerHour;

	// Modal controllers
	private LoadedMealsModal loadedMealsModalController;
	private LoadedDronesModal loadedDronesModalController;
	private EditHourModal editHoursModalController;

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
	 * Event Handler for the edit hours button. Opens the edit hours modal.
	 */
	@FXML
	private void editHoursButtonClicked() {
		editHoursModalController.setOnHideListener(() -> {
			ordersPerHour = editHoursModalController.getOrdersForAllHours();
			hourNumber.setText(Integer.toString(ordersPerHour.size()));
		});
		editHoursModalController.show();
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

		// Generate Order for both packing algorithms
		List<Order> orders = new ArrayList<>();
		OrderGenerator orderGen = new OrderGenerator(meals, dropoffLocations);

		for (int i = 0; i < ordersPerHour.size(); i++) {
			int numOrders = ordersPerHour.get(i);
			orders.addAll(
				orderGen.getOrdersInInterval(
					numOrders,
					i * MILLISECONDS_PER_HOUR,
					(i + 1) * MILLISECONDS_PER_HOUR
				)
			);
		}

		// FIFO Simulation
		CompletableFuture<Results> fifoGreedy = CompletableFuture.supplyAsync(
			() -> runSimulation(
				loadedDrones,
				orders,
				pickupLocation,
				new Fifo(),
				1
			),
			HalfCoreExecutor.getService()
		);

		statsController.addSimulationFuture(fifoGreedy);

		// Knapsack Sim
		CompletableFuture<Results> greedyBacktrack = CompletableFuture
				.supplyAsync(
					() -> runSimulation(
						loadedDrones,
						orders,
						pickupLocation,
						new Knapsack(),
						2
					),
					HalfCoreExecutor.getService()
				);

		statsController.addSimulationFuture(greedyBacktrack);

		hide();
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
			final EditDroneModal editDroneModal,
			final EditHourModal editHoursModal
	) {
		loadedMealsModalController = loadedMealsModal;
		loadedMealsModal.setEditMealModalController(editMealModal);

		loadedDronesModalController = loadedDronesModal;
		loadedDronesModalController.setEditDroneModalController(editDroneModal);

		editHoursModalController = editHoursModal;
		ordersPerHour = editHoursModalController.getOrdersForAllHours();
		hourNumber.setText(Integer.toString(ordersPerHour.size()));
	}

	/**
	 * Opens the RunConfigurationModal based on the selected campus
	 * 
	 * @param selectedCampus the campus selected in the dropdown menu on the
	 *                       overview
	 */
	public void show(final Campus selectedCampus) {
		this.pickupLocation = locationXml.getPickupLocationForCampus(
			selectedCampus
		);
		this.dropoffLocations = locationXml.getDropoffReactiveForCampus(
			selectedCampus
		);

		super.show();
	}
}
