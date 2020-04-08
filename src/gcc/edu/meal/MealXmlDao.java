package gcc.edu.meal;

import java.util.List;

import edu.gcc.xml.annotation.XPathQuery;
import edu.gcc.xml.annotation.XmlDao;
import edu.gcc.xml.annotation.XmlDelete;
import edu.gcc.xml.annotation.XmlInsert;
import edu.gcc.xml.annotation.XmlUpdate;
import javafx.collections.ObservableList;

@XmlDao(Meal.class)
public interface MealXmlDao {
	@XmlInsert
	public void insert(final Meal meal);
	
	@XmlUpdate
	public boolean update(final Meal meal);
	
	@XmlDelete
	public boolean delete(final Meal meal);
	
	@XPathQuery(value = "//Meal", list = true, reactive = true)
	public ObservableList<Meal> getAllObservable();
	
	@XPathQuery(value = "//Meal", list = true)
	public List<Meal> getAll();
}
