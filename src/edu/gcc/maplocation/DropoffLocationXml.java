package edu.gcc.maplocation;

import edu.gcc.xml.XmlDaoFactory;

public class DropoffLocationXml {
	private static DropoffLocationXmlDao instance = XmlDaoFactory.createDao(DropoffLocationXmlDao.class);
	
	public static DropoffLocationXmlDao getInstance() {
		return instance;
	}
}
