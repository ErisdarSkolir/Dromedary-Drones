package edu.gcc.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Overview implements Initializable {
	private static final Logger logger = LoggerFactory.getLogger(
		Overview.class
	);

	// XML
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	private ObservableList<MapLocation> dropOffLocations = FXCollections
			.observableArrayList();
	private Map<Marker, MapLocation> currentMarkers = new HashMap<>();

	@FXML
	private ComboBox<Campus> campusDropdown;

	@FXML
	private AddDeliveryModal deliveryModalController;
	@FXML
	private CampusModal campusModalController;
	@FXML
	private MealConfigurationModal mealConfigurationModalController;
	@FXML
	private EditDeliveryModal editDeliveryModalController;

	@FXML
	private MapView mapView;

	@FXML
	private Button newCampusButton;

	@FXML
	protected void newCampusButtonClicked() {
		campusModalController.show();
	}

	@FXML
	protected void campusDropdownClicked() {
		Campus campus = campusDropdown.getValue();

		Gui.getInstance().setTitle(campus.getName());

		mapView.setCenter(
			mapLocationToCoordinate(
				locationXml.getPickupLocationForCampus(campus.getName())
			)
		);
		mapView.setZoom(17);

		dropOffLocations = locationXml.getDropoffReactiveForCampus(
			campus.getName()
		);
		setMapMarkers(dropOffLocations);
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
		mapView.initializedProperty().addListener(
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

	private void setMapMarkers(final ObservableList<MapLocation> locations) {
		// Remove any previous markers
		if (!currentMarkers.isEmpty()) {
			currentMarkers.keySet().stream().forEach(mapView::removeMarker);
			currentMarkers.clear();
		}

		// Add new markers
		for (MapLocation location : locations) {
			Marker marker = Marker.createProvided(Marker.Provided.RED)
					.setPosition(mapLocationToCoordinate(location)).setVisible(
						true
					);
			currentMarkers.put(marker, location);
			mapView.addMarker(marker);
		}

		// TODO: add location listener
	}

	private void markerClicked(final MarkerEvent event) {
		editDeliveryModalController.show(currentMarkers.get(event.getMarker()));
	}

	private void mapViewRightClicked(final MapViewEvent event) {
		event.consume();

		deliveryModalController.setMapLocation(
			coordinateToMapLocation(event.getCoordinate())
		);
		deliveryModalController.show(
			campusDropdown.getSelectionModel().getSelectedItem()
		);
	}

	private void addMarkers() {

	}

	private MapLocation coordinateToMapLocation(final Coordinate coordinate) {
		return new MapLocation(
				coordinate.getLatitude(),
				coordinate.getLongitude(),
				MapLocation.DROPOFF
		);
	}

	private Coordinate mapLocationToCoordinate(final MapLocation location) {
		return new Coordinate(location.getxCoord(), location.getyCoord());
	}
}
