package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import edu.gcc.maplocation.MapLocationXml;
import edu.gcc.maplocation.MapLocationXmlDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import jfxtras.styles.jmetro.MDL2IconFont;

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
	private MapView mapView;
	
	@FXML
	private Button newCampusButton;
	
	@FXML
	protected void newCampusButtonClicked() {
		campusModalController.show();
	}

	@FXML
	protected void campusDropdownClicked() {

	}

	@FXML
	protected void addDeliveryLocationClicked() {
		deliveryModalController.show(campusDropdown.getValue());
	}

	@FXML
	protected void runSimulation() {
		System.out.println("run button clicked");

		new Thread(() -> {
			System.out.println("Simulation run");

			// Simulation sim = new Simulation(meals, shopLocation, dropoffLocations,
			// packingAlgorithm, 1);
			// sim.runSimulation();
			// statistics.setSimulation(sim);
			// Gui.getInstance().navigateTo(UiText.STATISTICS_ID);
		}).start();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//newCampusButton.setGraphic(new MDL2IconFont("\uE710"));
		
		mapView.initialize();
		
        mapView.initializedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				mapView.setCenter(new Coordinate(41.15514, -80.0786));
				mapView.setZoom(18);
			}
		});
		
		campusDropdown.setItems(campusXml.getAll());
	}
}
