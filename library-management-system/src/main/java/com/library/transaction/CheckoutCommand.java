package com.library.transaction;

import java.util.logging.Logger;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;

/**
 * Concrete command for checkout operation
 */
public class CheckoutCommand implements TransactionCommand {
	private static final Logger logger = Logger.getLogger(CheckoutCommand.class.getName());

	private final Book book;
	private final Patron patron;
	private final Transaction transaction;

	public CheckoutCommand(Book book, Patron patron) {
		this.book = book;
		this.patron = patron;
		this.transaction = new Transaction(book.getIsbn(), patron.getPatronId(), TransactionType.CHECKOUT);
	}

	@Override
	public void execute() {
		if (!book.isAvailable()) {
			throw new IllegalStateException("Book is not available for checkout");
		}

		if (!patron.canCheckoutMoreBooks()) {
			throw new IllegalStateException("Patron has reached checkout limit");
		}

		book.setStatus(BookStatus.CHECKED_OUT);
		patron.addCheckout(book.getIsbn());

		BorrowingRecord record = new BorrowingRecord(book.getIsbn(), transaction.getTransactionDate());
		patron.addToBorrowingHistory(record);

		logger.info("Checkout executed: Book " + book.getIsbn() + " by Patron " + patron.getPatronId());
	}

	@Override
	public void undo() {
		book.setStatus(BookStatus.AVAILABLE);
		patron.removeCheckout(book.getIsbn());
		logger.info("Checkout undone for book: " + book.getIsbn());
	}

	public Transaction getTransaction() {
		return transaction;
	}
}