package library.entities;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BookTest {
	
	ILoan loan;
	IMember member;
	String author;
	String title;
	String callNumber;
	int bookID;
	EBookState state;
	IBook book;

	@Before
	public void setUp() throws Exception {
		loan = mock(ILoan.class);
		member = mock(IMember.class);
		author = "Adam Poulsen";
		title = "Computer Science 101";
		callNumber = "88";
		bookID = 99;
		book = new Book(author, title, callNumber, bookID);
		state = EBookState.AVAILABLE;
	}

	@After
	public void tearDown() throws Exception {
		book = null;
		state = null;
	}
	
	@Test
	public void testCreate() {
		assertTrue(book instanceof Book);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateBadParamAuthorNull() {
		book = new Book(null, title, callNumber, bookID);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateBadParamTitleNull() {
		book = new Book(author, null, callNumber, bookID);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateBadParamCallNumberNull() {
		book = new Book(author, title, null, bookID);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateBadParamBookIDLessThanOrEqualToZero() {
		book = new Book(author, title, callNumber, -1);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateBadParamBookIDEqualToZero() {
		book = new Book(author, title, callNumber, 0);
	}

	@Test
	public void testBorrow() {
		book.borrow(loan);
		assertTrue(loan == book.getLoan());
	}
	
	@Test (expected=RuntimeException.class)
	public void testBorrowBookNotAvailable() {
		book.borrow(loan);
		book.borrow(loan);
	}

	@Test
	public void testGetLoan() {
		book.borrow(loan);
		assertTrue(loan == book.getLoan());
	}
	
	@Test
	public void testGetLoanBookOnLoan() {
		assertTrue(book.getLoan() == null);
	}
	
	@Test
	public void testReturnBook() {
		book.borrow(loan);
		book.returnBook(false);
	}
	
	@Test
	public void testReturnBookDamaged() {
		book.borrow(loan);
		book.returnBook(true);
		assertTrue(book.getState() == EBookState.DAMAGED);
	}
	
	@Test (expected=RuntimeException.class)
	public void testReturnBookNotOnLoan() {
		book.returnBook(false);	
	}

	@Test
	public void testLose() {
		book.borrow(loan);
		book.lose();
		assertTrue(book.getState() == EBookState.LOST);
	}
	
	@Test (expected=RuntimeException.class)
	public void testLoseBookNotOnLoan() {
		book.lose();
	}

	@Test
	public void testRepair() {
		book.borrow(loan);
		book.returnBook(true);
		book.repair();
		assertTrue(book.getState() == EBookState.AVAILABLE);
	}
	
	@Test (expected=RuntimeException.class)
	public void testRepairNotDamaged() {
		book.repair();
	}
	
	@Test 
	public void testDisposeBookAvailable() {
		book.borrow(loan);
		book.returnBook(false);
		book.dispose();
		assertTrue(book.getState() == EBookState.DISPOSED);
	}
	
	@Test 
	public void testDisposeBookDamaged() {
		book.borrow(loan);
		book.returnBook(true);
		book.dispose();
		assertTrue(book.getState() == EBookState.DISPOSED);
	}
	
	@Test 
	public void testDisposeBookLost() {
		book.borrow(loan);
		book.lose();
		book.dispose();
		assertTrue(book.getState() == EBookState.DISPOSED);
	}
	
	@Test (expected=RuntimeException.class)
	public void testDisposeBookOnLoan() {
		book.borrow(loan);
		book.dispose();
	}
	
	@Test (expected=RuntimeException.class)
	public void testDisposeBookDisposed() {
		book.borrow(loan);
		book.dispose();
	}

	@Test
	public void testGetState() {
		assertTrue(book.getState() == state);
	}

	@Test
	public void testGetAuthor() {
		assertTrue(book.getAuthor() == author);
	}

	@Test
	public void testGetTitle() {
		assertTrue(book.getTitle() == title);
	}

	@Test
	public void testGetCallNumber() {
		assertTrue(book.getCallNumber() == callNumber);
	}

	@Test
	public void testGetID() {
		assertTrue(book.getID() == bookID);
	}
	

}

