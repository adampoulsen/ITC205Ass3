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

public class BorrowUC_CTLTest {
	
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
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
	public void testCardSwipedSucessful() {
		IMember borrower = mock(IMember.class);
		ILoan loan = mock(ILoan.class);
		int id = 1;
		
		when(memberDAO.getMemberByID(id)).thenReturn(borrower);
		when(borrower.hasOverDueLoans()).thenReturn(false);
		when(borrower.hasReachedLoanLimit()).thenReturn(false);
		when(borrower.hasFinesPayable()).thenReturn(false);
		when(borrower.hasReachedFineLimit()).thenReturn(false);
		when(borrower.getID()).thenReturn(id);
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
		ctl.cardSwiped(id);
		
				//Verify methods inside the cardSwiped() method
		verify(memberDAO).getMemberByID(id);
		verify(borrower).hasOverDueLoans();
		verify(borrower).hasReachedLoanLimit();
		verify(borrower).hasFinesPayable();
		verify(borrower).hasReachedFineLimit();
		verify(borrower).getID();
		verify(borrower).getFirstName();
		verify(borrower).getLastName();
		verify(borrower).getContactPhone();
		verify(ui).displayMemberDetails(id, " ", "");
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
	
	@Test
	public void testInitialise() {
		
	}



}
