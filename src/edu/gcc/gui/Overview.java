package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class Overview implements Initializable{
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();
	
	@FXML
	private Button newCampusButton;
	
	@FXML
	private ComboBox<Campus> campusDropdown;
	
	@FXML
	private ScatterChart<Number, Number> campusMap;
	// Map data
	private ObservableList<XYChart.Data<Number, Number>> mapDropoffLocations;
	private ObservableList<XYChart.Data<Number, Number>> mapPickupLocations;
	
	@FXML
	protected void newCampusButtonClicked() {
		System.out.println("New Campus button clicked");
	}
	
	@FXML
	protected void campusDropdownClicked() { 
		System.out.println("Dropdown clicked");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Pickup Points
		Series<Number, Number> pickupLocationSeries = new Series<>();
		pickupLocationSeries.setName("Shop Location");
		mapPickupLocations = pickupLocationSeries.getData();

		// Dropoff Points
		Series<Number, Number> dropoffLocationSeries = new Series<>();
		dropoffLocationSeries.setName("Delivery Points");
		mapDropoffLocations = dropoffLocationSeries.getData();

		campusMap.getData().add(pickupLocationSeries);
		campusMap.getData().add(dropoffLocationSeries);
		
		setMapLocationData(new Campus("Test"));
	}
	
	public void setMapLocationData(final Campus campus) {
		if (campus == null)
			return;

		mapDropoffLocations.clear();
		mapPickupLocations.clear();

		// Add dropoff locations to chart and set a change listener to update it if data
		// changes
		ObservableList<MapLocation> dropoffLocations = locationXml.getDropoffReactiveForCampus(campus.getName());
		dropoffLocations.addListener(createMapChangeListener(mapDropoffLocations));
		dropoffLocations.stream().map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
				.forEach(mapDropoffLocations::add);

		// Add pickup locations to chart and set a change listener to update it if data
		// changes
		ObservableList<MapLocation> pickupLocations = locationXml.getPickupReactiveForCampus(campus.getName());
		pickupLocations.addListener(createMapChangeListener(mapPickupLocations));
		pickupLocations.stream().map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
				.forEach(mapPickupLocations::add);
	}

	private ListChangeListener<MapLocation> createMapChangeListener(
			final ObservableList<XYChart.Data<Number, Number>> list) {
		return c -> {
			while (c.next()) {
				c.getRemoved().stream().map(location -> new Data<>(location.getxCoord(), location.getyCoord()))
						.forEach(list::remove);

				c.getAddedSubList().stream()
						.map(location -> new Data<Number, Number>(location.getxCoord(), location.getyCoord()))
						.forEach(list::add);
			}
		};
	}
}
