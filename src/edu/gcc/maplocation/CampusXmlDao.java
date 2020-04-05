package edu.gcc.maplocation;

import java.util.List;

import edu.gcc.xml.XmlReactive;
import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;

@XmlDao(Campus.class)
public interface CampusXmlDao {
	@XmlInsert
	public void insert(final Campus campus);
	
	@XmlUpdate
	public boolean update(final Campus campus);
	
	@XmlDelete
	public boolean delete(final Campus campus);
	
	@XPathQuery(value = "//Campus", list = true, reactive = true)
	public XmlReactive<List<Campus>> getAll();
}
