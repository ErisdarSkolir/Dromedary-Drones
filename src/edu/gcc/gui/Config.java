package edu.gcc.gui;

import java.util.prefs.Preferences;

public class Config {
	private static Preferences preferences;
	
	static {
		preferences = Preferences.userRoot().node(Config.class.getName());
	}
	
	public static Preferences get() {
		return preferences;
	}
}
