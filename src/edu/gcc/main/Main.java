package edu.gcc.main;

import edu.gcc.gui.Gui;
import javafx.application.Application;

public class Main {
	public static void main(String[] args) {
		//Thread thread = new Thread(() -> {
			Application.launch(Gui.class, args);
		//});
		//thread.start();
	}
}
