package edu.gcc.drone;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;
import javafx.collections.ObservableList;

/**
 * Data access object for a drone XML file.
 * 
 * @author Luke Donmoyer
 */
@XmlDao(Drone.class)
public interface DroneXmlDao {
	/**
	 * Insert thge given {@link Drone} into the XML file.
	 * 
	 * @param drone the {@link Drone} to insert.
	 */
	@XmlInsert
	public void insert(final Drone drone);

	/**
	 * Updates the given {@link Drone} in the XML file.
	 * 
	 * @param drone the {@link Drone} to update.
	 */
	@XmlUpdate
	public void update(final Drone drone);

	/**
	 * Deletes the given {@link Drone} in the XML file.
	 * 
	 * @param drone the {@link Drone} to delete.
	 */
	@XmlDelete
	public void delete(final Drone drone);

	/**
	 * Returns an {@link ObservableList} of {@link Drone}s with the given
	 * boolean loaded value.
	 * 
	 * @param loaded the value of the drone's loaded value.
	 * @return a {@link ObservableList} of {@link Drone}s
	 */
	@XPathQuery(value = "//Drone[loaded[text()='{0}']]", list = true, reactive = true)
	public ObservableList<Drone> getObservableLoadedDrones(
			final boolean loaded
	);
}
