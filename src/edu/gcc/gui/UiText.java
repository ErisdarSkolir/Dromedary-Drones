package edu.gcc.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UiText {
	private static final String BUNDLE_NAME = "edu.gcc.gui.ui_text"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static final String CSS = "application.css";
	public static final String CANCEL_TEXT = getString("Cancel_Text");
	public static final String SUBMIT_TEXT = getString("Submit_Text");

	private UiText() {
		throw new UnsupportedOperationException("Cannot instantiate static utility class");
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
