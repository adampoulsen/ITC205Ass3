import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import library.entities.Book;
import library.entities.Loan;
import library.entities.Member;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LoanDAOIntegrationTest {
	
	private IBook book;
	private String author;
	private String title;
	private String callNumber;
	private int bookID;
	
	private IMember borrower;
	private String fName;
	private String lName;
	private String contact;
	private String email;
	private int memberID;
	
	private Map<Integer, ILoan> loanMap;
	private ILoanHelper helper;
	private ILoan loan;
	private int loanID;
	private Date borrowDate;
	private Date dueDate;
	private Calendar calendar;
	private LoanMapDAO loanDAO;

	@Before
	public void setUp() throws Exception {
		
		author = "Adam Poulsen";
		title = "Computer Science 101";
		callNumber = "88";
		bookID = 10;
		book = new Book(author, title, callNumber, bookID);
		
		fName = "Sid";
		lName = "Bernardson";
		contact = "0401234567";
		email = "sbernadson@email.com";
		memberID = 77;
		borrower = new Member(fName, lName, contact, email, memberID);
		

		loanID = 1;
		calendar = Calendar.getInstance();
		borrowDate = new Date();
		calendar.setTime(borrowDate);
		calendar.add(Calendar.DATE, ILoan.LOAN_PERIOD);
		dueDate = calendar.getTime();
		loan = new Loan(book, borrower, borrowDate, dueDate);
		loan.commit(loanID);
		loanMap = new HashMap<Integer, ILoan>();
		loanMap.put(loanID, loan);
		helper = new LoanHelper();
		loanDAO = new LoanMapDAO(helper, loanMap);
		
	}

	@After
	public void tearDown() throws Exception {
		loanMap = null;
		helper = null;
		loanDAO = null;
		loan = null;
	}

	@Test
	public void testLoanDAO() {
		loanDAO = new LoanMapDAO(helper);
		assertNotNull(helper);
		assertTrue(loanDAO instanceof ILoanDAO);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testLoanDAONull() {
		loanDAO = new LoanMapDAO(null);
	}
	
	@Test
	public void testLoanDAOWithMap() {
		loanDAO = new LoanMapDAO(helper, loanMap);
		assertTrue(loanDAO instanceof ILoanDAO);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testLoanDAOWithMapNull() {
		loanDAO = new LoanMapDAO(helper, null);
	}
	
	@Test
	public void testGetLoanByID() {
		assertTrue(loanDAO.getLoanByID(loanID) == loan);
	}
	
	@Test
	public void testGetLoanByIDFalse() {
		int falseLoanID = 2;
		assertTrue(loanDAO.getLoanByID(falseLoanID) == null);
	}
	
	@Test
	public void testGetLoanByBook() {
		assertTrue(loanDAO.getLoanByBook(book) == loan);
	}
	
	@Test
	public void testGetLoanByBookFalse() {
		IBook book2 = new Book("Joe", "Gardening", "33", 65);
		assertTrue(loanDAO.getLoanByBook(book2) == null);
	}
	
	@Test
	public void testListLoans() {
		assertTrue(loanDAO.listLoans().contains(loan));
	}
	
	@Test
	public void testListLoansFail() {
		ILoan loan2 = new Loan(new Book("Joe", "Gardening", "33", 65), new Member("Trish", "Cresswell", "69538212", "pcresswell@email.com", 123), borrowDate, dueDate);
		assertFalse(loanDAO.listLoans().contains(loan2));
	}
	
	@Test
	public void testFindLoansByBorrower() {
		assertTrue(loanDAO.findLoansByBorrower(borrower).get(0) == loan);
	}
	
	@Test
	public void testFindLoansByBorrowerFalse() {
		IMember borrower2 = new Member("Trish", "Cresswell", "69538212", "pcresswell@email.com", 123);
		List<ILoan> list = new ArrayList<ILoan>(loanDAO.findLoansByBorrower(borrower2));
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testFindLoansByBookTitle() {
		assertTrue(loanDAO.findLoansByBookTitle(book.getTitle()).get(0) == loan);
	}
	
	@Test
	public void testFindLoansByBookTitleFalse() {
		IBook book2 = new Book("Joe", "Gardening", "33", 65);
		List<ILoan> list = new ArrayList<ILoan>(loanDAO.findLoansByBookTitle(book2.getTitle()));
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testUpdateOverDueStatus() {
		loanDAO.updateOverDueStatus(new Date());
	}
	
	@Test
	public void testFindOverDueLoansFalse() {
		List<ILoan> list = new ArrayList<ILoan>(loanDAO.findOverDueLoans());
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testFindOverDueLoansTrue() {
		Calendar falseCalendar = Calendar.getInstance();
		falseCalendar.add(Calendar.YEAR, 1000);
		Date falseDate = falseCalendar.getTime();
		loan.checkOverDue(falseDate);
		loanDAO.updateOverDueStatus(falseDate);
		List<ILoan> list = new ArrayList<ILoan>(loanDAO.findOverDueLoans());
		assertTrue(list.contains(loan));
	}
	
	@Test
	public void testCreateLoan() {
		ILoan loan = loanDAO.createLoan(borrower, book);
		assertNotNull(loan);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateLoanBadBorrowerParam() {
		ILoan loan = loanDAO.createLoan(null, book);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateLoanBadBookParam() {
		ILoan loan = loanDAO.createLoan(borrower, null);
	}
	
	@Test
	public void testCommitLoan() {
		ILoan loan2 = new Loan(new Book("Joe", "Gardening", "33", 65), new Member("Trish", "Cresswell", "69538212", "pcresswell@email.com", 123), borrowDate, dueDate);
		loanDAO.commitLoan(loan2);
	}
	
	@Test (expected=NullPointerException.class)
	public void testCommitLoanBadParam() {
		loanDAO.commitLoan(null);
	}
}
