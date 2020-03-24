package edu.gcc.xml;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class XmlDaoFactoryTest {
	private Path path = Paths.get(String.format("%s\\DromedaryDrones\\db\\%s.xml", System.getenv("APPDATA"),
			TestObject.class.getSimpleName()));

	private TestDao dao;

	@BeforeEach
	public void setup() {
		dao = XmlDaoFactory.createDao(TestDao.class);
	}

	/*@AfterEach
	public void cleanup() throws IOException {
		Files.delete(path);
	}*/
	
	@Test
	public void test() {
		TestObject obj1 = new TestObject(0, 2, 3, 4);
		TestObject obj2 = new TestObject(1, 509, 10, 501);
		
		dao.insertObject(obj1);
		dao.insertObject(obj2);
		
		System.out.println(dao.getById(0));
	}
}
