package edu.gcc.maplocation;

import edu.gcc.xml.XmlDaoFactory;

public class MapLocationXml {
	private static MapLocationXmlDao instance = XmlDaoFactory.createDao(MapLocationXmlDao.class);
	
	public static MapLocationXmlDao getInstance() {
		return instance;
	}
}
