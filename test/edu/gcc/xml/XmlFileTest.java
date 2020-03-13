package edu.gcc.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
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
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import com.ximpleware.ModifyException;
import com.ximpleware.NavException;

import edu.gcc.xml.exception.XmlWriteException;

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
	
	@BeforeEach
	public void setup() throws IOException {
		Path path = Paths.get(FILE_PATH);
		
		Files.deleteIfExists(path);
		
		Files.write(path, CONTENTS.getBytes(), StandardOpenOption.CREATE);
	}
	
	@Test
	public void xmlFileconstructorOnNonExistingFile() {
		try {
			XmlFile xml = new XmlFile(NON_EXISTING_FILE_PATH);
			assertNotNull(xml);
			
			Path path = Paths.get(NON_EXISTING_FILE_PATH);
			assertThat(Files.exists(path), is(true));
			
		} catch (ModifyException | IOException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@Test
	public void xmlFileConstructorOnExistingFile() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
		} catch (ModifyException | IOException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@Test
	public void isWritingReturnsFalseWhenFutureIsNull() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			assertThat(xml.isWriting(), is(false));
		} catch (ModifyException | IOException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@Test
	public void isWritingReturnsFalseWhenFutureIsDone() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			@SuppressWarnings("unchecked")
			CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
			Mockito.when(future.isDone()).thenReturn(true);
			Whitebox.setInternalState(xml, "writeFuture", future);
			
			assertThat(xml.isWriting(), is(false));
		} catch (ModifyException | IOException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@Test
	public void isWritingReturnsTrueWhenFutureIsNotDone() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			@SuppressWarnings("unchecked")
			CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
			Mockito.when(future.isDone()).thenReturn(false);
			Whitebox.setInternalState(xml, "writeFuture", future);
			
			assertThat(xml.isWriting(), is(true));
		} catch (ModifyException | IOException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@Test
	public void flushReturnsIfIsWritingIsFalse() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			@SuppressWarnings("unchecked")
			CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
			Mockito.when(future.isDone()).thenReturn(true);
			Whitebox.setInternalState(xml, "writeFuture", future);
			
			xml.flush();
			
			Mockito.verify(future, Mockito.times(0)).get();
		} catch (ModifyException | IOException | InterruptedException | ExecutionException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@Test
	public void flushCallsGetIfIsWritingIsTrue() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			@SuppressWarnings("unchecked")
			CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
			Mockito.when(future.isDone()).thenReturn(false);
			Whitebox.setInternalState(xml, "writeFuture", future);
			
			xml.flush();
			
			Mockito.verify(future, Mockito.times(1)).get();
		} catch (ModifyException | IOException | InterruptedException | ExecutionException e) {
			fail("XmlFile constructor failed " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void flushThrowsExceptionIfGetThrowsInterruptedException() {
		Assertions.assertThrows(XmlWriteException.class, () -> {
			try {
				XmlFile xml = new XmlFile(FILE_PATH);
				assertNotNull(xml);
				
				
				CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
				Mockito.when(future.isDone()).thenReturn(false);
				Mockito.when(future.get()).thenThrow(InterruptedException.class);
				Whitebox.setInternalState(xml, "writeFuture", future);
				
				xml.flush();
			} catch (ModifyException | IOException | InterruptedException | ExecutionException e) {
				fail("XmlFile constructor failed " + e.getMessage());
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void flushThrowsExceptionIfGetThrowsExecutionException() {
		Assertions.assertThrows(XmlWriteException.class, () -> {
			try {
				XmlFile xml = new XmlFile(FILE_PATH);
				assertNotNull(xml);
				
				
				CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
				Mockito.when(future.isDone()).thenReturn(false);
				Mockito.when(future.get()).thenThrow(ExecutionException.class);
				Whitebox.setInternalState(xml, "writeFuture", future);
				
				xml.flush();
			} catch (ModifyException | IOException | InterruptedException | ExecutionException e) {
				fail("XmlFile constructor failed " + e.getMessage());
			}
		});
	}
	
	private final String toInsert = "   <book id=\"bk103\">\r\n" + 
			"      <author>Corets, Eva</author>\r\n" + 
			"      <title>Maeve Ascendant</title>\r\n" + 
			"      <genre>Fantasy</genre>\r\n" + 
			"      <price>5.95</price>\r\n" + 
			"      <publish_date>2000-11-17</publish_date>\r\n" + 
			"      <description>After the collapse of a nanotechnology \r\n" + 
			"      society in England, the young survivors lay the \r\n" + 
			"      foundation for a new society.</description>\r\n" + 
			"   </book>\r\n";
	
	private final String afterInsert = "<?xml version=\"1.0\"?>\r\n" + 
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
	public void insertAfterElement() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			@SuppressWarnings("unchecked")
			CompletableFuture<Void> future = Mockito.mock(CompletableFuture.class);
			Mockito.when(future.isDone()).thenReturn(false);
			Whitebox.setInternalState(xml, "writeFuture", future);
			
			xml.insertElementAtEnd(toInsert);
			xml.flush();
			
			String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
			
			Mockito.verify(future, Mockito.times(1)).get();
			assertThat(newContents, is(equalTo(afterInsert)));
			
		} catch (ModifyException | IOException | NavException | InterruptedException | ExecutionException e) {
			fail("XmlFile failed " + e.getMessage());
		}
	}
	
	@Test
	public void insertAfterElementWaitsForFutureIfWriting() {
		try {
			XmlFile xml = new XmlFile(FILE_PATH);
			assertNotNull(xml);
			
			xml.insertElementAtEnd(toInsert);
			xml.flush();
			
			String newContents = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
			
			assertThat(newContents, is(equalTo(afterInsert)));
			
		} catch (ModifyException | IOException | NavException | InterruptedException | ExecutionException e) {
			fail("XmlFile failed " + e.getMessage());
		}
	}
	
	@AfterAll
	public static void cleanup() throws IOException {
		Files.delete(Paths.get(FILE_PATH));
		Files.deleteIfExists(Paths.get(NON_EXISTING_FILE_PATH));
	}
}
