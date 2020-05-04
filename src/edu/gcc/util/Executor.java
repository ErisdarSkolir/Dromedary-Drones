package edu.gcc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
	private static ExecutorService service = Executors.newCachedThreadPool();
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(Executor::shutdown));
	}
	
	private static void shutdown() {
		service.shutdownNow();
	}
	
	public static ExecutorService getService() {
		return service;
	}
}
