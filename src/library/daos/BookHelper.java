package library.daos;

import library.entities.Book;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookHelper implements IBookHelper {

	@Override
	public IBook makeBook(String author, String title, String callNumber, int id) {
		// TODO Auto-generated method stub
		IBook book = new Book(author, title, callNumber, id);
		return book;
	}

}
