package edu.gcc.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;

import edu.gcc.xml.exception.XmlInsertElementException;
import edu.gcc.xml.exception.XmlWriteException;

@ExtendWith(MockitoExtension.class)
class XmlFileTest {
	private static final String FILE_PATH = "test/res/XmlFileTest.xml";
	private static final String NON_EXISTING_FILE_PATH = "test/res/XmlNonExistingFile.xml";
	
	private static final String CONTENTS = "<?xml version=\"1.0\"?>\r\n" + 
			"<catalog>\r\n" + 
			"   <book id=\"bk101\">\r\n" + 
			"      <author>Gambardella, Matthew</author>\r\n" + 
			"      <title>XML Developer's Guide</title>\r\n" + 
			"      <genre>Computer</genre>\r\n" + 
			"      <price>44.95</price>\r\n" + 
			"      <publish_date>2000-10-01</publish_date>\r\n" + 
			"      <description>An in-depth look at creating applications \r\n" + 
			"      with XML.</description>\r\n" + 
			"   </book>\r\n" + 
			"   <book id=\"bk102\">\r\n" + 
			"      <author>Ralls, Kim</author>\r\n" + 
			"      <title>Midnight Rain</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>5.95</price>\r\n" + 
			"      <publish_date>2000-12-16</publish_date>\r\n" + 
			"      <description>A former architect battles corporate zombies, \r\n" + 
			"      an evil sorceress, and her own childhood to become queen \r\n" + 
			"      of the world.</description>\r\n" + 
			"   </book>\r\n" +
			"</catalog>";
	
	@Mock
	private CompletableFuture<Void> future;
	
	@Mock
	private VTDNav nav;
	
	private XmlFile xml;
	private Field futureField;
	private Field navField;
	
	@BeforeEach
	public void setup() throws IOException, ModifyException, NoSuchFieldException {
		Path path = Paths.get(FILE_PATH);
		
		Files.deleteIfExists(path);
		Files.write(path, CONTENTS.getBytes(), StandardOpenOption.CREATE);
		
		xml = new XmlFile(FILE_PATH);
		assertNotNull(xml);
		
		futureField = XmlFile.class.getDeclaredField("writeFuture");
		navField = XmlFile.class.getDeclaredField("nav");
	}
	
	@AfterAll
	public static void cleanup() throws IOException {
		Files.delete(Paths.get(FILE_PATH));
		Files.deleteIfExists(Paths.get(NON_EXISTING_FILE_PATH));
	}
	
	@Test
	public void xmlFileconstructorOnNonExistingFile() throws ModifyException, IOException {
		XmlFile file = new XmlFile(NON_EXISTING_FILE_PATH);
		assertNotNull(file);
		
		Path path = Paths.get(NON_EXISTING_FILE_PATH);
		assertThat(Files.exists(path), is(true));
	}
	
	@Test
	public void xmlFileConstructorOnExistingFile() throws ModifyException, IOException {
		XmlFile file = new XmlFile(FILE_PATH);
		assertNotNull(file);
	}
	
	@Test
	public void isWritingReturnsFalseWhenFutureIsNull() {
		assertThat(xml.isWriting(), is(false));
	}
	
	@Test
	public void isWritingReturnsFalseWhenFutureIsDone() {
		Mockito.when(future.isDone()).thenReturn(true);
		FieldSetter.setField(xml, futureField, future);
		
		assertThat(xml.isWriting(), is(false));
	}
	
	@Test
	public void isWritingReturnsTrueWhenFutureIsNotDone() {
		Mockito.when(future.isDone()).thenReturn(false);
		FieldSetter.setField(xml, futureField, future);
		
		assertThat(xml.isWriting(), is(true));
	}
	
	@Test
	public void flushReturnsIfIsWritingIsFalse() throws InterruptedException, ExecutionException {
		Mockito.when(future.isDone()).thenReturn(true);
		FieldSetter.setField(xml, futureField, future);
		
		xml.flush();
			
		Mockito.verify(future, Mockito.times(0)).get();
	}
	
