package edu.gcc.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UiText {
	private static final String BUNDLE_NAME = "edu.gcc.gui.ui_text"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private UiText() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
