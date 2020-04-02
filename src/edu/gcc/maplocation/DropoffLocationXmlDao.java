package edu.gcc.maplocation;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;

@XmlDao(value = MapLocation.class, fileName = "DropoffLocation")
public interface DropoffLocationXmlDao {
	@XmlInsert
	public void insert(final MapLocation location);
	
	@XmlUpdate
	public boolean update(final MapLocation location);
	
	@XmlDelete
	public boolean delete(final MapLocation location);
	
	@XPathQuery("//MapLocation[id[text()='{0}']]")
	public MapLocation getMapLocation(final int id);
}
