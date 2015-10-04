package library;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

import library.interfaces.EBorrowState;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
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

public class BorrowUC_CTLCardSwipedTest {
	
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
	private IMember borrower;
	private int memberID;
	private ILoan loan;
	private ICardReader reader;
	private IDisplay display;
	private IPrinter printer;
	private IScanner scanner;
	private ABorrowPanel ui;
	private BorrowUC_CTL ctl;

	@Before
	public void setUp() throws Exception {
		
		bookDAO = mock(IBookDAO.class);
		memberDAO = mock(IMemberDAO.class);
		loanDAO = mock(ILoanDAO.class);
		borrower = mock(IMember.class);
		memberID = 1;
		loan = mock(ILoan.class);
		reader = mock(ICardReader.class);
		display = mock(IDisplay.class);
		printer = mock(IPrinter.class);
		scanner = mock(IScanner.class);
		ui = mock(ABorrowPanel.class);
		ctl = new BorrowUC_CTL(reader, scanner, printer, display, bookDAO, loanDAO, memberDAO, ui);
		
	}

	@After
	public void tearDown() throws Exception {
		bookDAO = null;
		memberDAO = null;
		loanDAO = null;
		reader = null;
		display = null;
		printer = null;
		scanner = null;
		ui = null;
		ctl = null;
	}
	
	@Test
	public void testCardSwipedNoIssues() {
		
		when(memberDAO.getMemberByID(memberID)).thenReturn(borrower);
		when(borrower.hasOverDueLoans()).thenReturn(false);
		when(borrower.hasReachedLoanLimit()).thenReturn(false);
		when(borrower.hasFinesPayable()).thenReturn(false);
		when(borrower.hasReachedFineLimit()).thenReturn(false);
		when(borrower.getID()).thenReturn(memberID);
		when(borrower.getFirstName()).thenReturn("");
		when(borrower.getLastName()).thenReturn("");
		when(borrower.getContactPhone()).thenReturn("");
		
				//Testing the display of existing loans, requires the use of the buildLoanListDisplay() method to build a string from a list of loans
		List<ILoan> loanList = mock(List.class);
		when(borrower.getLoans()).thenReturn(loanList);
		Iterator<ILoan> iterator = mock(Iterator.class);		
		when(loanList.iterator()).thenReturn(iterator);
		when(iterator.hasNext()).thenReturn(true, false);
		when(iterator.next()).thenReturn(loan);
		when(loan.toString()).thenReturn("loanDetails");
		
		when(display.getDisplay()).thenReturn(null);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		
				//Verify methods inside the cardSwiped() method
		verify(memberDAO).getMemberByID(memberID);
		verify(borrower).hasOverDueLoans();
		verify(borrower).hasReachedLoanLimit();
		verify(borrower).hasFinesPayable();
		verify(borrower).hasReachedFineLimit();
		verify(borrower).getID();
		verify(borrower).getFirstName();
		verify(borrower).getLastName();
		verify(borrower).getContactPhone();
		verify(ui).displayMemberDetails(memberID, " ", "");
				//More verifications required by the System Sequence Diagram
		verify(reader).setEnabled(false);
		verify(scanner).setEnabled(true);
		verify(ui).setState(EBorrowState.SCANNING_BOOKS);
		verify(ui).displayScannedBookDetails("");
		verify(ui).displayPendingLoan("");
				//displayOutstandingFineMessage() not verified because for this test we assume there is not outstanding fines
		verify(borrower, atLeast(2)).getLoans();
				//toString() method cannot be mocked, instead this assertTrue verifies loan.toString()
		assertTrue(loan.toString() == "loanDetails");
		verify(ui).displayExistingLoan("loanDetails");
		
	}
	
	@Test (expected=RuntimeException.class)
	public void testCardSwipedBadStateCancelled() {
		ctl.cancelled();
		ctl.cardSwiped(memberID);
	}
	
	@Test (expected=RuntimeException.class)
	public void testCardSwipedBadStateCompleted() {
		ctl.scansCompleted();
		ctl.cardSwiped(memberID);
	}
	
	@Test (expected=RuntimeException.class)
	public void testCardSwipedBadStateLoansConfirmed() {
		ctl.loansConfirmed();
		ctl.cardSwiped(memberID);
	}
	
	@Test (expected=RuntimeException.class)
	public void testCardSwipedBadStateScanningBooks() {
		ctl.loansRejected();
		ctl.cardSwiped(memberID);
	}	
	
	@Test
	public void testCardSwipedMemberNotFound() {
		when(memberDAO.getMemberByID(memberID)).thenReturn(null);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		verify(ui).displayErrorMessage(String.format("Member ID %d not found", memberID));
	}
	/*
	@Test
	public void testCardSwipedBorrowingRestrictedDueToOverDueLoans() {
		when(borrower.hasOverDueLoans()).thenReturn(true);
		assertTrue(ctl.getState() == EBorrowState.BORROWING_RESTRICTED);
		verify(reader).setEnabled(false);
		verify(scanner).setEnabled(false);
		verify(ui).displayErrorMessage(String.format("Member %d cannot borrow at this time.", borrower.getID()));
	}
	
	@Test
	public void testCardSwipedBorrowingRestrictedDueToReachedLoanLimit() {
		when(borrower.hasReachedLoanLimit()).thenReturn(true);
		assertTrue(ctl.getState() == EBorrowState.BORROWING_RESTRICTED);
		verify(reader).setEnabled(false);
		verify(scanner).setEnabled(false);
		verify(ui).displayErrorMessage(String.format("Member %d cannot borrow at this time.", borrower.getID()));
	}
	
	@Test
	public void testCardSwipedBorrowingRestrictedDueToReachedFineLimit() {
		when(borrower.hasReachedFineLimit()).thenReturn(true);
		assertTrue(ctl.getState() == EBorrowState.BORROWING_RESTRICTED);
		verify(reader).setEnabled(false);
		verify(scanner).setEnabled(false);
		verify(ui).displayErrorMessage(String.format("Member %d cannot borrow at this time.", borrower.getID()));
	}
	*/
	@Test
	public void testCardSwipedHasFines() {
		when(memberDAO.getMemberByID(memberID)).thenReturn(borrower);
		when(borrower.hasFinesPayable()).thenReturn(true);
		when(borrower.getFineAmount()).thenReturn(1.5f);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		verify(ui).displayOutstandingFineMessage(1.5f);
	}
	
	@Test
	public void testCardSwipedOverDue() {
		when(memberDAO.getMemberByID(memberID)).thenReturn(borrower);
		when(borrower.hasOverDueLoans()).thenReturn(true);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		verify(ui).displayOverDueMessage();
	}
	
	@Test
	public void testCardSwipedAtLoanLimit() {
		when(memberDAO.getMemberByID(memberID)).thenReturn(borrower);
		when(borrower.hasReachedLoanLimit()).thenReturn(true);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		verify(ui).displayAtLoanLimitMessage();
	}
	
	@Test
	public void testCardSwipedOFineLimit() {
		when(memberDAO.getMemberByID(memberID)).thenReturn(borrower);
		when(borrower.hasReachedFineLimit()).thenReturn(true);
		when(borrower.getFineAmount()).thenReturn(1.5f);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		verify(ui).displayOverFineLimitMessage(1.5f);
	}

}
