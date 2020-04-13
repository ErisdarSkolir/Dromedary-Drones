package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Modal implements Initializable {
	private double deltaX;
	private double deltaY;

	@FXML
	private VBox container;

	public void show() {
		container.setLayoutX((container.getScene().getWidth() / 2) - (container.getWidth() / 2));
		container.setLayoutY((container.getScene().getHeight() / 2) - (container.getHeight() / 2));
		
		container.setVisible(true);
	}

	public void hide() {
		container.setVisible(false);
	}

	@FXML
	protected void onMenuPressed(MouseEvent event) {
		deltaX = container.getLayoutX() - event.getSceneX();
		deltaY = container.getLayoutY() - event.getSceneY();
	}

	@FXML
	protected void onMenuDrag(MouseEvent event) {
		container.setLayoutX(event.getSceneX() + deltaX);
		container.setLayoutY(event.getSceneY() + deltaY);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hide();
	}
}
