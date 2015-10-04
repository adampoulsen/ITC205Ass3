package library;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

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

public class BorrowUC_CTLLoansConfirmedTest {
	
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
	private IMember borrower;
	private int memberID;
	private ILoan loan;
	private ICardReader reader;
	private IDisplay display;
	private JPanel previous;
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
		previous = mock(JPanel.class);
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
	public void testLoansConfirmedNoIssues() {
		List<ILoan> loanList = mock(List.class);
		when(borrower.getLoans()).thenReturn(loanList);
		Iterator<ILoan> iterator = mock(Iterator.class);		
		when(loanList.iterator()).thenReturn(iterator);
		when(iterator.hasNext()).thenReturn(true, false);
		when(iterator.next()).thenReturn(loan);
		when(loan.toString()).thenReturn("loanDetails");
		
		when(display.getDisplay()).thenReturn(null);
		when(memberDAO.getMemberByID(memberID)).thenReturn(borrower);
		ctl.initialise();
		ctl.cardSwiped(memberID);
		ctl.loansConfirmed();
		
		assertTrue(ctl.getState() == EBorrowState.COMPLETED);
		verify(reader, atLeast(2)).setEnabled(false);
		verify(scanner, atLeast(2)).setEnabled(false);
		System.out.println(loan.toString());
		assertTrue(loan.toString() == "loanDetails");
		verify(printer).print("");
		//verify(display).setDisplay(previous, "Main Menu");
	}
	
	@Test
	public void testLoansEmptyLoanList() {
		verify(loanDAO, never()).commitLoan(loan);
	}

}
