package edu.gcc.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;

import edu.gcc.gui.modal.AddCampusModal;
import edu.gcc.gui.modal.AddDeliveryModal;
import edu.gcc.gui.modal.EditCampusModal;
import edu.gcc.gui.modal.EditDeliveryModal;
import edu.gcc.gui.modal.LoadedMealsModal;
import edu.gcc.gui.modal.EditMealModal;
import edu.gcc.gui.modal.RunConfigurationModal;
import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.results.Results;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;

/**
 * Represents the main map/overview screen for the Dromedary Drones application.
 * 
 * @author Luke Donmoyer, Ethan Harvey
 */
public class Overview implements Initializable {
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	private ObservableList<MapLocation> dropOffLocations = FXCollections
			.observableArrayList();
	private BiMap<Marker, MapLocation> currentMarkers = HashBiMap.create();

	@FXML
	private ComboBox<Campus> campusDropdown;

	@FXML
	private WindowBar windowBarController;

	@FXML
	private RunConfigurationModal runConfigurationModalController;
	@FXML
	private AddDeliveryModal deliveryModalController;
	@FXML
	private EditDeliveryModal editDeliveryModalController;
	@FXML
	private AddCampusModal campusModalController;
	@FXML
	private EditCampusModal editCampusModalController;
	@FXML
	private EditMealModal editMealModalController;
	@FXML
	private LoadedMealsModal mealConfigurationModalController;

	@FXML
	private MapView mapView;

	/**
	 * Event handler for when the edit campus button is clicked. This opens the
	 * modal and then ensures that the edited campus is selected when the modal
	 * closes. If no campus is currenlty selected, this method returns
	 * immediately.
	 */
	@FXML
	protected void editCampusButtonClicked() {
		Campus selectedCampus = campusDropdown.getSelectionModel()
				.getSelectedItem();

		if (selectedCampus == null)
			return;

		long selectedId = selectedCampus.getId();

		editCampusModalController.show(selectedCampus);
		editCampusModalController.setOnHideListener(
			() -> campusDropdown.setValue(campusXml.getCampusForId(selectedId))
		);
	}

	/**
	 * Event handler for when the new campus button is clicked. Opens the create
	 * campus modal and makes the newly create campus the selected one when the
	 * modal closes.
	 */
	@FXML
	protected void newCampusButtonClicked() {
		campusModalController.show();
		campusModalController.setOnHideListener(
			() -> campusDropdown.setValue(
				campusDropdown.getItems()
						.get(campusDropdown.getItems().size() - 1)
			)
		);
	}

	/**
	 * Event handler for when the campus dropdown is clicked. This loads the
	 * selected campus, re centers the map, and sets the map locations. If the
	 * selected campus ends up being null this method returns immediately.
	 */
	@FXML
	protected void campusDropdownClicked() {
		Campus campus = campusDropdown.getValue();

		if (campus == null)
			return;

		Gui.getInstance().setTitle(campus.getName());

		mapView.setCenter(
			locationXml.getPickupLocationForCampus(campus).toCoordinate()
		);
		mapView.setZoom(17);

		dropOffLocations = locationXml.getDropoffReactiveForCampus(campus);
		setMapMarkers(
			locationXml.getPickupLocationForCampus(campus),
			dropOffLocations
		);
	}

	/**
	 * Event handler for when the add delivery location is clicked. Opens the
	 * modal for adding a new location.
	 */
	@FXML
	protected void addDeliveryLocationClicked() {
		deliveryModalController.show(campusDropdown.getValue());
	}

	/**
	 * Event handler for when the run simulation button is called. This opens
	 * the run configuration modal.
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	protected void runSimulation() {
		/*
		 * System.out.println("run button clicked");
		 * 
		 * new Thread(() -> { System.out.println("Simulation run");
		 * 
		 * // Simulation sim = new Simulation(meals, shopLocation,
		 * dropoffLocations, // packingAlgorithm, 1); // sim.runSimulation(); //
		 * statistics.setSimulation(sim); //
		 * Gui.getInstance().navigateTo(UiText.STATISTICS_ID); }).start();
		 */

		// mealConfigurationModalController.show();

		if (campusDropdown.getSelectionModel().getSelectedItem() == null)
			return;

		/*
		 * TEST COMPLETABLE FUTURE: DELETE BEFORE SUBMISSION
		 */
		/*
		 * ArrayList<Long> timePerOrder = new ArrayList<>(); ArrayList<Integer>
		 * ordersPerTrip = new ArrayList<>(); ArrayList<Long> distancePerTrip =
		 * new ArrayList<>(); String simType = "test";
		 * 
		 * timePerOrder.add((long) 6000); timePerOrder.add((long) 5500);
		 * timePerOrder.add((long) 9000); timePerOrder.add((long) 7242);
		 * ordersPerTrip.add(4); ordersPerTrip.add(2); ordersPerTrip.add(7);
		 * ordersPerTrip.add(1); distancePerTrip.add((long) 450);
		 * distancePerTrip.add((long) 520); distancePerTrip.add((long) 190);
		 * distancePerTrip.add((long) 487); Results r = new Results(
		 * timePerOrder, ordersPerTrip, distancePerTrip, simType );
		 * CompletableFuture<Results> f = CompletableFuture.completedFuture(r);
		 * 
		 * ArrayList<Long> timePerOrder2 = new ArrayList<>(); ArrayList<Integer>
		 * ordersPerTrip2 = new ArrayList<>(); ArrayList<Long> distancePerTrip2
		 * = new ArrayList<>(); String simType2 = "test";
		 * 
		 * timePerOrder2.add((long) 6600); timePerOrder2.add((long) 5400);
		 * timePerOrder2.add((long) 7400); timePerOrder2.add((long) 7212);
		 * ordersPerTrip2.add(1); ordersPerTrip2.add(3); ordersPerTrip2.add(4);
		 * ordersPerTrip2.add(2); distancePerTrip2.add((long) 470);
		 * distancePerTrip2.add((long) 502); distancePerTrip2.add((long) 100);
		 * distancePerTrip2.add((long) 420); Results r2 = new Results(
		 * timePerOrder2, ordersPerTrip2, distancePerTrip2, simType2 );
		 * CompletableFuture<Results> f2 =
		 * CompletableFuture.completedFuture(r2);
		 * 
		 * /* END TEST OBJECT
		 */

