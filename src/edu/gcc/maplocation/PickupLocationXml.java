package edu.gcc.maplocation;

import edu.gcc.xml.XmlDaoFactory;

public class PickupLocationXml {
	private static PickupLocationXmlDao instance = XmlDaoFactory.createDao(PickupLocationXmlDao.class);
	
	public static PickupLocationXmlDao getInstance() {
		return instance;
	}
}
