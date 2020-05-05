package edu.gcc.meal;

import java.util.List;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;
import javafx.collections.ObservableList;

/**
 * Data access object for the meals.
 * 
 * @author Luke Donmoyer
 */
@XmlDao(Meal.class)
public interface MealXmlDao {
	/**
	 * Inserts the given meal into the XML file.
	 * 
	 * @param meal The meal to insert.
	 */
	@XmlInsert
	public void insert(final Meal meal);

	/**
	 * Updates the given meal in the XML file.
	 * 
	 * @param meal The meal to update
	 * @return true if the value was updated, otherwise false.
	 */
	@XmlUpdate
	public boolean update(final Meal meal);

	/**
	 * Deletes the given meal from the XML file.
	 * 
	 * @param meal The meal to delete
	 * @return true if the value was deleted, otherwise false.
	 */
	@XmlDelete
	public boolean delete(final Meal meal);

	/**
	 * Gets a list o fall meals as an observable list.
	 * 
	 * @return An observable list of all meals.
	 */
	@XPathQuery(value = "//Meal", list = true, reactive = true)
	public ObservableList<Meal> getAllObservable();

	/**
	 * Get a regular list of all meals.
	 * 
	 * @return A list of meals.
	 */
	@XPathQuery(value = "//Meal", list = true)
	public List<Meal> getAll();

	/**
	 * Returns an observable list of meals with the given boolean loaded state.
	 * 
	 * @param loaded The loaded state.
	 * @return An observable list of meals with the given loaded state.
	 */
	@XPathQuery(value = "//Meal[loaded[text()='{0}']]", list = true, reactive = true)
	public ObservableList<Meal> getAllLoadedObservable(final boolean loaded);
}