	@Test
	public void flushCallsGetIfIsWritingIsTrue() throws InterruptedException, ExecutionException {
		Mockito.when(future.isDone()).thenReturn(false);
		FieldSetter.setField(xml, futureField, future);
		
		xml.flush();
		
		Mockito.verify(future, Mockito.times(1)).get();
	}
	
	@Test
	public void flushThrowsExceptionIfGetThrowsInterruptedException() {
		Assertions.assertThrows(XmlWriteException.class, () -> {
			Mockito.when(future.isDone()).thenReturn(false);
			Mockito.when(future.get()).thenThrow(InterruptedException.class);
			FieldSetter.setField(xml, futureField, future);
			
			xml.flush();
		});
	}
	
	@Test
	public void flushThrowsExceptionIfGetThrowsExecutionException() {
		Assertions.assertThrows(XmlWriteException.class, () -> {
			Mockito.when(future.isDone()).thenReturn(false);
			Mockito.when(future.get()).thenThrow(ExecutionException.class);
			FieldSetter.setField(xml, futureField, future);
			
			xml.flush();
		});
	}
	
	private static final String toInsert = "   <book id=\"bk103\">\r\n" + 
			"      <author>Corets, Eva</author>\r\n" + 
			"      <title>Maeve Ascendant</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>5.95</price>\r\n" + 
			"      <publish_date>2000-11-17</publish_date>\r\n" + 
			"      <description>After the collapse of a nanotechnology \r\n" + 
			"      society in England, the young survivors lay the \r\n" + 
			"      foundation for a new society.</description>\r\n" + 
			"   </book>\r\n";
	
	private static final String afterInsert = "<?xml version=\"1.0\"?>\r\n" + 
			"<catalog>\r\n" + 
			"   <book id=\"bk101\">\r\n" + 
			"      <author>Gambardella, Matthew</author>\r\n" + 
			"      <title>XML Developer's Guide</title>\r\n" + 
			"      <genre>Computer</genre>\r\n" + 
			"      <price>44.95</price>\r\n" + 
			"      <publish_date>2000-10-01</publish_date>\r\n" + 
			"      <description>An in-depth look at creating applications \r\n" + 
			"      with XML.</description>\r\n" + 
			"   </book>\r\n" + 
			"   <book id=\"bk102\">\r\n" + 
			"      <author>Ralls, Kim</author>\r\n" + 
			"      <title>Midnight Rain</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>5.95</price>\r\n" + 
			"      <publish_date>2000-12-16</publish_date>\r\n" + 
			"      <description>A former architect battles corporate zombies, \r\n" + 
			"      an evil sorceress, and her own childhood to become queen \r\n" + 
			"      of the world.</description>\r\n" + 
			"   </book>\r\n" +
			"   <book id=\"bk103\">\r\n" + 
			"      <author>Corets, Eva</author>\r\n" + 
			"      <title>Maeve Ascendant</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>5.95</price>\r\n" + 
			"      <publish_date>2000-11-17</publish_date>\r\n" + 
			"      <description>After the collapse of a nanotechnology \r\n" + 
			"      society in England, the young survivors lay the \r\n" + 
			"      foundation for a new society.</description>\r\n" + 
			"   </book>\r\n" +
			"</catalog>";
	
	@Test
	public void insertAfterElementWaitsForFutureIfWriting() throws IOException, 
		InterruptedException, ExecutionException {
		Mockito.when(future.isDone()).thenReturn(false);
		FieldSetter.setField(xml, futureField, future);
		
		xml.insertElementAtEnd(toInsert);
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		Mockito.verify(future, Mockito.times(1)).get();
		assertThat(newContents, is(equalTo(afterInsert)));
	}
	
	@Test
	public void insertAfterElement() throws IOException {
		xml.insertElementAtEnd(toInsert);
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		assertThat(newContents, is(equalTo(afterInsert)));
	}
	
