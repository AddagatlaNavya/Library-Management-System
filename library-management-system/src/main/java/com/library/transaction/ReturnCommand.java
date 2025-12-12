package com.library.transaction;

import java.util.Date;
import java.util.logging.Logger;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Patron;

/**
 * Concrete command for return operation
 */
public class ReturnCommand implements TransactionCommand {
	private static final Logger logger = Logger.getLogger(ReturnCommand.class.getName());

	private final Book book;
	private final Patron patron;
	private final Transaction transaction;

	public ReturnCommand(Book book, Patron patron) {
		this.book = book;
		this.patron = patron;
		this.transaction = new Transaction(book.getIsbn(), patron.getPatronId(), TransactionType.RETURN);
	}

	@Override
	public void execute() {
		if (book.getStatus() != BookStatus.CHECKED_OUT) {
			throw new IllegalStateException("Book is not checked out");
		}

		if (!patron.getCurrentCheckouts().contains(book.getIsbn())) {
			throw new IllegalStateException("This patron did not check out this book");
		}

		book.setStatus(BookStatus.AVAILABLE);
		patron.removeCheckout(book.getIsbn());
		transaction.setReturnDate(new Date());

		// Update borrowing history
		patron.getBorrowingHistory().stream()
				.filter(record -> record.getIsbn().equals(book.getIsbn()) && !record.isReturned()).findFirst()
				.ifPresent(record -> record.setReturnDate(new Date()));

		logger.info("Return executed: Book " + book.getIsbn() + " by Patron " + patron.getPatronId());
	}

	@Override
	public void undo() {
		book.setStatus(BookStatus.CHECKED_OUT);
		patron.addCheckout(book.getIsbn());
		transaction.setReturnDate(null);
		logger.info("Return undone for book: " + book.getIsbn());
	}

	public Transaction getTransaction() {
		return transaction;
	}
}