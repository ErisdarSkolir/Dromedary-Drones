package edu.gcc.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gcc.maplocation.MapLocation;
import edu.gcc.meal.Meal;
import edu.gcc.packing.PackingAlgorithm;
import edu.gcc.simulation.Simulation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Gui extends Application {
	private static Gui instance;

	public Gui() {
		Gui.initInstance(this);
	}

	private static void initInstance(final Gui inst) {
		if (inst != null && Gui.instance == null)
			Gui.instance = inst;
	}

	public static Gui getInstance() {
		if (instance != null)
			return instance;

		throw new IllegalStateException(String.format("%s is not initialized", Gui.class));
	}

	private Stage primaryState;

	private Map<String, Scene> scenes = new HashMap<>();

	public void navigateTo(final String id) {
		if (!scenes.containsKey(id))
			throw new IllegalArgumentException(String.format("No scene registered as %s", id));

		Platform.runLater(() -> primaryState.setScene(scenes.get(id)));
	}

	public void runSimulation(List<Meal> meals, MapLocation shopLocation, List<MapLocation> dropoffLocations, PackingAlgorithm packingAlgorithm) {
		Thread thread = new Thread(() -> {
			Simulation sim = new Simulation(meals, shopLocation, dropoffLocations, packingAlgorithm, 1);
			sim.runSimulation();
			//statistics.setSimulation(sim);
			navigateTo(UiText.STATISTICS_ID);
		});
		thread.start();
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		this.primaryState = primaryStage;

		FXMLLoader loader = new FXMLLoader();
		
		Parent overview = loader.load(getClass().getResource("overview.fxml"));
		overview.setVisible(true);
		
		Scene scene = new Scene(overview);
		scenes.put("overview", scene);
		
		JMetro jmetro = new JMetro(Style.LIGHT);
		jmetro.setScene(scenes.get("overview"));

		primaryStage.setScene(scenes.get(UiText.OVERVIEW_ID));
		primaryStage.show();
	}
}