package edu.gcc.xml;

import edu.gcc.xml.annotation.XmlSerializable;

@XmlSerializable("id")
public class TestObject {
	public int id;

	public int x;
	public int y;

	public transient int z;

	public TestObject(int id, int x, int y, int z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "TestObject [id=" + id + ", x=" + x + ", y=" + y + "]";
	}
}