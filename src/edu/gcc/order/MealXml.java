package edu.gcc.order;

import edu.gcc.xml.XmlDaoFactory;

public class MealXml {
	private static MealXmlDao instance = XmlDaoFactory.createDao(MealXmlDao.class);
	
	private MealXml() {
		throw new UnsupportedOperationException("Cannot create instance of singleton class");
	}
	
	public static MealXmlDao getInstance() {
		return instance;
	}
}
