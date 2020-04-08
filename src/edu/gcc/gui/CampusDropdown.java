package edu.gcc.gui;

import edu.gcc.maplocation.Campus;
import edu.gcc.maplocation.CampusXml;
import edu.gcc.maplocation.CampusXmlDao;
import javafx.scene.control.ComboBox;

public class CampusDropdown {
	//XML
	private CampusXmlDao campusXml = CampusXml.getInstance();
	
	//Dropdown element
	private ComboBox<Campus> element;
	
	public CampusDropdown(final CampusMap campusMap) {
		this.element = new ComboBox<>();

		this.element.setItems(campusXml.getAll());
		this.element.setMinWidth(200);
		this.element.getSelectionModel().select(0);
		this.element.setOnAction(event -> campusMap.setMapLocationData(element.getValue()));
	}
	
	public Campus getValue() {
		return element.getValue();
	}
	
	public void setValue(final Campus campus) {
		element.setValue(campus);
	}
	
	public ComboBox<Campus> getElement(){
		return element;
	}
}
