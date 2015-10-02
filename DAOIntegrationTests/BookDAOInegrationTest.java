import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BookDAOInegrationTest {
	
	private BookDAO bookDAO;
	private IBookHelper helper;
	private String author;
	private String title;
	private String callNumber;

	@Before
	public void setUp() throws Exception {
		author = "Adam Poulsen";
		title = "Computer Science 101";
		callNumber = "88";
		
		helper = new BookHelper();
		bookDAO = new BookDAO(helper);
	}

	@After
	public void tearDown() throws Exception {
		helper = null;
		bookDAO = null;
	}

	@Test
	public void testBookDAO() {
		assertNotNull(helper);
		assertTrue(bookDAO instanceof IBookDAO);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testBookDAONull() {
		bookDAO = new BookDAO(null);
	}
	
	@Test
	public void testAddBook() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		assertNotNull(book);
		assertTrue(book == bookDAO.findBooksByAuthor(author).get(0));
	}
	
	@Test
	public void testGetBookByID() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		assertNotNull(book);
		assertTrue(book == bookDAO.getBookByID(book.getID()));
	}
	
	@Test
	public void testGetBookByIDFail() {
		IBook book = helper.makeBook(author, title, callNumber, 1);
		assertNotNull(book);
		assertTrue(bookDAO.getBookByID(book.getID()) == null);
	}
	
	@Test
	public void listBooks() {
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
		IBook book = bookDAO.addBook(author, title, callNumber);
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (book.getAuthor() == e.getAuthor()) {
				assertTrue(book.getAuthor() == e.getAuthor());
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testFindBooksByAuthorNoMatch() {
		bookDAO.addBook(author, title, callNumber);
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
		IBook book = bookDAO.addBook(author, title, callNumber);
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (book.getTitle() == e.getTitle()) {
				assertTrue(book.getTitle() == e.getTitle());
			}
			else fail("Match was found");
		}
	}
	
	@Test
	public void testFindBooksByTitleNoMatch() {
		bookDAO.addBook(author, title, callNumber);
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
		IBook book = bookDAO.addBook(author, title, callNumber);
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
		bookDAO.addBook(author, title, callNumber);
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
		bookDAO.addBook(author, title, callNumber);
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
		bookDAO.addBook(author, title, callNumber);
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
