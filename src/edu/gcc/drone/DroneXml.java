package edu.gcc.drone;

import edu.gcc.xml.XmlDaoFactory;

/**
 * Singleton class for the {@link DroneXmlDao} data access object. Any
 * references to {@link DroneXmlDao} should be gotten from this class.
 * 
 * @author Luke Donmoyer
 */
public class DroneXml {
	private static DroneXmlDao instance = XmlDaoFactory.createDao(
		DroneXmlDao.class
	);

	private DroneXml() {
		throw new UnsupportedOperationException(
				"Cannot create instance of singleton class"
		);
	}

	/**
	 * Returns the {@link DroneXmlDao} instance.
	 * 
	 * @return {@link DroneXmlDao}
	 */
	public static DroneXmlDao getInstance() {
		return instance;
	}
}
