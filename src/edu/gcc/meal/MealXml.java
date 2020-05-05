package edu.gcc.meal;

import edu.gcc.xml.XmlDaoFactory;

/**
 * Singleton class for the {@link MealXmlDao} data access object. Any
 * references to {@link MealXmlDao} should be gotten from this class.
 * 
 * @author Luke Donmoyer
 */
public class MealXml {
	private static MealXmlDao instance = XmlDaoFactory.createDao(MealXmlDao.class);
	
	private MealXml() {
		throw new UnsupportedOperationException("Cannot create instance of singleton class");
	}
	

	/**
	 * Returns the {@link MealXmlDao} instance.
	 * 
	 * @return {@link MealXmlDao}
	 */
	public static MealXmlDao getInstance() {
		return instance;
	}
}
