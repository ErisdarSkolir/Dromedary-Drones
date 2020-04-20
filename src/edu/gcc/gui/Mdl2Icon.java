package edu.gcc.gui;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * JavaFX component that represents an MDL2 Icon Font Character. It is a label
 * that has an extra property for the hexadecimal Unicode number without the
 * preceding "\\u".
 * 
 * @author Luke Donmoyer
 */
public class Mdl2Icon extends Label {
	/**
	 * Loads the packaged MDL2 Icon Font and keeps a static reference.
	 */
	private static final Font MDL2_ICON_FONT = Font.loadFont(Mdl2Icon.class.getResourceAsStream("SegMDL2.ttf"), 12);

	/**
	 * Property representing the hexadecimal Unicode number to display from the MDL2
	 * Icon Font.
	 */
	private final StringProperty iconCode = new SimpleStringProperty();

	/**
	 * Default constructor. Creates a listener that sets the label's text to the
	 * Unicode character specified by iconCode whenever it changes.
	 */
	public Mdl2Icon() {
		super();
		setFont(MDL2_ICON_FONT);
		getStyleClass().addAll("icon-font", "mdl2-assets");
		iconCode.addListener((obs, old, newValue) -> textProperty()
				.set(new String(DatatypeConverter.parseHexBinary(newValue), StandardCharsets.UTF_16)));
	}

	/**
	 * Gets the icon code property.
	 * 
	 * @return A string property of the current icon code.
	 */
	public final StringProperty iconCodeProperty() {
		return this.iconCode;
	}

	/**
	 * Get the icon code as a string.
	 * 
	 * @return A string of the current icon code.
	 */
	public final String getIconCode() {
		return this.iconCodeProperty().get();
	}

	/**
	 * Set the icon code to the given string.
	 * 
	 * @param iconCode The icon code to set.
	 */
	public final void setIconCode(final String iconCode) {
		this.iconCodeProperty().set(iconCode);
	}
}
