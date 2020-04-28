package edu.gcc.gui;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

public class Statistics implements Initializable{
	// TODO: add xml dao for run history stuff here

	private String data;
	
	@FXML
	private WindowBar windowBarController;

	// TODO: change this to whatever run history object we come up with
	@FXML
	private ListView<String> runHistory;

	@FXML
	private void onDeleteButtonClicked() {
		// TODO: delete currenlty selected run history object from xml here
	}
	
	@FXML
	private void onExportButtonClicked() {
		askForFile().ifPresent(this::saveCsvFile);
	}

	private void saveCsvFile(final File file) {
		// TODO: save to csv file here
	}
	
	public void message(String message) {
		data = message;
		System.out.println(message);
	}

	private Optional<File> askForFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

		File file = fileChooser.showSaveDialog(Gui.getInstance().getStage());

		return Optional.ofNullable(file);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
