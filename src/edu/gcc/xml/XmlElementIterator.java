package edu.gcc.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import edu.gcc.xml.exception.XmlReadException;

/**
 * @author Luke Donmoyer
 * 
 *         A wrapper class for an iterator over an XML file.
 */
public class XmlElementIterator implements Iterator<Map<String, String>> {

	private int nextNode = -1;

	private VTDNav vn;
	private AutoPilot ap;

	/**
	 * Main constructor. Sets up VTD-XML objects and primes the first element.
	 * 
	 * @param vn    The VTD Navigator to copy.
	 * @param xPath The xPath query to select elements.
	 */
	public XmlElementIterator(final VTDNav vn, final String xPath) {
		this.vn = vn.cloneNav();
		this.ap = new AutoPilot();
		this.ap.bind(this.vn);

		try {
			this.vn.toElement(VTDNav.ROOT);
			this.ap.selectXPath(xPath);
			this.nextNode = this.ap.evalXPath();
		} catch (NavException | XPathParseException | XPathEvalException e) {
			throw new XmlReadException("Could not iterate xml", e);
		}
	}

	@Override
	public boolean hasNext() {
		return nextNode != -1;
	}

	@Override
	public Map<String, String> next() {
		if (nextNode == -1)
			throw new NoSuchElementException();

		Map<String, String> result = new HashMap<>();

		// Iterator over object elements and store them in a map
		try {
			ap.selectElement("*");
			vn.toElement(VTDNav.FIRST_CHILD);

			while (ap.iterate()) {
				int value = vn.getText();

				if (value != -1)
					result.put(vn.toString(value - 1), vn.toString(value));
				else
					throw new NavException("This element does not have text");
			}

			vn.toElement(VTDNav.PARENT);
			nextNode = ap.evalXPath();
		} catch (NavException | XPathEvalException e) {
			throw new XmlReadException("Could not get next element ", e);
		}

		return result;
	}
}
