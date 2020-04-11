package edu.gcc.gui;

import static edu.gcc.gui.UiText.CANCEL_TEXT;
import static edu.gcc.gui.UiText.CSS;
import static edu.gcc.gui.UiText.SUBMIT_TEXT;
import java.text.DecimalFormat;
import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXml;
import edu.gcc.meal.MealXmlDao;
import edu.gcc.packing.Fifo;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Overview extends GridPane {
	// XML
	private CampusXmlDao campusXml = CampusXml.getInstance();
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private MealXmlDao mealXml = MealXml.getInstance();

	// Meal form data
	private DecimalFormat probabilityDecimalFormat = new DecimalFormat("0.##");
	private ObservableList<Meal> mealList = mealXml.getAllObservable();

	private HBox campusMenu = new HBox(10);
	private HBox simulationMenu = new HBox(10);

	private CampusMap campusMap = new CampusMap();
	private CampusDropdown campusDropdown = new CampusDropdown(campusMap);

	private Stage modal = new Stage();

	public Overview() {
		setId(UiText.OVERVIEW_ID);

		modal.initModality(Modality.APPLICATION_MODAL);

		createCampusMenu();
		createSimulationMenu();

		campusMap.setMapLocationData(campusDropdown.getValue());

		add(campusMenu, 0, 0);
		add(campusMap.getElement(), 0, 1);
		add(simulationMenu, 0, 2);
	}

	public void createSimulationMenu() {

		simulationMenu.setId("simulation_menu");
		
		//Add DeliveryLocation Window
		AddDeliveryLocation addDeliveryLocation = new AddDeliveryLocation(campusDropdown, locationXml, modal);
		Scene add_delivery_scene = addDeliveryLocation.getScene();
		
		
		//Run Simulation options window
		RunSimulation runSimulationWindow = new RunSimulation(
				mealXml, locationXml, campusDropdown, mealList, probabilityDecimalFormat, modal);
		Scene run_simulation_scene = runSimulationWindow.getScene();
		
	

		// New delivery location Button
		Button newDeliveryButon = new Button("New Delivery Location");
		newDeliveryButon.setOnAction(event -> {
			// Open modal
			createAndShowModal("Add Delivery Location", add_delivery_scene);
		});

		// Run Simulation Button
		Button runButton = new Button("Run Simulation");
		runButton.setOnAction(event -> {
			createAndShowModal("Run Simulation", run_simulation_scene);
		});

		simulationMenu.getChildren().addAll(newDeliveryButon, runButton);

		add_delivery_scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
		run_simulation_scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
	}

	public void createCampusMenu() {
		campusMenu.setId("campus_menu");
		campusMenu.getChildren().add(campusDropdown.getElement());
		
		AddCampus addCampusModal = new AddCampus(
				campusMap, campusXml, campusDropdown, locationXml, modal);
		Scene add_campus_scene = addCampusModal.getScene();


		// New campus
		Button newCampusButton = new Button("New Campus");
		newCampusButton.setOnAction(event -> createAndShowModal("Add Campus", add_campus_scene));
		campusMenu.getChildren().add(newCampusButton);

		add_campus_scene.getStylesheets().add(getClass().getResource(CSS).toExternalForm());
	}

	private void createAndShowModal(final String title, final Scene scene) {
		modal.setTitle(title);
		modal.setScene(scene);
		modal.showAndWait();
	}
}
