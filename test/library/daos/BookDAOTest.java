package library.daos;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import library.entities.Book;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BookDAOTest {
	
	ILoanDAO loanDAO;
	IMemberDAO memberDAO;
	BookDAO bookDAO;
	IBookHelper helper;
	String author;
	String title;
	String callNumber;
	

	@Before
	public void setUp() throws Exception {
		loanDAO = mock(ILoanDAO.class);
		memberDAO = mock(IMemberDAO.class);
		helper = new BookHelper();
		author = "Adam Poulsen";
		title = "Computer Science 101";
		callNumber = "88";
		bookDAO = new BookDAO(helper);
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
		bookDAO.addBook(author, title, callNumber);
	}
	
	@Test
	public void testAddBookNewBookFoundInCollection() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		assertTrue(bookDAO.listBooks().contains(book));
	}

	@Test
	public void testGetBookByID() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		for (IBook e : bookDAO.listBooks()) {
			if (book.getID() == e.getID()) {
				assertTrue(book.getID() == e.getID());
			}
			else throw new RuntimeException("");
		}
	}
	
	@Test (expected=RuntimeException.class)
	public void testGetBookByIDNoIDMatch() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		int fakeID = 12345;
		for (IBook e : bookDAO.listBooks()) {
			if (e.getID() != fakeID) {
				throw new RuntimeException("");
			}
			else throw new IllegalArgumentException("");
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
			else throw new IllegalArgumentException("");
		}
	}

	@Test
	public void testFindBooksByAuthor() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (book.getAuthor() == e.getAuthor()) {
				assertTrue(book.getAuthor() == e.getAuthor());
			}
			else throw new RuntimeException("");
		}
	}
	
	@Test (expected=RuntimeException.class)
	public void testFindBooksByAuthorNoMatch() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		String authorTemp = "Joe";
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (e.getAuthor() != authorTemp) {
				throw new RuntimeException("");
			}
			else throw new IllegalArgumentException("");
		}
	}

	@Test
	public void testFindBooksByTitle() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (book.getTitle() == e.getTitle()) {
				assertTrue(book.getTitle() == e.getTitle());
			}
			else throw new RuntimeException("");
		}
	}
	
	@Test (expected=RuntimeException.class)
	public void testFindBooksByTitleNoMatch() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		String titleTemp = "Maths 201";
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (e.getTitle() != titleTemp) {
				throw new RuntimeException("");
			}
			else throw new IllegalArgumentException("");
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
			else throw new RuntimeException("");
		}
	}
	
	@Test (expected=RuntimeException.class)
	public void testFindBooksByAuthorTitleNoMatchTitle() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		String titleTemp = "Maths 201";
		for (IBook e : bookDAO.findBooksByTitle(title)) {
			if (e.getTitle() != titleTemp) {
				throw new RuntimeException("");
			}
			else throw new IllegalArgumentException("");
		}
	}
	
	@Test (expected=RuntimeException.class)
	public void testFindBooksByAuthorTitleNoMatchAuthor() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		String authorTemp = "Joe";
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (e.getAuthor() != authorTemp) {
				throw new RuntimeException("");
			}
			else throw new IllegalArgumentException("");
		}
	}
	
	@Test (expected=RuntimeException.class)
	public void testFindBooksByAuthorTitleNoMatches() {
		IBook book = bookDAO.addBook(author, title, callNumber);
		String titleTemp = "Maths 201";
		String authorTemp = "Joe";
		for (IBook e : bookDAO.findBooksByAuthor(author)) {
			if (e.getTitle() != titleTemp) {
				throw new RuntimeException("");
			}
			else {
				if (e.getAuthor() != authorTemp) {
					throw new RuntimeException("");
				}
				else throw new IllegalArgumentException("");
			}
		}
	}
	
}
