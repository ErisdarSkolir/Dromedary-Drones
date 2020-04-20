package edu.gcc.gui;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Mdl2Icon extends Label {
	private static final Font mdlIconFont = Font.loadFont(Mdl2Icon.class.getResourceAsStream("SegMDL2.ttf"), 12);

	private final StringProperty iconCode = new SimpleStringProperty();

	public Mdl2Icon() {
		super();
		setFont(mdlIconFont);
		setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		getStyleClass().addAll("icon-font", "mdl2-assets");
		iconCode.addListener((obs, old, newValue) -> textProperty()
				.set(new String(DatatypeConverter.parseHexBinary(newValue), StandardCharsets.UTF_16)));
	}

	public final StringProperty iconCodeProperty() {
		return this.iconCode;
	}

	public final String getIconCode() {
		return this.iconCodeProperty().get();
	}

	public final void setIconCode(final String iconCode) {
		this.iconCodeProperty().set(iconCode);
	}
}
