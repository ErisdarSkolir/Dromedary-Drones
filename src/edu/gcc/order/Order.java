package edu.gcc.order;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("id")
public class Order {
	private int id;
	
	private String clientName;
}
