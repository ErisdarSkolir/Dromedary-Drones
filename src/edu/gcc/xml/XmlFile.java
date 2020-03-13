package edu.gcc.xml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.ximpleware.AutoPilot;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import edu.gcc.xml.exception.XmlWriteException;

/**
 * @author Luke Donmoyer
 * 
 *         This class represents an actual XML file on disk and handles all
 *         reading from and writing to said file. It uses futures to handle
 *         writing and re-parsing asynchronously but will block if a read is
 *         attempted while still writing.
 *
 */
public class XmlFile {
	private static final String HEADER = "<?xml version=\"1.0\"?>\r\n";
	
	private CompletableFuture<Void> writeFuture;

	private String filepath;

	private VTDGen gen = new VTDGen();
	private XMLModifier xmlModifier = new XMLModifier();
	private AutoPilot autopilot = new AutoPilot();
	private VTDNav nav;

	/**
	 * Main constructor. If the file does not exist, one will be created.
	 * 
	 * @param filepath the path to the XML file
	 * @throws ModifyException
	 * @throws IOException
	 */
	public XmlFile(final String filepath) throws ModifyException, IOException {
		this.filepath = filepath;
		
		Path path = Paths.get(filepath);
		if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
			Files.write(path, HEADER.getBytes(), StandardOpenOption.CREATE);
		}

		parse(filepath);
	}

	/**
	 * Inserts an XML element at the end of the XML file within the root tag.
	 * 
	 * @param element the XML element to add as a string.
	 * @throws NavException
	 * @throws ModifyException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public final void insertElementAtEnd(final String element) throws NavException, ModifyException,
			UnsupportedEncodingException, InterruptedException, ExecutionException {
		if(isWriting())
			writeFuture.get();

		nav.toElement(VTDNav.ROOT);
		xmlModifier.insertBeforeTail(element);

		write();
	}

	/**
	 * Removes the element specified by the given XPath. Returns true if an element
	 * was removed or false if no such element existed.
	 * 
	 * @param xPath the path to the element to be removed as specified by an XPath
	 * @return true if the element was successfully removed, otherwise false
	 * @throws XPathParseException
	 * @throws XPathEvalException
	 * @throws NavException
	 * @throws ModifyException
	 */
	public final boolean removeElement(final String xPath)
			throws XPathParseException, XPathEvalException, NavException, ModifyException {
		autopilot.selectXPath(xPath);

		if (autopilot.evalXPath() == -1)
			return false;

		nav.toElement(VTDNav.PARENT);
		xmlModifier.remove();

		write();

		return true;
	}

	/**
	 * Updates the element represented XPath with the given element string.
	 * 
	 * @param xPath   the element to replace
	 * @param element the string to be inserted
	 * @return true if the update was successful, otherwise false
	 * @throws XPathParseException
	 * @throws XPathEvalException
	 * @throws NavException
	 * @throws ModifyException
	 * @throws UnsupportedEncodingException
	 */
	public final boolean updateElement(final String xPath, final String element) throws XPathParseException,
			XPathEvalException, NavException, ModifyException, UnsupportedEncodingException {
		autopilot.selectXPath(xPath);

		if (autopilot.evalXPath() == -1)
			return false;

		nav.toElement(VTDNav.PARENT);
		xmlModifier.remove();
		xmlModifier.insertAfterElement(element);

		write();

		return true;
	}

	/**
	 * Checks to see if there is a write operation currently running.
	 * 
	 * @return true if the XML file is currently being written to, otherwise false.
	 */
	public final boolean isWriting() {
		return writeFuture != null && !writeFuture.isDone();
	}

	/**
	 * Blocks until the current write operation is complete. If no write operation
	 * is in progress this method returns immediately.
	 */
	public final void flush() {
		if (!isWriting())
			return;

		try {
			writeFuture.get();
		} catch (InterruptedException |ExecutionException e) {
			throw new XmlWriteException("Could not flush xml write " + filepath, e);
		}
	}

	/**
	 * Checks if the there are any changes to write and if so, creates a future and
	 * returns immediately.
	 */
	private final synchronized void write() {
		writeFuture = CompletableFuture.runAsync(this::outputAndReparse);
	}

	/**
	 * Writes and re-parses any changes to the XML file. If an error occurs, this
	 * method will throw an unchecked exception.
	 */
	private final void outputAndReparse() {
		try {
			xmlModifier.output(filepath);
			parse(filepath);
		} catch (IOException | ModifyException | TranscodeException e) {
			throw new XmlWriteException("Could not output and reparse XML file " + filepath, e);
		}
	}

	/**
	 * Parses the given file and binds the VTD navigator and modifier.
	 * 
	 * @param path the file system path to the XML file
	 * 
	 * @throws ModifyException if the XML modifier cannot be bound to the nav
	 */
	private final void parse(final String path) throws ModifyException {
		gen.parseFile(path, false);
		nav = gen.getNav();
		autopilot.bind(nav);
		xmlModifier.bind(nav);
	}
}
