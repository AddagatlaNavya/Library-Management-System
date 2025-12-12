package com.library.observer;

import java.util.logging.Logger;

import com.library.model.Book;
import com.library.model.Patron;

/**
 * Concrete implementation of BookObserver Notifies patrons when reserved books
 * become available
 */
public class PatronNotifier implements BookObserver {
	private static final Logger logger = Logger.getLogger(PatronNotifier.class.getName());

	private final Patron patron;

	public PatronNotifier(Patron patron) {
		this.patron = patron;
	}

	public Patron getPatron() {
		return patron;
	}

	@Override
	public void notify(Book book) {
		logger.info("NOTIFICATION: Dear " + patron.getName() + " the book " + book.getTitle()
				+ " is now available for checkout!");

		// In a real system, this would send an email or SMS
		sendNotification(patron, book);
	}

	private void sendNotification(Patron patron, Book book) {
		// Simulate sending notification
		if (patron.getEmail() != null) {
			logger.info(String.format("Email sent to " + patron.getEmail() + " about book: " + book.getTitle()));
		}
	}

}