	private final String afterRemove = "<?xml version=\"1.0\"?>\r\n" + 
			"<catalog>\r\n" + 
			"   <book id=\"bk101\">\r\n" + 
			"      <author>Gambardella, Matthew</author>\r\n" + 
			"      <title>XML Developer's Guide</title>\r\n" + 
			"      <genre>Computer</genre>\r\n" + 
			"      <price>44.95</price>\r\n" + 
			"      <publish_date>2000-10-01</publish_date>\r\n" + 
			"      <description>An in-depth look at creating applications \r\n" + 
			"      with XML.</description>\r\n" + 
			"   </book>\r\n" +
			"   \r\n" +
			"</catalog>";
	
	@Test
	public void removeElementRemovesCorrectElement() throws IOException {
		boolean result = xml.removeElement("//book[author[text()='Ralls, Kim']]");
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		assertThat(result, is(true));
		assertThat(newContents, is(equalTo(afterRemove)));
	}
	
	@Test
	public void removeElementReturnsFalseIfElementDoesNotExist() throws IOException {
		boolean result = xml.removeElement("//book[@id=\"bk102354678\"]");
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		assertThat(result, is(false));
		assertThat(newContents, is(equalTo(CONTENTS)));
	}
	
	@Test
	public void removeElementWaitsForFutureIfWriting() throws InterruptedException, 
		ExecutionException, IOException {
		Mockito.when(future.isDone()).thenReturn(false);
		FieldSetter.setField(xml, futureField, future);
		
		boolean result = xml.removeElement("//book[@id=\"bk102\"]");
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		Mockito.verify(future, Mockito.times(1)).get();
		assertThat(result, is(true));
		assertThat(newContents, is(equalTo(afterRemove)));
	}
	
	private static final String afterEdit = "<?xml version=\"1.0\"?>\r\n" + 
			"<catalog>\r\n" + 
			"   <book id=\"bk101\">\r\n" + 
			"      <author>Gambardella, Matthew</author>\r\n" + 
			"      <title>XML Developer's Guide</title>\r\n" + 
			"      <genre>Computer</genre>\r\n" + 
			"      <price>44.95</price>\r\n" + 
			"      <publish_date>2000-10-01</publish_date>\r\n" + 
			"      <description>An in-depth look at creating applications \r\n" + 
			"      with XML.</description>\r\n" + 
			"   </book>\r\n   " + 
			"	<book id=\"bk103\">\r\n" + 
			"      <author>Smith, John</author>\r\n" + 
			"      <title>Some random title</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>1000.00</price>\r\n" + 
			"      <publish_date>2020-11-17</publish_date>\r\n" + 
			"      <description>After the collapse of a nanotechnology \r\n" + 
			"      society in England, the young survivors lay the \r\n" + 
			"      foundation for a new society.</description>\r\n" + 
			"   </book>\r\n" +
			"</catalog>";
	
	private static final String edit = "	<book id=\"bk103\">\r\n" + 
			"      <author>Smith, John</author>\r\n" + 
			"      <title>Some random title</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>1000.00</price>\r\n" + 
			"      <publish_date>2020-11-17</publish_date>\r\n" + 
			"      <description>After the collapse of a nanotechnology \r\n" + 
			"      society in England, the young survivors lay the \r\n" + 
			"      foundation for a new society.</description>\r\n" + 
			"   </book>";
	
	@Test
	public void updateElementCorrectlyUpdatesElement() throws IOException {
		boolean result = xml.updateElement("//book[@id=\"bk102\"]", edit);
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		assertThat(result, is(true));
		assertThat(newContents, is(equalTo(afterEdit)));
	}
	
	@Test
	public void updateElementReturnsFalseIfXPathIsInvalid() throws IOException {
		boolean result = xml.updateElement("//book[@id='99999']", edit);
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		assertThat(result, is(false));
		assertThat(newContents, is(equalTo(CONTENTS)));
	}
	
	@Test
	public void updateElementWaitsForFutureIfWriting() throws IOException {
		Mockito.when(future.isDone()).thenReturn(false);
		FieldSetter.setField(xml, futureField, future);
		
		boolean result = xml.updateElement("//book[@id=\"bk102\"]", edit);
		xml.flush();
		
		String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
		
		assertThat(result, is(true));
		assertThat(newContents, is(equalTo(afterEdit)));
	}
}
