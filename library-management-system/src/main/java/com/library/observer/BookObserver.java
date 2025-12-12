package com.library.observer;

import com.library.model.Book;

/**
 * Observer Pattern implementation for book reservation notifications
 */
public interface BookObserver {
	void notify(Book book);
}
