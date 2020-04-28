package edu.gcc.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 * The main instance for the JavaFX window. Includes a number of utility methods
 * for interacting with the window.
 * 
 * @author Luke Donmoyer, Ethan Harvey
 */
public class Gui extends Application {
	/**
	 * A static singleton instance of this class.
	 */
	private static Gui instance;

	/**
	 * Constructor that initializes the singleton variable for this class.
	 */
	public Gui() {
		Gui.initInstance(this);
	}

	/**
	 * Sets the singleton variable to the given instance if it is null. If the
	 * singleton variable is already set this method does nothing.
	 * 
	 * @param inst The instance to set the singleton variable to.
	 */
	private static void initInstance(final Gui inst) {
		if (inst != null && Gui.instance == null)
			Gui.instance = inst;
	}

	/**
	 * Gets the singleton instance for the {@link Gui} class. This object will
	 * always be the same for the lifetime of the program.
	 * 
	 * @return The singleton instance of this class.
	 * @throws IllegalStateException if the singleton variable is not
	 *                               initialized.
	 */
	public static Gui getInstance() {
		if (instance != null)
			return instance;

		throw new IllegalStateException(
				String.format("%s is not initialized", Gui.class)
		);
	}

	private Stage primaryStage;

	private Map<String, Scene> scenes = new HashMap<>();
	private Map<String, Object> controllers = new HashMap<>();

	private JMetro jmetro = new JMetro(Style.LIGHT);

	/**
	 * A property that contains the current title of the JavaFX window.
	 */
	private StringProperty titleProperty = new SimpleStringProperty();

	/**
	 * Returns the JavaFX controller for the given id and automatically casts it
	 * to the correct class.
	 * 
	 * @param <T>   The type of the controller class that should be returned.
	 * @param id    The id of the scene to get the controller from.
	 * @param clazz The class of the controller to cast to.
	 * 
	 * @return The controller of given class type.
	 * 
	 * @throws IllegalArgumentException if there is no controller for the given
	 *                                  id.
	 */
	public <T> T getControllerForScene(final String id, final Class<T> clazz) {
		if (!controllers.containsKey(id))
			throw new IllegalArgumentException(
					String.format("No controller for scene with id %s", id)
			);

		return clazz.cast(controllers.get(id));
	}

	/**
	 * Switches the visible scene to the one denoted by the given id.
	 * 
	 * @param id The scene to switch to.
	 * 
	 * @throws IllegalArgumentException if there is no scene for the given id.
	 */
	public void navigateTo(final String id) {
		if (!scenes.containsKey(id))
			throw new IllegalArgumentException(
					String.format("No scene registered as %s", id)
			);

		// Ensures that the scene is stylized with JMetro and is visible.
		Platform.runLater(() -> {
			Scene scene = scenes.get(id);

			jmetro.setScene(scene);
			primaryStage.setScene(scene);
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		if (SystemUtils.IS_OS_WINDOWS)
			primaryStage.initStyle(StageStyle.UNDECORATED);

		setTitle("");

		addScene("overview", "overview.fxml");
		addScene("statistics", "statistics.fxml");

		if(Config.get().getBoolean("dark_mode", false))
			darkMode();
		
		navigateTo("overview");
		primaryStage.show();
	}

	/**
	 * Creates and adds the scene specified by the FXML resource to the list of
	 * scenes along with its associated controller.
	 * 
	 * @param id           The id the given scene should be registered by.
	 * @param fxmlResource The FXML file the scene should be based off of.
	 * 
	 * @throws RuntimeException         If there is a problem loading the FMXL
	 *                                  scene.
	 * @throws IllegalArgumentException If there is already a scene registered
	 *                                  with the given id.
	 */
	public void addScene(final String id, final String fxmlResource) {
		if (scenes.containsKey(id))
			throw new IllegalArgumentException(
					String.format(
						"Scene already registered under id: \"%s\"",
						id
					)
			);

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource(fxmlResource)
			);

			Scene scene = new Scene(loader.load());

			scenes.put(id, scene);
			controllers.put(id, loader.getController());

			jmetro.setScene(scene);
		} catch (IOException e) {
			throw new RuntimeException(
					String.format(
						"Could not create fxml scene %s from fxml file %s.",
						id,
						fxmlResource
					),
					e
			);
		}
	}

	/**
	 * Maximizes the JavaFX window.
	 */
	public void maximize() {
		primaryStage.setMaximized(true);

		Rectangle2D bounds = Screen.getScreensForRectangle(
			primaryStage.getX(),
			primaryStage.getY(),
			primaryStage.getWidth(),
			primaryStage.getHeight()
		).get(0).getVisualBounds();

		primaryStage.setX(bounds.getMinX());
		primaryStage.setY(bounds.getMinY());
		primaryStage.setWidth(bounds.getWidth());
		primaryStage.setHeight(bounds.getHeight());
	}

	/**
	 * Restores down the JavaFX window.
	 */
	public void restoreDown() {
		primaryStage.setMaximized(false);
	}

	/**
	 * Sets the JMetro scene to dark mode.
	 */
	public void darkMode() {
		jmetro.setStyle(Style.DARK);
		jmetro.reApplyTheme();
	}

	/**
	 * Sets the JMetro scene to light mode.
	 */
	public void lightMode() {
		jmetro.setStyle(Style.LIGHT);
		jmetro.reApplyTheme();
	}

	/**
	 * Minimizes the JavaFX window.
	 */
	public void minimize() {
		primaryStage.setIconified(true);
	}

	/**
	 * Returns the title property of the JavaFX window.
	 * 
	 * @return The title property of the window.
	 */
	public StringProperty getTitleProperty() {
		return this.titleProperty;
	}

	/**
	 * Sets the title of the JavaFX window. If the given argument is empty then
	 * the title will be set to "Dromedary Drones" otherwise it will be
	 * "Dromedary Drones: " plus the given argument.
	 */
	public void setTitle(String title) {
		if (title.isEmpty())
			titleProperty.set("Dromedary Drones");
		else
			titleProperty.set("Dromedary Drones: " + title);
	}

	/**
	 * Gets the stage of the JavaFX window.
	 * 
	 * @return The stage of the JavaFX window.
	 */
	public Stage getStage() {
		return primaryStage;
	}
}