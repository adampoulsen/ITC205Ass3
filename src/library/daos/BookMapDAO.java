package library.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookMapDAO implements IBookDAO {
	
	IBookHelper helper;
	ArrayList <IBook> bookCollection = new ArrayList<IBook>();
	int id;
	
	
	public BookMapDAO(IBookHelper helper) {
		if (helper == null) {
			throw new IllegalArgumentException("Helper cannot be null");
		}
		id = 1;
		this.helper = helper;
	}

	@Override
	public IBook addBook(String author, String title, String callNo) {
		Random rand = new Random();
		int id = nextID();
		IBook book = helper.makeBook(author, title, callNo, id);
		bookCollection.add(book);
		return book;
	}

	@Override
	public IBook getBookByID(int id) {
		for (IBook book : bookCollection) {
			  if (book.getID() == id){
				  return book;
			  }
		}
		return null;
	}

	@Override
	public List<IBook> listBooks() {
		return bookCollection;
	}

	@Override
	public List<IBook> findBooksByAuthor(String author) {
		ArrayList <IBook> bookByAuthorCollection = new ArrayList<IBook>();
		for (IBook book : bookCollection) {
			  if (book.getAuthor() == author){
				  bookByAuthorCollection.add(book);
			  }
		}
		return bookByAuthorCollection;
	}

	@Override
	public List<IBook> findBooksByTitle(String title) {
		ArrayList <IBook> bookByTitleCollection = new ArrayList<IBook>();
		for (IBook book : bookCollection) {
			  if (book.getTitle() == title){
				  bookByTitleCollection.add(book);
			  }
		}
		return bookByTitleCollection;
	}

	@Override
	public List<IBook> findBooksByAuthorTitle(String author, String title) {
		ArrayList <IBook> bookByTitleAndAuthorCollection = new ArrayList<IBook>();
		for (IBook book : bookCollection) {
			  if (book.getTitle() == title){
				  bookByTitleAndAuthorCollection.add(book);
			  }
			  if (book.getTitle() == author){
				  bookByTitleAndAuthorCollection.add(book);
			  }
		}
		return bookByTitleAndAuthorCollection;
	}
	
	private int nextID() {
		return id++;
	}

}
