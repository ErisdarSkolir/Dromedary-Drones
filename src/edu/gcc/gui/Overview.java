package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Overview implements Initializable {
	private static final Logger logger = LoggerFactory.getLogger(Overview.class);

	// XML
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	private CampusXmlDao campusXml = CampusXml.getInstance();

	@FXML
	private ComboBox<Campus> campusDropdown;

	@FXML
	private DeliveryModal deliveryModalController;

	@FXML
	private CampusModal campusModalController;

	@FXML
	private MealConfigurationModal mealConfigurationModalController;

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

		mapView.setCenter(mapLocationToCoordinate(locationXml.getPickupLocationForCampus(campus.getName())));
		mapView.setZoom(17);
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
		 * // Simulation sim = new Simulation(meals, shopLocation, dropoffLocations, //
		 * packingAlgorithm, 1); // sim.runSimulation(); //
		 * statistics.setSimulation(sim); //
		 * Gui.getInstance().navigateTo(UiText.STATISTICS_ID); }).start();
		 */

		mealConfigurationModalController.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// newCampusButton.setGraphic(new MDL2IconFont("\uE710"));

		mapView.initialize(Configuration.builder().showZoomControls(false).build());

		mapView.initializedProperty().addListener((observalbe, newValue, oldValue) -> {
			mapView.setCenter(new Coordinate(0.0, 0.0));
			mapView.setZoom(0);
		});

		campusDropdown.setItems(campusXml.getAll());
	}

	public Coordinate mapLocationToCoordinate(final MapLocation location) {
		return new Coordinate(location.getxCoord(), location.getyCoord());
	}
}
