package edu.gcc.maplocation;

import java.util.List;

import edu.gcc.xml.XmlReactive;
import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;

@XmlDao(value = MapLocation.class, fileName = "PickupLocation")
public interface PickupLocationXmlDao {
	@XmlInsert
	public void insert(final MapLocation location);
	
	@XmlUpdate
	public boolean update(final MapLocation location);
	
	@XmlDelete
	public boolean delete(final MapLocation location);
	
	@XPathQuery("//MapLocation[id[text()='{0}']]")
	public MapLocation getMapLocation(final int id);
	
	@XPathQuery(value = "//MapLocation", list = true)
	public List<MapLocation> getAll();
	
	@XPathQuery(value = "//MapLocation", list = true, reactive = true)
	public XmlReactive<List<MapLocation>> getAllReactive();
}
