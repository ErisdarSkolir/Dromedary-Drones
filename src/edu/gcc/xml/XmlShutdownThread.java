package edu.gcc.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that registers and maintains a shutdown hook that will run all
 * runnables. This is meant to be used in conjunction with the {@link XmlFile}
 * class so they have a chance to write all of their changes if the JVM shuts down
 * down in the middle of a write command.
 */
class XmlShutdownThread {
	private static Thread thread = new Thread(XmlShutdownThread::shutdown);
	private static List<Runnable> runnables = new ArrayList<>();

	/**
	 * Static initializer to automatically register the shutdown hook.
	 */
	static {
		Runtime.getRuntime().addShutdownHook(thread);
	}

	private XmlShutdownThread() {
		throw new UnsupportedOperationException("Cannot instantiate static utility class");
	}

	/**
	 * Runs all registered runnables.
	 */
	private static void shutdown() {
		runnables.stream().forEach(Runnable::run);
	}

	/**
	 * Ensures the given runnable will be run when the shutdown hook is run.
	 * 
	 * @param runnable The runnable the be run on shutdown.
	 */
	public static void addShutdownEvent(final Runnable runnable) {
		runnables.add(runnable);
	}
}
