package com.library.search;

import java.util.List;
import java.util.stream.Collectors;

import com.library.model.Book;

/**
 * Search books by ISBN
 */
class ISBNSearchStrategy implements BookSearchStrategy {
	@Override
	public List<Book> search(List<Book> books, String query) {
		return books.stream().filter(book -> book.getIsbn().equals(query)).collect(Collectors.toList());
	}
}