package edu.gcc.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
	/**
	 * The distance the window has to be dragged when maximizes before it is
	 * restored down.
	 */
	private static final double RESTORE_THRESHOLD = 3.0;

	private double deltaX;
	private double deltaY;

	private boolean maximized;

	private Window window;

	/**
	 * Boolean property that determines whether custom window controls are
	 * displayed or not.
	 */
	private BooleanProperty windowControls = new SimpleBooleanProperty(true);

	/**
	 * Boolean property that determines whether the window is draggable or not.
	 */
	private BooleanProperty draggable = new SimpleBooleanProperty(true);

	@FXML
	private Menu backButton;
	@FXML
	private Menu helpMenu;
	@FXML
	private Menu fileMenu;
	@FXML
	private CheckMenuItem darkModeToggle;
	@FXML
	private SeparatorMenuItem fileMenuSeparator;

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

	/**
	 * Event handler for when the dark mode toggle button is pressed.
	 * 
	 * @param event
	 */
	@FXML
	private void toggleDarkMode(ActionEvent event) {
		CheckMenuItem item = (CheckMenuItem) event.getSource();
		Gui.getInstance().setDarkMode(item.isSelected());
	}

	/**
	 * Event handler for when the minimized button is pressed.
	 */
	@FXML
	private void onMinimizeButtonPressed() {
		Gui.getInstance().minimize();
	}

	/**
	 * Event handler for when the restore button is pressed. If currently
	 * maximized, the window will be restored down, if not maximized the window
	 * will be maximized.
	 */
	@FXML
	private void onRestoreButtonPressed() {
		if (maximized)
			restoreDown();
		else
			maximize();
	}

	/**
	 * Event handler for when the close button is pressed. This will terminate
	 * the JavaFX context and tell the JVM to exit with a code of 0.
	 */
	@FXML
	private void onCloseButtonPressed() {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * Event handler for when the menu bar is double clicked. This will cause
	 * the window to maximize or restore down depending on maximized state.
	 * 
	 * @param event
	 */
	@FXML
	private void onDoubleClick(MouseEvent event) {
		if (!(event.getButton().equals(MouseButton.PRIMARY) &&
				event.getClickCount() == 2)) {
			return;
		}

		if (maximized)
			restoreDown();
		else
			maximize();
	}

	/**
	 * Event handler for when the menu bar is pressed. This will set the offsets
	 * needed for dragging the window.
	 * 
	 * @param event
	 */
	@FXML
	private void onMousePressed(MouseEvent event) {
		if (!isDraggable())
			return;

		deltaX = event.getX();
		deltaY = event.getY();
	}

	/**
	 * Event handler for when the menu bar is dragged. This will minimize the
	 * window if it is currently maximized and move the window around the
	 * screen.
	 * 
	 * @param event
	 */
	@FXML
	private void onMouseDragged(MouseEvent event) {
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

	/**
	 * Initializes the window bar.
	 */
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

		Gui.getInstance()
				.getDarkModeProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (Boolean.TRUE.equals(newValue)) {
						toolbar.setBlendMode(BlendMode.ADD);
						darkModeToggle.setSelected(true);
					} else {
						toolbar.setBlendMode(BlendMode.MULTIPLY);
						darkModeToggle.setSelected(false);
					}
				});
	}

	/**
	 * When this method is called the back button is enabled and sets its event
	 * handler to the given handler.
	 * 
	 * @param event
	 */
	public void backButtonEnableAndAction(EventHandler<MouseEvent> event) {
		backButton.setVisible(true);
		backButton.getGraphic().setOnMouseReleased(event);
	}

	/**
	 * Sets the context file menu items.
	 * 
	 * @param menuItems The {@link MenuItem}s to add to the file menu.
	 */
	public void setFileMenuItems(MenuItem... menuItems) {
		fileMenuSeparator.setVisible(true);

		ObservableList<MenuItem> fileMenuItems = fileMenu.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			fileMenuItems.add(i, menuItems[i]);
		}
	}

	/**
	 * Sets the context help menu items.
	 * 
	 * @param menuItems The items to add to the help menu.
	 */
	public void setHelpMenuItems(MenuItem... menuItems) {
		helpMenu.getItems().addAll(menuItems);
	}

	/**
	 * Maximizes the window. Sets the internal state of the window bar and
	 * replaces the maximize icon to the restore down icon.
	 */
	public void maximize() {
		if (maximized)
			return;

		restoreIcon.setIconCode("E923");
		Gui.getInstance().maximize();
		maximized = true;
	}

	/**
	 * Restores down this window. Sets the internal state of the window bar and
	 * replaces the restore down icon with the maximize icon.
	 */
	public void restoreDown() {
		if (!maximized)
			return;

		restoreIcon.setIconCode("E922");
		Gui.getInstance().restoreDown();
		maximized = false;
	}

	/**
	 * Enables or disables window controls.
	 * 
	 * @param enabled
	 */
	public void setWindowControls(final boolean enabled) {
		windowControls.set(enabled);
	}

	/**
	 * Returns whether the window is draggable or not.
	 * 
	 * @return true if the window is draggable, otherwise false.
	 */
	public boolean isDraggable() {
		return draggable.get();
	}

	/**
	 * Sets whether the window is draggable or not.
	 * 
	 * @param draggable
	 */
	public void setDraggable(boolean draggable) {
		this.draggable.set(draggable);
	}

	/**
	 * Returns true if the current mouse position is outside of the given
	 * threshold relative to the current drag offsets.
	 * 
	 * @param threshold The threshold to compare against.
	 * @param event
	 */
	private boolean dragOutsideRestoreThreshold(
			double threshold,
			MouseEvent event
	) {
		return Math.abs(deltaX - event.getX()) > threshold ||
				Math.abs(deltaY - event.getY()) > threshold;
	}

	/**
	 * Aligns the window to the mouse based on the ratio of the screen size to
	 * the restored window size.
	 */
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
