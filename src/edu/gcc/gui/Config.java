package edu.gcc.gui;

import java.util.prefs.Preferences;

/**
 * Creates a static preferences object to be used in the program.
 * 
 * @author Luke Donmoyer
 */
public class Config {
	private static Preferences preferences;

	/*
	 * Initializes a preferences node with the Config class' name
	 */
	static {
		preferences = Preferences.userRoot().node(Config.class.getName());
	}

	private Config() {
		throw new UnsupportedOperationException(
				"Cannot create instance of static utility class"
		);
	}

	/**
	 * Returns the static preferences object.
	 * 
	 * @return The static preference object.
	 */
	public static Preferences get() {
		return preferences;
	}
}
