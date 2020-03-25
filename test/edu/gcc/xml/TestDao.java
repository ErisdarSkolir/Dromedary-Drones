package edu.gcc.xml;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
	
	@XPathQuery(value = "//TestObject[id[text()='{0}']]", list = true)
	List<TestObject> getByIdList(final int id);
	
	@XPathQuery(value = "//TestObject[id[text()='{0}']]", asynchronous = true)
	CompletableFuture<TestObject> getByIdAsync(final int id);
	
	@XPathQuery(value = "//TestObject[id[text()='{0}']]", list = true, asynchronous = true)
	CompletableFuture<List<TestObject>> getByIdAsyncList(final int id);
}
