package edu.gcc.maplocation;

import java.util.List;

import edu.gcc.xml.XmlReactive;
import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;

@XmlDao(MapLocation.class)
public interface MapLocationXmlDao {
	@XmlInsert
	public void insert(final MapLocation location);
	
	@XmlUpdate
	public boolean update(final MapLocation location);
	
	@XmlDelete
	public boolean delete(final MapLocation location);
	
	@XPathQuery("//MapLocation[id[text()='{0}']]")
	public MapLocation getMapLocation(final int id);
	
	@XPathQuery(value = "//MapLocation[campus[text()='{0}'] and type[text()='1']]", list = true, reactive = true)
	public XmlReactive<List<MapLocation>> getDropoffReactiveForCampus(final String campus);
}
