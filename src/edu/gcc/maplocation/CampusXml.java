package edu.gcc.maplocation;

import edu.gcc.xml.XmlDaoFactory;

public class CampusXml {
	private static CampusXmlDao instance = XmlDaoFactory.createDao(CampusXmlDao.class);
	
	public static CampusXmlDao getInstance() {
		return instance;
	}
}
