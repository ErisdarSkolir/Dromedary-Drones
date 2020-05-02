package edu.gcc.drone;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;
import javafx.collections.ObservableList;

@XmlDao(Drone.class)
public interface DroneXmlDao {
	@XmlInsert
	public void insert(final Drone drone);
	
	@XmlUpdate
	public void update(final Drone drone);
	
	@XmlDelete
	public void delete(final Drone drone);
	
	@XPathQuery(value = "//Drone[loaded[text()='{0}']]", list = true, reactive = true)
	public ObservableList<Drone> getObservableLoadedDrones(final boolean loaded);
}
