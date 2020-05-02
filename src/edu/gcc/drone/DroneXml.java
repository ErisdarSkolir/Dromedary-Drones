package edu.gcc.drone;

import edu.gcc.xml.XmlDaoFactory;

public class DroneXml {
	private static DroneXmlDao instance = XmlDaoFactory.createDao(DroneXmlDao.class);
	
	private DroneXml() {
		throw new UnsupportedOperationException("Cannot create instance of singleton class");
	}
	
	public static DroneXmlDao getInstance() {
		return instance;
	}
}
