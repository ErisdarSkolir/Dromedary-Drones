package edu.gcc.gui.modal;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.drone.Drone;
import edu.gcc.drone.DroneXml;
import edu.gcc.drone.DroneXmlDao;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class LoadedDronesModal extends Modal {
	private DroneXmlDao droneXml = DroneXml.getInstance();

	@FXML
	private ListView<Drone> loadedDroneList;

	@FXML
	private ListView<Drone> unloadedDroneList;

	private EditDroneModal editDroneModalController;

	private Drone selectedDrone;

	@FXML
	private void loadDroneClicked() {
		updateSelectedLoaded(true);
	}

	@FXML
	private void unloadDroneClicked() {
		updateSelectedLoaded(false);
	}

	@FXML
	private void addDroneClicked() {
		editDroneModalController.show();
	}

	@FXML
	private void removeDroneClicked() {
		droneXml.delete(selectedDrone);
	}

	@FXML
	private void editDroneClicked() {
		if (selectedDrone == null)
			return;

		editDroneModalController.show(selectedDrone);
	}

	@FXML
	private void okButtonClicked() {
		hide();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		loadedDroneList.setItems(droneXml.getObservableLoadedDrones(true));
		unloadedDroneList.setItems(droneXml.getObservableLoadedDrones(false));

		ReadOnlyObjectProperty<Drone> loadedMealProperty = loadedDroneList
				.getSelectionModel()
				.selectedItemProperty();
		ReadOnlyObjectProperty<Drone> unloadedMealProperty = unloadedDroneList
				.getSelectionModel()
				.selectedItemProperty();

		loadedMealProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;

			selectedDrone = newValue;

			if (unloadedMealProperty.get() != null)
				unloadedDroneList.getSelectionModel().clearSelection();
		});
		unloadedMealProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;

			selectedDrone = newValue;

			if (loadedMealProperty.get() != null)
				loadedDroneList.getSelectionModel().clearSelection();
		});
	}

	private void updateSelectedLoaded(final boolean loaded) {
		if (selectedDrone == null || selectedDrone.isLoaded() == loaded)
			return;

		selectedDrone.setLoaded(loaded);
		droneXml.update(selectedDrone);
	}

	public void setEditDroneModalController(
			final EditDroneModal editDroneModalController
	) {
		this.editDroneModalController = editDroneModalController;
	}
}