		/*
		 * Statistics statsController = Gui.getInstance()
		 * .getControllerForScene("statistics", Statistics.class); //
		 * statsController.message("Hello World");
		 * 
		 * Gui.getInstance().navigateTo("statistics");
		 * 
		 * statsController.sendToAllCharts(f);// TEST
		 * statsController.sendToAllCharts(f2);// TEST
		 */

		runConfigurationModalController.show();
	}

	/**
	 * The initialize method creates the map view and sets the campus dropdown
	 * items.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mapView.initialize(
			Configuration.builder().showZoomControls(false).build()
		);
		mapView.initializedProperty()
				.addListener(
					(observalbe, newValue, oldValue) -> mapPostInitialization()
				);

		campusDropdown.setItems(campusXml.getAll());

		MenuItem addCampusMenuItem = new MenuItem("Add Campus");
		addCampusMenuItem.setOnAction(event -> this.newCampusButtonClicked());

		MenuItem runSimulationMenuItem = new MenuItem("Run Simulation");
		runSimulationMenuItem.setOnAction(event -> this.runSimulation());

		windowBarController.setFileMenuItems(
			addCampusMenuItem,
			runSimulationMenuItem
		);

		runConfigurationModalController.setLoadedMealsModalController(
			mealConfigurationModalController,
			editMealModalController
		);
	}

	/**
	 * This method is called after the map is initialized. Sets up map event
	 * handlers and some configuration options.
	 */
	private void mapPostInitialization() {
		mapView.setCenter(new Coordinate(0.0, 0.0));
		mapView.setZoom(0);

		mapView.addEventHandler(
			MapViewEvent.MAP_RIGHTCLICKED,
			this::mapViewRightClicked
		);
		mapView.addEventHandler(
			MarkerEvent.MARKER_CLICKED,
			this::markerClicked
		);
	}

	/**
	 * Removes any previous map markers and creates new ones for the given
	 * pickup and dropoff locations.
	 * 
	 * @param pickupLocation    The pickup location to add to the map.
	 * @param deliveryLocations A list of dropoff locations to add the map.
	 */
	private void setMapMarkers(
			final MapLocation pickupLocation,
			final ObservableList<MapLocation> deliveryLocations
	) {
		// Remove any previous markers
		if (!currentMarkers.isEmpty()) {
			currentMarkers.keySet().stream().forEach(mapView::removeMarker);
			currentMarkers.clear();
		}

		// Add pickup location marker
		Marker pickupMarker = Marker.createProvided(Marker.Provided.BLUE)
				.setPosition(pickupLocation.toCoordinate())
				.setVisible(true);
		mapView.addMarker(pickupMarker);
		currentMarkers.put(pickupMarker, pickupLocation);

		// Add dropoff location markers
		for (MapLocation location : deliveryLocations) {
			Marker dropoffMarker = Marker.createProvided(Marker.Provided.RED)
					.setPosition(location.toCoordinate())
					.setVisible(true)
					.attachLabel(new MapLabel(location.getName()));
			currentMarkers.put(dropoffMarker, location);
			mapView.addMarker(dropoffMarker);
		}

		// Set listener on location list
		deliveryLocations.addListener(this::locationChangeListener);
	}

	/**
	 * Event handler for when the dropoff locations observable list changes.
	 * This will remove any unnecessary locations and add any new ones.
	 * 
	 * @param change
	 */
	private void locationChangeListener(Change<? extends MapLocation> change) {
		while (change.next()) {
			if (change.wasAdded()) {
				// Add markers for added locations
				for (MapLocation location : change.getAddedSubList()) {
					Marker marker = Marker.createProvided(Marker.Provided.RED)
							.setPosition(location.toCoordinate())
							.setVisible(true)
							.attachLabel(new MapLabel(location.getName()));
					currentMarkers.put(marker, location);
					mapView.addMarker(marker);
				}
			} else if (change.wasRemoved()) {
				// Remove markers for deleted locations
				for (MapLocation location : change.getRemoved()) {
					Marker marker = currentMarkers.inverse().remove(location);
					mapView.removeMarker(marker);
				}
			}
		}
	}

	/**
	 * Event hander for when a marker is clicked. This opens the location editor
	 * modal and auto fills with that locations information.
	 * 
	 * @param event The input event.
	 */
	private void markerClicked(final MarkerEvent event) {
		editDeliveryModalController.show(currentMarkers.get(event.getMarker()));
	}

	/**
	 * Event handler for when the map is right clicked. This opens the add
	 * location modal with the latitude and longitude auto-filled with the click
	 * location from the map. If no campus is currently selected this method
	 * returns immediately.
	 * 
	 * @param event The map event.
	 */
	private void mapViewRightClicked(final MapViewEvent event) {
		Campus selectedCampus = campusDropdown.getSelectionModel()
				.getSelectedItem();

		if (selectedCampus == null)
			return;

		deliveryModalController.show(
			campusDropdown.getSelectionModel().getSelectedItem(),
			event.getCoordinate().getLatitude(),
			event.getCoordinate().getLongitude()
		);
	}
}
