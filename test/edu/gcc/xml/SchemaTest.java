package edu.gcc.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import edu.gcc.xml.annotation.XmlSerializable;



@ExtendWith(MockitoExtension.class)
public class SchemaTest {
	private Path path = Paths.get(String.format("%s\\DromedaryDrones\\db\\%s.xml", System.getenv("APPDATA"),
			TestObject.class.getSimpleName()));

	private Schema<TestObject> schema;

	@BeforeEach
	public void setup() {
		schema = new Schema<>(TestObject.class);
	}

	@AfterEach
	public void cleanup() throws IOException {
		Files.delete(path);
	}

	@Test
	public void schemaCreatesCorrectFile() {
		assertThat(Files.exists(path), is(true));
	}

	@Test
	public void schemaInsertWorksCorrectly() throws IOException {
		String afterInsert = "<root><TestObject><x>1</x><y>2</y><id>0</id></TestObject><TestObject><x>4</x><y>5</y><id>1</id></TestObject></root>";

		TestObject obj1 = new TestObject(0, 1, 2, 3);
		TestObject obj2 = new TestObject(1, 4, 5, 6);

		schema.insert(obj1);
		schema.insert(obj2);
		
		schema.flush();
		
		String contents = new String(Files.readAllBytes(path));
		assertThat(contents, is(equalTo(afterInsert)));
	}
	
	@Test
	public void schemaDeleteWorksCorrectly() throws IOException {
		String afterDelete = "<root><TestObject><x>1</x><y>2</y><id>0</id></TestObject></root>";
		TestObject obj1 = new TestObject(0, 1, 2, 3);
		TestObject obj2 = new TestObject(1, 4, 5, 6);

		schema.insert(obj1);
		schema.insert(obj2);
		
		schema.delete(obj2);
		
		schema.flush();
		
		String contents = new String(Files.readAllBytes(path));
		assertThat(contents, is(equalTo(afterDelete)));
	}
	
	@Test
	public void schemaUpdateWorksCorrectly() throws IOException {
		String beforeUpdate = "<root><TestObject><x>1</x><y>2</y><id>0</id></TestObject></root>";
		String afterUpdate = "<root><TestObject><x>1</x><y>3591</y><id>0</id></TestObject></root>";
		
		TestObject obj1 = new TestObject(0, 1, 2, 3);

		schema.insert(obj1);
		schema.flush();
		
		String contents = new String(Files.readAllBytes(path));
		assertThat(contents, is(equalTo(beforeUpdate)));
		
		obj1.y = 3591;
		
		schema.update(obj1);
		schema.flush();
		
		contents = new String(Files.readAllBytes(path));
		assertThat(contents, is(equalTo(afterUpdate)));
	}
	
	@Test
	public void schemaGetWorksCorrectly() throws IOException {
		TestObject obj1 = new TestObject(0, 1, 2, 3);

		schema.insert(obj1);
		
		TestObject schemaObject = schema.get("//TestObject[id[text()='0']]");
		
		assertThat(schemaObject.id, is(equalTo(obj1.id)));
		assertThat(schemaObject.x, is(equalTo(obj1.x)));
		assertThat(schemaObject.y, is(equalTo(obj1.y)));
	}
	
	@Test
	public void schemaTransientFieldsAreUninitialized() {
		TestObject obj1 = new TestObject(0, 1, 2, 3);

		schema.insert(obj1);
		
		TestObject schemaObject = schema.get("//TestObject[id[text()='0']]");
		
		assertThat(schemaObject.z, is(equalTo(0)));
		assertThat(schemaObject.z, is(not(equalTo(obj1.z))));
	}
	
	@Test
	public void schemaCanReturnEmtpyList() {
		TestObject obj1 = new TestObject(0, 1, 2, 3);

		schema.insert(obj1);
		
		List<TestObject> list = schema.getList("//TestObject[id[text()='1']]");
		
		assertThat(list.isEmpty(), is(equalTo(true)));
	}
	
	@Test
	public void schemaCanGetListOfOneElement() {
		TestObject obj1 = new TestObject(0, 1, 2, 3);

		schema.insert(obj1);
		
		List<TestObject> list = schema.getList("//TestObject[id[text()='0']]");
		
		assertThat(list.size(), is(equalTo(1)));

		TestObject schemaObject = list.get(0);
		
		assertThat(schemaObject.id, is(equalTo(obj1.id)));
		assertThat(schemaObject.x, is(equalTo(obj1.x)));
		assertThat(schemaObject.y, is(equalTo(obj1.y)));
	}
	
	@Test
	public void schemaCanGetListOfMultipleElements() throws IOException {
		TestObject obj1 = new TestObject(0, 1, 2, 3);
		TestObject obj2 = new TestObject(1, 4, 2, 6);

		schema.insert(obj1);
		schema.insert(obj2);
		
		List<TestObject> list = schema.getList("//TestObject");
		
		assertThat(list.size(), is(equalTo(2)));
		
		TestObject schemaObj1 = list.get(0);
		TestObject schemaObj2 = list.get(1);
		
		assertThat(schemaObj1.id, is(equalTo(obj1.id)));
		assertThat(schemaObj1.x, is(equalTo(obj1.x)));
		assertThat(schemaObj1.y, is(equalTo(obj1.y)));
		
		assertThat(schemaObj2.id, is(equalTo(obj2.id)));
		assertThat(schemaObj2.x, is(equalTo(obj2.x)));
		assertThat(schemaObj2.y, is(equalTo(obj2.y)));
	}
}
