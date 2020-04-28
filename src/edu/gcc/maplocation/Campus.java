package edu.gcc.maplocation;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable(value = "id", autogenerate = true)
public class Campus {
	private long id;
	private String name;
	
	public Campus(final String name) {
		this.name = name;
	}
	
	public final void setName(final String name) {
		this.name = name; 
	}
	
	public final String getName() {
		return name;
	}
	
	public final long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
