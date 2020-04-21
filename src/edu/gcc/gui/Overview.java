package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

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
import edu.gcc.gui.modal.MealConfigurationModal;
import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class Overview implements Initializable {
	// XML
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	private ObservableList<MapLocation> dropOffLocations = FXCollections
			.observableArrayList();
	private BiMap<Marker, MapLocation> currentMarkers = HashBiMap.create();

	@FXML
	private ComboBox<Campus> campusDropdown;

	@FXML
	private AddDeliveryModal deliveryModalController;
	@FXML
	private EditDeliveryModal editDeliveryModalController;
	@FXML
	private AddCampusModal campusModalController;
	@FXML
	private EditCampusModal editCampusModalController;
	@FXML
	private MealConfigurationModal mealConfigurationModalController;

	@FXML
	private MapView mapView;

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

	@FXML
	protected void addDeliveryLocationClicked() {
		deliveryModalController.show(campusDropdown.getValue());
	}

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

		mealConfigurationModalController.show();
	}

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
	}

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
