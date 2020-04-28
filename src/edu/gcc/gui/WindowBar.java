package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class WindowBar implements Initializable {
	private static final double RESTORE_THRESHOLD = 3.0;

	private double deltaX;
	private double deltaY;

	private boolean maximized;

	private Window window;

	private BooleanProperty windowControls = new SimpleBooleanProperty(true);
	private BooleanProperty draggable = new SimpleBooleanProperty(true);

	@FXML
	private MenuBar windowControlMenu;

	@FXML
	private ToolBar toolbar;

	@FXML
	private Region spacer;

	@FXML
	private Label title;

	@FXML
	private Mdl2Icon restoreIcon;
	
	@FXML
	private CheckMenuItem darkModeToggle;

	@FXML
	protected void toggleDarkMode(ActionEvent event) {
		if (((CheckMenuItem) event.getSource()).isSelected()) {
			Config.get().putBoolean("dark_mode", true);
			Gui.getInstance().darkMode();
			toolbar.setBlendMode(BlendMode.ADD);
		} else {
			Config.get().putBoolean("dark_mode", false);
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
		if (maximized)
			restoreDown();
		else
			maximize();
	}

	@FXML
	protected void onCloseButtonPressed() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	protected void onDoubleClick(MouseEvent event) {
		if (!(event.getButton().equals(MouseButton.PRIMARY) &&
				event.getClickCount() == 2)) {
			return;
		}

		if (maximized)
			restoreDown();
		else
			maximize();
	}

	@FXML
	protected void onMousePressed(MouseEvent event) {
		if (!isDraggable())
			return;

		deltaX = event.getX();
		deltaY = event.getY();
	}

	/**
	 * @param event
	 */
	@FXML
	protected void onMouseDragged(MouseEvent event) {
		if (!isDraggable())
			return;

		if (maximized) {
			if (dragOutsideRestoreThreshold(RESTORE_THRESHOLD, event)) {
				restoreDown();
				alignWindowToMouse(event);
				return;
			} else {
				return;
			}
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

		windowControlMenu.visibleProperty().bind(windowControls);

		if (Gui.getInstance().getStage().getStyle() != StageStyle.UNDECORATED) {
			setWindowControls(false);
			setDraggable(false);
		}
		
		if(Config.get().getBoolean("dark_mode", false))
		{
			toolbar.setBlendMode(BlendMode.ADD);
			darkModeToggle.setSelected(true);
		}
	}

	public void maximize() {
		if (maximized)
			return;

		restoreIcon.setIconCode("E923");
		Gui.getInstance().maximize();
		maximized = true;
	}

	public void restoreDown() {
		if (!maximized)
			return;

		restoreIcon.setIconCode("E922");
		Gui.getInstance().restoreDown();
		maximized = false;
	}

	public void setWindowControls(final boolean enabled) {
		windowControls.set(enabled);
	}

	public boolean isDraggable() {
		return draggable.get();
	}

	public void setDraggable(boolean draggable) {
		this.draggable.set(draggable);
	}

	private boolean dragOutsideRestoreThreshold(
			double threshold,
			MouseEvent event
	) {
		return Math.abs(deltaX - event.getX()) > threshold ||
				Math.abs(deltaY - event.getY()) > threshold;
	}

	private void alignWindowToMouse(final MouseEvent event) {
		Stage primaryStage = Gui.getInstance().getStage();

		Rectangle2D bounds = Screen.getScreensForRectangle(
			primaryStage.getX(),
			primaryStage.getY(),
			primaryStage.getWidth(),
			primaryStage.getHeight()
		).get(0).getVisualBounds();

		double xRatio = primaryStage.getWidth() / bounds.getMaxX();
		double yRatio = primaryStage.getHeight() / bounds.getMaxY();

		window.setX(event.getScreenX() - (xRatio * event.getScreenX()));
		window.setY(event.getScreenY() - (yRatio * event.getScreenY()));

		deltaX = (xRatio * event.getScreenX());
		deltaY = (yRatio * event.getScreenY());
	}
}
