import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import library.BorrowUC_CTL;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import library.daos.MemberHelper;
import library.daos.MemberMapDAO;
import library.entities.Loan;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.panels.borrow.ABorrowPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SuccessfulCompletionScenario {
	
	private IMemberDAO memberDAO;
	private IMember borrower;
	private int memberID;
	private IBookDAO bookDAO;
	private IBook book1;
	private IBook book2;
	private int bookID;
	private ILoanDAO loanDAO;
	private ILoan loan;
	private int loanID;
	private IBorrowUI ui;
	private BorrowUC_CTL ctl;
	private ICardReader reader;
	private IDisplay display;
	private IPrinter printer;
	private IScanner scanner;
	private Calendar calendar;
	private Date borrowDate;
	private Date dueDate;

	@Before
	public void setUp() throws Exception {
		memberDAO = new MemberMapDAO(new MemberHelper());
		bookDAO = new BookDAO(new BookHelper());
		loanDAO = new LoanMapDAO(new LoanHelper());
		ui = mock(ABorrowPanel.class);
		reader = mock(ICardReader.class);
		display = mock(IDisplay.class);
		printer = mock(IPrinter.class);
		scanner = mock(IScanner.class);
		ctl = new BorrowUC_CTL(reader, scanner, printer, display, bookDAO, loanDAO, memberDAO, ui);
		
		memberDAO.addMember("Adam", "Poulsen", "0401987654", "apoulsen@email.com");
		borrower = memberDAO.getMemberByID(1);
		memberID = borrower.getID();
		bookDAO.addBook("Sidney Bernardson", "Electronics", "999");
		book1 = bookDAO.getBookByID(1);
		loanID = 1;
		calendar = Calendar.getInstance();
		borrowDate = new Date();
		calendar.setTime(borrowDate);
		calendar.add(Calendar.DATE, ILoan.LOAN_PERIOD);
		dueDate = calendar.getTime();
		loan = new Loan(book1, borrower, borrowDate, dueDate);
		loan.commit(loanID);
		
	}

	@After
	public void tearDown() throws Exception {
		
		memberDAO = null;
		bookDAO = null;
		loanDAO = null;
		ui = null;
		book1 = null;
		book2 = null;
		loan = null;
		
	}

	@Test
	public void testSuccessfulCompletion() {
		ctl.initialise();
		verify(display).getDisplay();
		verify(display).setDisplay((JPanel) ui, "Borrow UI");	
		assertTrue(ctl.getState() == EBorrowState.INITIALIZED);
		
		ctl.cardSwiped(borrower.getID());
		verify(ui).displayMemberDetails(memberID, "Adam Poulsen", "0401987654");
		verify(reader).setEnabled(false);
		verify(scanner).setEnabled(true);
		verify(ui).setState(EBorrowState.SCANNING_BOOKS);
		assertTrue(ctl.getState() == EBorrowState.SCANNING_BOOKS);
		verify(reader).setEnabled(false);
		verify(scanner).setEnabled(true);
		verify(ui).displayExistingLoan(borrower.getLoans().get(0).toString());
		assertTrue(loan == borrower.getLoans().get(0));
		
		ctl.bookScanned(999);
		verify(ui).displayErrorMessage("");
		verify(ui, never()).displayErrorMessage(String.format("Book %d not found", 1));
		verify(ui, never()).displayErrorMessage(String.format("Book %d is not available: %s", book1.getID(), book1.getState()));
		verify(ui, never()).displayErrorMessage(String.format("Book %d already scanned: ", book1.getID()));
		verify(ui).displayScannedBookDetails("");
		verify(ui).displayPendingLoan("");
		
		ctl.scansCompleted();
		assertTrue(ctl.getState() == EBorrowState.CONFIRMING_LOANS);
		verify(reader, atLeast(2)).setEnabled(false);
		verify(scanner, atLeast(2)).setEnabled(false);
		verify(ui).displayConfirmingLoan("");
		
		ctl.loansConfirmed();
		assertTrue(ctl.getState() == EBorrowState.COMPLETED);
		verify(reader, atLeast(2)).setEnabled(false);
		verify(scanner, atLeast(2)).setEnabled(false);
		verify(printer).print("");
		
		//ctl.close() automatically called in setState() COMPLETED state case
		JPanel previous = display.getDisplay();
		verify(display).setDisplay(previous, "Main Menu");

	}

}
