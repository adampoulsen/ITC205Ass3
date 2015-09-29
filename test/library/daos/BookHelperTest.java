package library.daos;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import library.entities.Book;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BookHelperTest {
	
	IBookDAO bookDAO;
	IBookHelper helper;
	String author;
	String title;
	String callNumber;
	int id;

	@Before
	public void setUp() throws Exception {
		helper = new BookHelper();
		bookDAO = mock(IBookDAO.class);
		author = "Adam Poulsen";
		title = "Computer Science 101";
		callNumber = "88";
		id = 99;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertTrue(helper instanceof BookHelper);
	}
	
	@Test
	public void testMakeBook() {
		IBook book1 = helper.makeBook(author, title, callNumber, id);
		IBook book2 = new Book(author, title, callNumber, id);
		
		assertTrue(book1.getID() == book2.getID());
	}

}
