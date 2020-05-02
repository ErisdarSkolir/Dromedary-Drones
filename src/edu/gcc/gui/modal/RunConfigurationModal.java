package edu.gcc.gui.modal;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import jfxtras.scene.control.LocalTimeTextField;

public class RunConfigurationModal extends Modal {
	@FXML
	private LocalTimeTextField timeField;

	@FXML
	private Spinner<Integer> numHoursSpinner;

	@FXML
	private Label mealNumber;
	@FXML
	private Label droneNumber;

	private LoadedMealsModal loadedMealsModalController;

	@FXML
	private void editLoadedMealsClicked() {
		loadedMealsModalController.show();
	}

	@FXML
	private void editLoadedDronesClicked() {

	}

	@FXML
	private void cancelButtonClicked() {
		hide();
	}

	@FXML
	private void runButtonClicked() {

	}

	public void setLoadedMealsModalController(
			final LoadedMealsModal loadedMealsModal,
			final EditMealModal editMealModal
	) {
		loadedMealsModalController = loadedMealsModal;
		loadedMealsModal.setEditMealModalController(editMealModal);
	}
}
