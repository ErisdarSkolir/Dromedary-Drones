package edu.gcc.gui;



import static edu.gcc.gui.UiText.CANCEL_TEXT;
import static edu.gcc.gui.UiText.SUBMIT_TEXT;

import java.text.DecimalFormat;

import edu.gcc.maplocation.MapLocationXmlDao;
import edu.gcc.meal.Meal;
import edu.gcc.meal.MealXmlDao;
import edu.gcc.packing.Fifo;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RunSimulation {

	
	private GridPane run_simulation;
	private Scene run_simulation_scene;
	
	private HBox simulation_button_form;
	private HBox add_edit_form;
	private VBox combo_form;
	private VBox combo_labels;
	
	private Label title_label;
	private Label burgers_label;
	private Label fries_label;
	private Label drinks_label;
	private Label percent_label;
	private Label error_label;
	
	private TextField title_combo;
	private TextField burgers_combo;
	private TextField fries_combo;
	private TextField drinks_combo;
	private TextField percentage_combo;

	private Button add_edit_button;
	private Button delete_button;
	private Button run_cancel_button;
	private Button run_submit_button;
	
	private Meal addNewMeal;
	
	private ListView<Meal> combo_list;
	
	
	public RunSimulation(
			MealXmlDao mealXml, MapLocationXmlDao locationXml,
			CampusDropdown campusDropdown, ObservableList<Meal> mealList,
			DecimalFormat probabilityDecimalFormat, Stage modal) {
		
		/*
		 * Init GridPane and Scene
		 */
		run_simulation = new GridPane();
		run_simulation_scene = new Scene(run_simulation);
		run_simulation.setId("modal");
		
		/*
		 * Init VBoxes and HBoxes
		 */
		simulation_button_form = new HBox(10);
		add_edit_form = new HBox(10);
		combo_form = new VBox(10);
		combo_labels = new VBox(10);
		
		/*
		 * add VBoxes and HBoxes to Gridpane
		 */
		run_simulation.add(simulation_button_form, 1, 4);
		run_simulation.add(add_edit_form, 2, 2);
		run_simulation.add(combo_form, 2, 1);
		run_simulation.add(combo_labels, 1, 1);
		simulation_button_form.setId("form");
		add_edit_form.setId("form");
		combo_form.setId("form");
		combo_labels.setId("labels");
		
		/*
		 * Init Labels
		 */
		title_label = new Label("Title:");
		burgers_label = new Label("Burgers:");
		fries_label = new Label("Fries:");
		drinks_label = new Label("Drinks:");
		percent_label = new Label("Percent:");
		error_label = new Label("TOTAL GREATER THAN 100%");
		
		
		title_label.setId("label");
		burgers_label.setId("label");
		fries_label.setId("label");
		drinks_label.setId("label");
		percent_label.setId("label");
		error_label.setId("error");
		
		//error label is invisible
		error_label.setVisible(false);
		
		/*
		 * Add Labels to Gridpane
		 */
		combo_labels.getChildren().add(title_label);
		combo_labels.getChildren().add(burgers_label);
		combo_labels.getChildren().add(fries_label);
		combo_labels.getChildren().add(drinks_label);
		combo_labels.getChildren().add(percent_label);
		run_simulation.add(error_label, 2, 3);

	
		/*
		 * Init TextFields 
		 */
		title_combo = new TextField();
		title_combo.setMaxWidth(100);
		
		burgers_combo = new TextField();
		burgers_combo.setMaxWidth(30);
		
		fries_combo = new TextField();
		fries_combo.setMaxWidth(30);
		
		drinks_combo = new TextField();
		drinks_combo.setMaxWidth(30);
		
		percentage_combo = new TextField();
		percentage_combo.setMaxWidth(50);

		/*
		 * Add TextFields to GridPane
		 */
		
		combo_form.getChildren().add(title_combo);
		combo_form.getChildren().add(burgers_combo);
		combo_form.getChildren().add(fries_combo);
		combo_form.getChildren().add(drinks_combo);
		combo_form.getChildren().add(percentage_combo);
		
		
		
		/*
		 * ListView of combos
		 */
		combo_list = new ListView<>();
		combo_list.setItems(mealList);
		combo_list.setPrefWidth(100);
		combo_list.setPrefHeight(70);
		combo_list.setOnMouseClicked(event -> {
			if (!combo_list.getSelectionModel().getSelectedItem().equals(addNewMeal)) {
				error_label.setVisible(false);
				Meal currentMeal = combo_list.getSelectionModel().getSelectedItem();
				title_combo.setText(currentMeal.getName());
				// TODO: Get Burger, Fry, and Drink count
				percentage_combo.setText(
						String.format("%s%%", probabilityDecimalFormat.format(currentMeal.getProbability() * 100.0f)));
				delete_button.setDisable(false);
			} else {
				error_label.setVisible(false);
				title_combo.setText("");
				burgers_combo.setText("");
				fries_combo.setText("");
				drinks_combo.setText("");
				// Set leftover percentage
				double probability = 0;
				for(Meal temp : mealList) {
					probability += temp.getProbability();
				}
				percentage_combo.setText(String.format("%s%%", Double.toString(100.0f - (probability * 100.0f))));
				delete_button.setDisable(true);
			}
		});
		
		/*
		 * Add new Meal option
		 */
		addNewMeal = new Meal("ADD NEW", 0.0f);
		mealList.add(addNewMeal);
		
		/*
		 * Init Buttons, Set Actions, and add to GridPane
		 */
		
		
		// Init
		add_edit_button = new Button("Add/Edit");
		delete_button = new Button("Delete");
		run_cancel_button = new Button(CANCEL_TEXT); // $NON-NLS-1$
		run_submit_button = new Button(SUBMIT_TEXT);
		
		// Set Actions
		add_edit_button.setOnAction(event -> {
			// Check to make sure probabilty of meals is not over 100
			double probability = 0;
			for(Meal temp : mealList) {
				probability += temp.getProbability() * 100.0f;
			}
			probability += Double.parseDouble(percentage_combo.getText().replace("%", ""));
			if (probability > 100.0f) {
				error_label.setVisible(true);
			} else {
				error_label.setVisible(false);
				Meal meal = new Meal(
						title_combo.getText(),
						Float.parseFloat(
								percentage_combo.getText().replace("%", "")) / 100.0f); //$NON-NLS-2$
				mealXml.insert(meal);
				mealList.remove(addNewMeal); // Bit of a hack to keep the ADD NEW element at the bottom
				mealList.add(addNewMeal);
			}
		});
		
		run_cancel_button.setOnAction(event -> modal.close());
	
		delete_button.setOnAction(event -> 
			mealXml.delete(combo_list.getSelectionModel().getSelectedItem()));
		
		run_submit_button.setOnAction(event -> {
			modal.close();
			Gui.getInstance().runSimulation(mealXml.getAll(), locationXml.getPickupLocationForCampus(campusDropdown.getValue().getName()),
					locationXml.getDropoffReactiveForCampus(campusDropdown.getValue().getName()), new Fifo());
		});
		
		//Add to GridPane
		add_edit_form.getChildren().add(add_edit_button);
		add_edit_form.getChildren().add(delete_button);
		simulation_button_form.getChildren().add(run_cancel_button);
		run_simulation.add(combo_list, 0, 1);
		simulation_button_form.getChildren().add(run_submit_button);
	}
	
	
	
	public Scene getScene() {
		return run_simulation_scene;
	}

}
