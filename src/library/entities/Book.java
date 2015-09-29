package library.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class Book implements IBook {
	
	ILoan loan;
	IMember member;
	String author;
	String title;
	String callNumber;
	int bookID;
	EBookState state;


	public Book(String author, String title, String callNumber, int bookID) {
		if (author == null || 
			title == null || 
			callNumber == null) {
			throw new IllegalArgumentException("Paramaters cannot be null");
		}
		
		if (bookID <= 0) {
			throw new IllegalArgumentException("BookID must be a positive integer");
		}
		
		this.author = author;
		this.title = title;
		this.callNumber = callNumber;
		this.bookID = bookID;
		this.state = EBookState.AVAILABLE;
	}
	
	@Override
	public void borrow(ILoan loan) {
		this.loan = loan;
		if (state != EBookState.AVAILABLE) {
			throw new RuntimeException("Book isn't available");
		}
		else state = EBookState.ON_LOAN;
	}

	@Override
	public ILoan getLoan() {
		if (state != EBookState.ON_LOAN) {
			return null;
		}
		else return loan;
	}

	@Override
	public void returnBook(boolean damaged) {
		loan = null;
		
		if (state != EBookState.ON_LOAN) {
			throw new RuntimeException("Book isn't on loan");
		}
		
		if (damaged == true) {
			state = EBookState.DAMAGED;
		}
		else state = EBookState.AVAILABLE;
	}

	@Override
	public void lose() {
		if (state != EBookState.ON_LOAN) {
			throw new RuntimeException("Book is lost but not on loan");
		}
		
		state = EBookState.LOST;
	}

	@Override
	public void repair() {
		if (state != EBookState.DAMAGED) {
			throw new RuntimeException("Book isn't Damaged");
		}
		
		state = EBookState.AVAILABLE;
	}

	@Override
	public void dispose() {
		if (state == EBookState.ON_LOAN ||
			state == EBookState.DISPOSED) {
			throw new RuntimeException("Book isn't Available/Damaged/Lost");
		}
		
		state = EBookState.DISPOSED;
	}

	@Override
	public EBookState getState() {
		return state;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getCallNumber() {
		return callNumber;
	}

	@Override
	public int getID() {
		return bookID;
	}

}
