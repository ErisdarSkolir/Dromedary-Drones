package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import edu.gcc.gui.Gui;
import edu.gcc.gui.Statistics;
import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.drone.Drone;
import edu.gcc.drone.DroneXml;
import edu.gcc.drone.DroneXmlDao;
import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import edu.gcc.order.Order;
import edu.gcc.order.OrderGenerator;
import edu.gcc.packing.Fifo;
import edu.gcc.packing.Knapsack;
import edu.gcc.results.Results;
import edu.gcc.simulation.Simulation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import jfxtras.scene.control.LocalTimeTextField;

public class RunConfigurationModal extends Modal {
	private MealXmlDao mealXml = MealXml.getInstance();
	private DroneXmlDao droneXml = DroneXml.getInstance();
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	
	
	
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
		
		
		List<String> customers = new ArrayList<>();
		List<Meal> meals = loadedMeals.stream().collect(Collectors.toList());
		customers.add("Bob");
		customers.add("John");
		customers.add("Jane");
		customers.add("That random guy over there");		
		
		Statistics statsController = 
				Gui.getInstance().getControllerForScene("statistics", Statistics.class); 
		
		
		//Run both sims 10 times
		for(int iteration = 0; iteration < 10; iteration++) {
			//  Generate Order for both packing algorithms
			List<Order> orders = new ArrayList<>();
			OrderGenerator orderGen = new OrderGenerator(meals, customers, dropoffLocations);
			orders.addAll(orderGen.getOrdersInInterval(10, 0, 3_600_000));
			//end Generate Orders
			
			final int index = iteration;
			
			//FIFO Sim
			CompletableFuture<Void> futureFIFO = CompletableFuture.supplyAsync(() -> 
			{
				Simulation simulation = new Simulation(meals, orders, pickupLocation, dropoffLocations, new Knapsack(), 1);
				return simulation.runSimulation();
				
			}).thenAccept(result-> statsController.sendToAllCharts(index, result));
			
			//Knapsack Sim
			CompletableFuture<Void> knapsackFIFO = CompletableFuture.supplyAsync(() -> 
			{
				Simulation simulation = new Simulation(meals, orders, pickupLocation, dropoffLocations, new Fifo(), 1);
				return simulation.runSimulation();
				
			}).thenAccept(result-> statsController.sendToAllCharts(index, result));
		}
		
		Gui.getInstance().navigateTo("statistics");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		mealNumber.setText(Integer.toString(loadedMeals.size()));
		droneNumber.setText(Integer.toString(loadedDrones.size()));
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
	
	
	public void show(final Campus selectedCampus) {
		this.selectedCampus = selectedCampus;	
		this.pickupLocation = locationXml.getPickupLocationForCampus(selectedCampus);
		this.dropoffLocations = locationXml.getDropoffReactiveForCampus(selectedCampus);
		super.show();
	}

}

