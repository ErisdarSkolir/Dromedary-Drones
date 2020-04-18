package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Window;

public class WindowBar implements Initializable {
	private Window window;

	private double deltaX;
	private double deltaY;

	private boolean draggable = true;

	@FXML
	private ToolBar toolbar;

	@FXML
	private Region spacer;

	@FXML
	private Label title;
	
	@FXML
	private Mdl2Icon restoreIcon;

	@FXML
	protected void toggleDarkMode(ActionEvent event) {
		if (((CheckMenuItem) event.getSource()).isSelected()) {
			Gui.getInstance().darkMode();
			toolbar.setBlendMode(BlendMode.ADD);
		} else {
			Gui.getInstance().lightMode();
			toolbar.setBlendMode(BlendMode.MULTIPLY);
		}
	}

	@FXML
	protected void onMinimizeButtonPressed() {
		Gui.getInstance().minimize();
	}

	@FXML
	protected void onRestoreButtonPressed() {
		if (draggable) {
			restoreIcon.setIconCode("E923");
			Gui.getInstance().maximize();
			draggable = false;
		} else {
			restoreIcon.setIconCode("E922");
			Gui.getInstance().restoreDown();
			draggable = true;
		}
	}

	@FXML
	protected void onCloseButtonPressed() {
		Platform.exit();
	}

	@FXML
	protected void onDoubleClick(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			if (draggable) {
				restoreIcon.setIconCode("E923");
				Gui.getInstance().maximize();
				draggable = false;
			} else {
				restoreIcon.setIconCode("E922");
				Gui.getInstance().restoreDown();
				draggable = true;
			}
		}
	}

	@FXML
	protected void onMousePressed(MouseEvent event) {
		deltaX = event.getX();
		deltaY = event.getY();
	}

	@FXML
	protected void onMouseDragged(MouseEvent event) {
		if (!draggable) {
			restoreIcon.setIconCode("E922");
			Gui.getInstance().restoreDown();
			draggable = true;
		}

		window.setX(event.getScreenX() - deltaX);
		window.setY(event.getScreenY() - deltaY);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		HBox.setHgrow(spacer, Priority.ALWAYS);
		title.textProperty().bind(Gui.getInstance().getTitleProperty());

		spacer.sceneProperty().addListener((obs, old, newValue) -> {
			if (newValue != null)
				window = newValue.getWindow();
		});
	}
}
