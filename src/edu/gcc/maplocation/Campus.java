package edu.gcc.maplocation;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("name")
public class Campus {
	private String name;
	
	public Campus(final String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
