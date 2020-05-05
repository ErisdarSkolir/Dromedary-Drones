package edu.gcc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class contains an instance of an Executor that uses a fixed pool with
 * half of the computers available cores.
 * 
 * @author Luke Donmoyer
 */
public class HalfCoreExecutor {
	private static ExecutorService service;

	/**
	 * Initializes the thread pool and sets a shutdown hook that will shutdown
	 * the thread pool.
	 */
	static {
		service = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() / 2
		);
		Runtime.getRuntime()
				.addShutdownHook(new Thread(HalfCoreExecutor::shutdown));
	}

	private HalfCoreExecutor() {
		throw new IllegalStateException(
				"Cannot instantiate static utility class"
		);
	}

	/**
	 * Shutdown hook to close the thread pool.
	 */
	private static void shutdown() {
		service.shutdownNow();
	}

	/**
	 * Returns the Executor instance.
	 * 
	 * @return The fixed thread pool executor instance.
	 */
	public static ExecutorService getService() {
		return service;
	}
}
