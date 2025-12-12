package com.library.observer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

import com.library.model.Book;
import com.library.model.BookStatus;

/**
 * Manages book reservations and notifications
 */
public class ReservationManager implements BookSubject {
	private static final Logger logger = Logger.getLogger(ReservationManager.class.getName());

	private final Book book;
	private final Queue<BookObserver> waitlist;
	private final Map<String, BookObserver> observerMap;

	public ReservationManager(Book book) {
		this.book = book;
		this.waitlist = new LinkedList<>();
		this.observerMap = new HashMap<>();
	}

	@Override
	public void attach(BookObserver observer) {
		if (observer instanceof PatronNotifier) {
			PatronNotifier notifier = (PatronNotifier) observer;
			String patronId = notifier.getPatron().getPatronId();

			if (!observerMap.containsKey(patronId)) {
				waitlist.add(observer);
				observerMap.put(patronId, observer);
				logger.info("Patron " + patronId + " added to waitlist for book: " + book.getTitle());
			}
		}
	}

	@Override
	public void detach(BookObserver observer) {
		if (observer instanceof PatronNotifier) {
			PatronNotifier notifier = (PatronNotifier) observer;
			String patronId = notifier.getPatron().getPatronId();

			waitlist.remove(observer);
			observerMap.remove(patronId);
			logger.info(String.format("Patron " + patronId + " removed from waitlist for book: " + book.getTitle()));
		}
	}

	@Override
	public void notifyObservers() {
		// Notify only the first person in the waitlist
		if (!waitlist.isEmpty()) {
			BookObserver nextObserver = waitlist.peek();
			nextObserver.notify(book);
		}
	}

	public void bookReturned() {
		if (!waitlist.isEmpty()) {
			book.setStatus(BookStatus.RESERVED);
			notifyObservers();
		} else {
			book.setStatus(BookStatus.AVAILABLE);
		}
	}

	public void reservationFulfilled() {
		// Remove the notified patron from waitlist
		if (!waitlist.isEmpty()) {
			BookObserver fulfilled = waitlist.poll();
			if (fulfilled instanceof PatronNotifier) {
				PatronNotifier notifier = (PatronNotifier) fulfilled;
				observerMap.remove(notifier.getPatron().getPatronId());
			}
		}
	}

	public int getWaitlistSize() {
		return waitlist.size();
	}

	public Book getBook() {
		return book;
	}
}