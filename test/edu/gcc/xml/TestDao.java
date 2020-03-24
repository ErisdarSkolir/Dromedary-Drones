package edu.gcc.xml;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;

@XmlDao(TestObject.class)
public interface TestDao{
	@XmlInsert
	void insertObject(final TestObject object);
	
	@XmlDelete
	boolean deleteObject(final TestObject object);
	
	@XmlUpdate
	boolean updateObject(final TestObject object);
	
	@XPathQuery("//TestObject[id[text()='{0}']]")
	TestObject getById(final int id);
}
