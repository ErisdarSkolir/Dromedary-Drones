package edu.gcc.gui;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.MapLocation;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class CampusMap {
	// XML
	private MapLocationXmlDao locationXml = MapLocationXml.getInstance();

	// Map element
	private ScatterChart<Number, Number> element;

	// Map data
	private ObservableList<XYChart.Data<Number, Number>> mapDropoffLocations;
	private ObservableList<XYChart.Data<Number, Number>> mapPickupLocations;

	public CampusMap() {
		this.element = createMap();
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

	// Creates a map
	private ScatterChart<Number, Number> createMap() {
		// Map
		ScatterChart<Number, Number> result = new ScatterChart<>(new NumberAxis(), new NumberAxis());
		result.getXAxis().setTickLabelsVisible(false);
		result.getYAxis().setTickLabelsVisible(false);
		result.setTitle("Campus Map");
		result.setMaxWidth(500);
		result.setMaxHeight(400);

		// Pickup Points
		Series<Number, Number> pickupLocationSeries = new Series<>();
		pickupLocationSeries.setName("Shop Location");
		mapPickupLocations = pickupLocationSeries.getData();

		// Dropoff Points
		Series<Number, Number> dropoffLocationSeries = new Series<>();
		dropoffLocationSeries.setName("Delivery Points");
		mapDropoffLocations = dropoffLocationSeries.getData();

		result.getData().add(pickupLocationSeries);
		result.getData().add(dropoffLocationSeries);

		return result;
	}

	public ScatterChart<Number, Number> getElement() {
		return element;
	}
}
