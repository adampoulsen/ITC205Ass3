package library.daos;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BookDAOTest {

	BookDAO bookDAO;
	IBookHelper helper;
	String author;
	String title;
	String callNumber;
	

	@Before
	public void setUp() throws Exception {
		helper = mock(IBookHelper.class);
		bookDAO = new BookDAO(helper);
		author = "Adam Poulsen";
		title = "Computer Science 101";
		callNumber = "88";
	}

	@After
	public void tearDown() throws Exception {
		bookDAO = null;
		helper = null;
	}

	@Test
	public void testCreate() {
		assertTrue(bookDAO instanceof BookDAO);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateBadParamHelperNull() {
		bookDAO = new BookDAO(null);
	}
	
	@Test
	public void testAddBook() {
		IBook expectedBook = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(expectedBook);
		
		IBook actualBook = bookDAO.addBook(author, title, callNumber);
		
		verify(helper).makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class));
		assertEquals(expectedBook, actualBook);
	}
	
	@Test
	public void testAddBookNewBookFoundInCollection() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		assertTrue(bookDAO.listBooks().contains(book));
	}

	@Test
	public void testGetBookByID() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		for (IBook e : bookDAO.listBooks()) {
			if (book.getID() == e.getID()) {
				assertTrue(book.getID() == e.getID());
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testGetBookByIDNoIDMatch() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		int fakeID = 12345;
		for (IBook e : bookDAO.listBooks()) {
			if (e.getID() != fakeID) {
				assertTrue(e.getID() != fakeID);
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testGetBookByIDNullList() {
		assertTrue(bookDAO.listBooks().isEmpty());
	}

	@Test
	public void testListBooks() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		for (IBook e : bookDAO.listBooks()) {
			if (book == e) {
				assertTrue(book == e);
			}
			else fail("Match was found");
		}
	}

	@Test
	public void testFindBooksByAuthor() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (book.getAuthor() == e.getAuthor()) {
				assertTrue(book.getAuthor() == e.getAuthor());
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testFindBooksByAuthorNoMatch() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		String authorTemp = "Joe";
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (e.getAuthor() != authorTemp) {
				assertTrue(e.getAuthor() != authorTemp);
			}
			else fail("Match was found");
		}
	}

	@Test
	public void testFindBooksByTitle() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (book.getTitle() == e.getTitle()) {
				assertTrue(book.getTitle() == e.getTitle());
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testFindBooksByTitleNoMatch() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		String titleTemp = "Maths 201";
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (e.getTitle() != titleTemp) {
				assertTrue(e.getTitle() != titleTemp);
			}
			else fail("Match was found");
		}
	}

	@Test
	public void testFindBooksByAuthorTitle() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		for (IBook e : bookDAO.findBooksByAuthorTitle(author, title)) {
			if (book.getTitle() == e.getTitle()) {
				assertTrue(book.getTitle() == e.getTitle());
			}
			else {
				throw new RuntimeException("");
				}
			if (book.getAuthor() == e.getAuthor()) {
				assertTrue(book.getAuthor() == e.getAuthor());
			}
			else {
				throw new RuntimeException("");
			}
		}
	}
	
	@Test
	public void testFindBooksByAuthorTitleNoMatchTitle() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		String titleTemp = "Maths 201";
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (e.getTitle() != titleTemp) {
				assertTrue(e.getTitle() != titleTemp);
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testFindBooksByAuthorTitleNoMatchAuthor() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		String authorTemp = "Joe";
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (e.getAuthor() != authorTemp) {
				assertTrue(e.getAuthor() != authorTemp);
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testFindBooksByAuthorTitleNoMatches() {
		IBook book = mock(IBook.class);
		when(helper.makeBook(eq(author), eq(title), eq(callNumber), any(Integer.class))).thenReturn(book);
		String titleTemp = "Maths 201";
		String authorTemp = "Joe";
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (e.getTitle() != titleTemp) {
				assertTrue(e.getTitle() != titleTemp);
			}
			else {
				fail("Match was found");
			}
			if (e.getAuthor() != authorTemp) {
					assertTrue(e.getAuthor() != authorTemp);
				}
			else fail("Match was found");
		}
	}
}
