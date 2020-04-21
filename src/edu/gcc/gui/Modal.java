package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gcc.gui.modal.OnHideListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * This class is a FXML controller that represents a Modal Window. A modal
 * window must have a menu bar that link to onMenuPressed and onMenuDrag and the
 * main container of the window must have a fxid of "container". This will make
 * the window draggable and also have the hide and show methods. Modals are
 * invisible by default.
 * 
 * @author Luke Donmoyer
 */
public class Modal implements Initializable {
	// Offsets for dragging the modal
	private double deltaX;
	private double deltaY;

	private OnHideListener listener;

	@FXML
	private VBox container;

	/**
	 * Makes this modal visible and resets its position to be in the middle of
	 * the current window.
	 */
	public void show() {
		container.setLayoutX(
			(container.getScene().getWidth() - container.getWidth()) / 2
		);
		container.setLayoutY(
			(container.getScene().getHeight() - container.getHeight()) / 2
		);

		container.setVisible(true);
	}

	/**
	 * Makes this modal invisible and calls the on hide listener.
	 */
	public void hide() {
		container.setVisible(false);
		notifyListeners();
	}

	/**
	 * Sets the deltaX and deltaY offsets for dragging the modal when its menu
	 * is pressed.
	 * 
	 * @param event The mouse event to get coordinates from.
	 */
	@FXML
	protected void onMenuPressed(MouseEvent event) {
		deltaX = container.getLayoutX() - event.getSceneX();
		deltaY = container.getLayoutY() - event.getSceneY();
	}

	/**
	 * Sets the layout coordinates of this modal when the menu is dragged.
	 * DeltaX and deltaY must be set before hand for this to put the modal in
	 * the correct position.
	 * 
	 * @param event The mouse event to get coordinates from.
	 */
	@FXML
	protected void onMenuDrag(MouseEvent event) {
		container.setLayoutX(event.getSceneX() + deltaX);
		container.setLayoutY(event.getSceneY() + deltaY);
	}

	/**
	 * Hides this modal when it is initialized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hide();
	}

	/**
	 * Sets the on hide listener.
	 * 
	 * @param listener
	 */
	public void setOnHideListener(final OnHideListener listener) {
		this.listener = listener;
	}

	/**
	 * Removes the current on hide listener.
	 */
	public void removeOnHideListeners() {
		this.listener = null;
	}

	/**
	 * Calls the on hide listener.
	 */
	private void notifyListeners() {
		if (listener != null)
			listener.onHide();
	}
}
