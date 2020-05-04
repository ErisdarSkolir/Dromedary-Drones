package edu.gcc.gui.modal;

import javafx.fxml.FXML;

/**
 * FXML controller class for the about modal.
 * 
 * @author Luke Donmoyer
 */
public class AboutModal extends Modal {

	/**
	 * Event handler for when the close button is clicked. Will hide this modal.
	 */
	@FXML
	private void onCloseButtonClicked() {
		hide();
	}
}
