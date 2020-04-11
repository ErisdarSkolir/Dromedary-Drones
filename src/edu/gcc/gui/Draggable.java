package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Draggable implements Initializable {
	private double deltaX;
	private double deltaY;

	@FXML
	private VBox container;
	private Parent parent;

	@FXML
	protected void onMenuPressed(MouseEvent event) {
		if(parent == null)
			return;
		
		deltaX = parent.getLayoutX() - event.getSceneX();
		deltaY = parent.getLayoutY() - event.getSceneY();
	}

	@FXML
	protected void onMenuDrag(MouseEvent event) {
		if(parent == null)
			return;
		
		parent.setLayoutX(event.getSceneX() + deltaX);
		parent.setLayoutY(event.getSceneY() + deltaY);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		container.parentProperty().addListener((observalbe, oldValue, newValue) -> {
			parent = newValue;
			parent.setStyle("-fx-border-color:  #b2bec3");
		});
	}
}
